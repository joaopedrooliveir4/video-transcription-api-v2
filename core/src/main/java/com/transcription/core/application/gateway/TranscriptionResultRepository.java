package com.transcription.core.application.gateway;

import com.transcription.core.domain.entity.TranscriptionResult;

public interface TranscriptionResultRepository {
    void save(TranscriptionResult result);
}