package com.transcription.core.application.gateway;

import java.util.UUID;

public interface TranscriptionEventPublisher {
    void publishJobCreated(UUID jobId, String sourceHash);
}