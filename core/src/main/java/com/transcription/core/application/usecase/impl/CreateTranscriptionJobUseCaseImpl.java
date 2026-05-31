package com.transcription.core.application.usecase.impl;

import com.transcription.core.application.dto.CreateTranscriptionJobRequest;
import com.transcription.core.application.dto.CreateTranscriptionJobResponse;
import com.transcription.core.application.gateway.TranscriptionJobRepository;
import com.transcription.core.application.usecase.CreateTranscriptionJobUseCase;
import com.transcription.core.domain.entity.TranscriptionJob;

import java.time.Clock;
import java.time.LocalDateTime;

public class CreateTranscriptionJobUseCaseImpl implements CreateTranscriptionJobUseCase {

    private final TranscriptionJobRepository repository;
    private final Clock clock;

    public CreateTranscriptionJobUseCaseImpl(TranscriptionJobRepository repository, Clock clock) {
        this.repository = repository;
        this.clock = clock;
    }

    @Override
    public CreateTranscriptionJobResponse execute(CreateTranscriptionJobRequest request) {
        TranscriptionJob job = new TranscriptionJob(
                request.mediaSource(),
                LocalDateTime.now(clock)
        );
        repository.save(job);
        return new CreateTranscriptionJobResponse(job.getId());
    }
}