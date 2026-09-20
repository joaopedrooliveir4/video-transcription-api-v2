package com.transcription.core.application.gateway;

import com.transcription.core.domain.entity.TranscriptionResult;

import java.util.Optional;
import java.util.UUID;

public interface TranscriptionResultRepository {
    void save(TranscriptionResult result);
    Optional<TranscriptionResult> findByJobId(UUID jobId);
}