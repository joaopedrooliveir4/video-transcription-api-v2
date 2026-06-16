package com.transcription.core.application.event;

import java.util.UUID;

public record TranscriptionJobCreatedEvent(UUID jobId, String sourceHash) {}