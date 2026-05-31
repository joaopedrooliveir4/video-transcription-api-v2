package com.transcription.core.application.gateway;

import com.transcription.core.domain.entity.TranscriptionJob;

public interface TranscriptionJobRepository {
    void save(TranscriptionJob job);
}