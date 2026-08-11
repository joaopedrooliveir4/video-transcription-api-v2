package com.transcription.core.application.gateway;

import com.transcription.core.domain.entity.TranscriptionJob;

import java.util.Optional;
import java.util.UUID;

public interface TranscriptionJobRepository {
    void save(TranscriptionJob job);
    Optional<TranscriptionJob> findBySourceHash(String sourceHash);
    Optional<TranscriptionJob> findById(UUID id);
}