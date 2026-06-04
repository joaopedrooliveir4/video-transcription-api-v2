package com.transcription.core.application.usecase.impl;

import com.transcription.core.application.dto.CreateTranscriptionJobRequest;
import com.transcription.core.application.dto.CreateTranscriptionJobResponse;
import com.transcription.core.application.gateway.SourceHashGenerator;
import com.transcription.core.application.gateway.TranscriptionJobRepository;
import com.transcription.core.application.usecase.CreateTranscriptionJobUseCase;
import com.transcription.core.domain.entity.TranscriptionJob;

import java.time.Clock;
import java.time.LocalDateTime;

public class CreateTranscriptionJobUseCaseImpl implements CreateTranscriptionJobUseCase {

    private final TranscriptionJobRepository repository;
    private final SourceHashGenerator hashGenerator;
    private final Clock clock;

    public CreateTranscriptionJobUseCaseImpl(TranscriptionJobRepository repository, SourceHashGenerator hashGenerator, Clock clock) {
        this.repository = repository;
        this.hashGenerator = hashGenerator;
        this.clock = clock;
    }

    @Override
    public CreateTranscriptionJobResponse execute(
            CreateTranscriptionJobRequest request
    ) {

        String sourceHash = hashGenerator.generate(
                request.mediaSource()
        );

        TranscriptionJob job = new TranscriptionJob(
                request.mediaSource(),
                sourceHash,
                LocalDateTime.now(clock)
        );

        repository.save(job);

        return new CreateTranscriptionJobResponse(
                job.getId()
        );
    }
}