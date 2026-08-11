package com.transcription.worker.service;

import com.transcription.core.application.gateway.TranscriptionJobRepository;
import com.transcription.core.application.gateway.TranscriptionResultRepository;
import com.transcription.core.domain.entity.TranscriptionJob;
import com.transcription.core.domain.entity.TranscriptionResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
public class TranscriptionProcessorService {
    private final TranscriptionJobRepository repository;
    private final TranscriptionResultRepository resultRepository;
    private final Clock clock;

    public TranscriptionProcessorService(TranscriptionJobRepository repository, TranscriptionResultRepository resultRepository, Clock clock) {
        this.repository = repository;
        this.resultRepository = resultRepository;
        this.clock = clock;
    }

    public void process(UUID jobId, String sourceHash) {
        TranscriptionJob job = repository.findById(jobId).orElseThrow();
        job.markAsProcessing(LocalDateTime.now(clock));
        repository.save(job);

        String outputPath = System.getProperty("java.io.tmpdir") + "/" + jobId + ".mp3";

        try {
            ProcessBuilder ytDlp = new ProcessBuilder(
                    "yt-dlp", "-x", "--audio-format", "mp3", "-o", outputPath, job.getMediaSource()
            );
            ytDlp.redirectErrorStream(true);
            Process ytProcess = ytDlp.start();
            ytProcess.getInputStream().readAllBytes();
            int ytExitCode = ytProcess.waitFor();

            if (ytExitCode != 0) {
                log.error("yt-dlp falhou para jobId={} com exitCode={}", jobId, ytExitCode);
                job.markAsFailed(LocalDateTime.now(clock));
                repository.save(job);
                return;
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
                TranscriptionResult result = new TranscriptionResult(jobId, transcribedText, LocalDateTime.now(clock));
                resultRepository.save(result);
                job.markAsCompleted(LocalDateTime.now(clock));
                log.info("Transcrição concluída com sucesso para jobId={}", jobId);
            } else {
                log.error("Whisper falhou para jobId={} com exitCode={}. Stderr: {}", jobId, whisperExitCode, stderrOutput.toString().trim());
                job.markAsFailed(LocalDateTime.now(clock));
            }

            repository.save(job);

        } catch (Exception e) {
            log.error("Erro ao processar jobId={}", jobId, e);
            job.markAsFailed(LocalDateTime.now(clock));
            repository.save(job);
        }
    }
}