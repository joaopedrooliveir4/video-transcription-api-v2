package com.transcription.infra.persistence.mapper;

import com.transcription.core.domain.entity.TranscriptionResult;
import com.transcription.infra.persistence.entity.TranscriptionResultJpaEntity;

public final class TranscriptionResultMapper {

    private TranscriptionResultMapper() {}

    public static TranscriptionResultJpaEntity toJpaEntity(TranscriptionResult result) {
        return new TranscriptionResultJpaEntity(
                result.getId(),
                result.getJobId(),
                result.getText(),
                result.getCreatedAt()
        );
    }
}