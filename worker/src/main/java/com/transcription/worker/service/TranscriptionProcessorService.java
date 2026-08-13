package com.transcription.worker.service;

import com.transcription.core.application.gateway.TranscriptionEngine;
import com.transcription.core.application.gateway.TranscriptionJobRepository;
import com.transcription.core.application.gateway.TranscriptionOutcome;
import com.transcription.core.application.gateway.TranscriptionResultRepository;
import com.transcription.core.domain.entity.TranscriptionJob;
import com.transcription.core.domain.entity.TranscriptionResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
public class TranscriptionProcessorService {
    private final TranscriptionJobRepository repository;
    private final TranscriptionResultRepository resultRepository;
    private final TranscriptionEngine engine;
    private final Clock clock;

    public TranscriptionProcessorService(
            TranscriptionJobRepository repository,
            TranscriptionResultRepository resultRepository,
            TranscriptionEngine engine,
            Clock clock
    ) {
        this.repository = repository;
        this.resultRepository = resultRepository;
        this.engine = engine;
        this.clock = clock;
    }

    public void process(UUID jobId, String sourceHash) {
        TranscriptionJob job = repository.findById(jobId).orElseThrow();
        job.markAsProcessing(LocalDateTime.now(clock));
        repository.save(job);

        try {
            TranscriptionOutcome outcome = engine.transcribe(jobId, job.getMediaSource());

            if (outcome.isSuccess()) {
                TranscriptionResult result = new TranscriptionResult(jobId, outcome.getText(), LocalDateTime.now(clock));
                resultRepository.save(result);
                job.markAsCompleted(LocalDateTime.now(clock));
            } else {
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