package com.transcription.worker.engine;

import com.transcription.core.application.gateway.TranscriptionEngine;
import com.transcription.core.application.gateway.TranscriptionOutcome;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Slf4j
@Component
public class ProcessBuilderTranscriptionEngine implements TranscriptionEngine {

    @Override
    public TranscriptionOutcome transcribe(UUID jobId, String mediaSource) {
        String outputPath = System.getProperty("java.io.tmpdir") + "/" + jobId + ".mp3";

        try {
            ProcessBuilder ytDlp = new ProcessBuilder(
                    "yt-dlp", "-x", "--audio-format", "mp3", "-o", outputPath, mediaSource
            );
            ytDlp.redirectErrorStream(true);
            Process ytProcess = ytDlp.start();
            ytProcess.getInputStream().readAllBytes();
            int ytExitCode = ytProcess.waitFor();

            if (ytExitCode != 0) {
                log.error("yt-dlp falhou para jobId={} com exitCode={}", jobId, ytExitCode);
                return TranscriptionOutcome.failure();
            }

            Path scriptPath = Path.of(System.getProperty("java.io.tmpdir"), "transcribe.py");
            try (InputStream scriptStream = getClass().getClassLoader().getResourceAsStream("transcribe.py")) {
                Files.copy(scriptStream, scriptPath, StandardCopyOption.REPLACE_EXISTING);
            }

            ProcessBuilder whisper = new ProcessBuilder("python", scriptPath.toString(), outputPath);
            whisper.redirectErrorStream(false);
            Process whisperProcess = whisper.start();

            StringBuilder stderrOutput = new StringBuilder();
            Thread stderrReader = new Thread(() -> {
                try {
                    byte[] errorBytes = whisperProcess.getErrorStream().readAllBytes();
                    stderrOutput.append(new String(errorBytes, StandardCharsets.UTF_8));
                } catch (Exception e) {
                    log.error("Erro ao ler stderr do processo Whisper para jobId={}", jobId, e);
                }
            });
            stderrReader.start();

            String transcribedText = new String(whisperProcess.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
            int whisperExitCode = whisperProcess.waitFor();
            stderrReader.join();

            if (whisperExitCode == 0 && !transcribedText.isBlank()) {
                log.info("Transcrição concluída com sucesso para jobId={}", jobId);
                return TranscriptionOutcome.success(transcribedText);
            } else {
                log.error("Whisper falhou para jobId={} com exitCode={}. Stderr: {}", jobId, whisperExitCode, stderrOutput.toString().trim());
                return TranscriptionOutcome.failure();
            }

        } catch (Exception e) {
            log.error("Erro ao transcrever jobId={}", jobId, e);
            return TranscriptionOutcome.failure();
        }
    }
}