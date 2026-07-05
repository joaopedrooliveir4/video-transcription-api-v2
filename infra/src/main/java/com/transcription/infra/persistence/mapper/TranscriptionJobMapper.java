package com.transcription.infra.persistence.mapper;

import com.transcription.core.domain.entity.TranscriptionJob;
import com.transcription.core.domain.enums.TranscriptionStatus;
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

    public static TranscriptionJob toDomain(
            TranscriptionJobJpaEntity jobJpaEntity
    ) {
        return new TranscriptionJob(
                jobJpaEntity.getId(),
                jobJpaEntity.getMediaSource(),
                jobJpaEntity.getSourceHash(),
                TranscriptionStatus.valueOf(jobJpaEntity.getStatus()),
                jobJpaEntity.getCreatedAt(),
                jobJpaEntity.getUpdatedAt()
        );
    }
}