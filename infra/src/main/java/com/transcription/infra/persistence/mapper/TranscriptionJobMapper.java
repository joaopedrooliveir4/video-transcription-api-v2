package com.transcription.infra.persistence.mapper;

import com.transcription.core.domain.entity.TranscriptionJob;
import com.transcription.infra.persistence.entity.TranscriptionJobJpaEntity;

public final class TranscriptionJobMapper {

    private TranscriptionJobMapper() {}

    public static TranscriptionJobJpaEntity toJpaEntity(
            TranscriptionJob job
    ) {
        return new TranscriptionJobJpaEntity(
                job.getId(),
                job.getMediaSource(),
                job.getSourceHash(),
                job.getStatus().name(),
                job.getCreatedAt(),
                job.getUpdatedAt()
        );
    }
}