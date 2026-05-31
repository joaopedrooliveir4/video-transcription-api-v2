package com.transcription.core.domain.entity;

import com.transcription.core.domain.enums.TranscriptionStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public class TranscriptionJob {

    private final UUID id;
    private final String mediaSource;
    private TranscriptionStatus status;
    private final LocalDateTime createdAt;

    public TranscriptionJob(
            String mediaSource,
            LocalDateTime createdAt
    ) {

        if (mediaSource == null || mediaSource.isBlank()) {
            throw new IllegalArgumentException("Media source cannot be null or empty");
        }

        if (createdAt == null) {
            throw new IllegalArgumentException("CreatedAt cannot be null");
        }

        this.id = UUID.randomUUID();
        this.mediaSource = mediaSource;
        this.status = TranscriptionStatus.PENDING;
        this.createdAt = createdAt;
    }

    public void markAsProcessing() {

        if (this.status != TranscriptionStatus.PENDING) {
            throw new IllegalStateException(
                    "Cannot mark as processing from status: " + this.status
            );
        }

        this.status = TranscriptionStatus.PROCESSING;
    }

    public void markAsCompleted() {

        if (this.status != TranscriptionStatus.PROCESSING) {
            throw new IllegalStateException(
                    "Cannot mark as completed from status: " + this.status
            );
        }

        this.status = TranscriptionStatus.COMPLETED;
    }

    public void markAsFailed() {

        if (this.status != TranscriptionStatus.PROCESSING) {
            throw new IllegalStateException(
                    "Cannot mark as failed from status: " + this.status
            );
        }

        this.status = TranscriptionStatus.FAILED;
    }

    public UUID getId() {
        return id;
    }

    public String getMediaSource() {
        return mediaSource;
    }

    public TranscriptionStatus getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}