package com.transcription.core.application.gateway;

import com.transcription.core.domain.entity.TranscriptionJob;

import java.util.Optional;

public interface TranscriptionJobRepository {
    void save(TranscriptionJob job);
    Optional<TranscriptionJob> findBySourceHash(String sourceHash);
}