package com.transcription.core.application.usecase.impl;

import com.transcription.core.application.dto.FindTranscriptionJobResponse;
import com.transcription.core.application.gateway.TranscriptionJobRepository;
import com.transcription.core.application.gateway.TranscriptionResultRepository;
import com.transcription.core.application.usecase.FindTranscriptionJobUseCase;
import com.transcription.core.domain.entity.TranscriptionJob;

import java.util.UUID;

public class FindTranscriptionJobUseCaseImpl implements FindTranscriptionJobUseCase {

    private final TranscriptionJobRepository jobRepository;
    private final TranscriptionResultRepository resultRepository;

    public FindTranscriptionJobUseCaseImpl(TranscriptionJobRepository jobRepository, TranscriptionResultRepository resultRepository) {
        this.jobRepository = jobRepository;
        this.resultRepository = resultRepository;
    }

    @Override
    public FindTranscriptionJobResponse execute(UUID jobId) {

        TranscriptionJob job = jobRepository.findById(jobId)
                .orElseThrow(() -> new IllegalArgumentException("Job not found: " + jobId));

        String text = resultRepository.findByJobId(jobId)
                .map(result -> result.getText())
                .orElse(null);

        return new FindTranscriptionJobResponse(
                job.getStatus(),
                text,
                job.getErrorMessage()
        );
    }
}