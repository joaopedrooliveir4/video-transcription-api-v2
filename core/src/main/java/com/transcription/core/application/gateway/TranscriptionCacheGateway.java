package com.transcription.core.application.gateway;

public interface TranscriptionCacheGateway {
    boolean exists (String sourceHash);
    void save (String sourceHash);
}