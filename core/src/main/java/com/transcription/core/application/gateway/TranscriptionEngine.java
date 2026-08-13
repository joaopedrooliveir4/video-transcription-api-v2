package com.transcription.core.application.gateway;

import java.util.UUID;

public interface TranscriptionEngine {
    TranscriptionOutcome transcribe(UUID jobId, String mediaSource);
}