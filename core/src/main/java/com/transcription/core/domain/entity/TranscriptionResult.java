package com.transcription.core.domain.entity;

import java.time.LocalDateTime;
import java.util.UUID;

public class TranscriptionResult {

    private final UUID id;
    private final UUID jobId;
    private final String text;
    private final LocalDateTime createdAt;

    public TranscriptionResult(UUID jobId, String text, LocalDateTime createdAt) {
        this.id = UUID.randomUUID();
        this.jobId = jobId;
        this.text = text;
        this.createdAt = createdAt;
    }

    public UUID getId() { return id; }
    public UUID getJobId() { return jobId; }
    public String getText() { return text; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}