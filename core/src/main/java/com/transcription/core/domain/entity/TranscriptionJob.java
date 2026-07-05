package com.transcription.core.domain.entity;

import com.transcription.core.domain.enums.TranscriptionStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public class TranscriptionJob {

    private final UUID id;
    private final String mediaSource;
    private final String sourceHash;

    private TranscriptionStatus status;

    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public TranscriptionJob(
            String mediaSource,
            String sourceHash,
            LocalDateTime createdAt
    ) {

        validateMediaSource(mediaSource);
        validateSourceHash(sourceHash);
        validateCreatedAt(createdAt);

        this.id = UUID.randomUUID();
        this.mediaSource = mediaSource;
        this.sourceHash = sourceHash;

        this.status = TranscriptionStatus.PENDING;

        this.createdAt = createdAt;
        this.updatedAt = createdAt;
    }

    public TranscriptionJob(UUID id, String mediaSource, String sourceHash, TranscriptionStatus status, LocalDateTime createdAt, LocalDateTime updatedAt) {
        validateMediaSource(mediaSource);
        validateSourceHash(sourceHash);
        validateCreatedAt(createdAt);

        this.id = id;
        this.mediaSource = mediaSource;
        this.sourceHash = sourceHash;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public void markAsProcessing(LocalDateTime updatedAt) {

        validateUpdatedAt(updatedAt);

        if (this.status != TranscriptionStatus.PENDING) {
            throw new IllegalStateException(
                    "Cannot mark as processing from status: " + this.status
            );
        }

        this.status = TranscriptionStatus.PROCESSING;
        this.updatedAt = updatedAt;
    }

    public void markAsCompleted(LocalDateTime updatedAt) {

        validateUpdatedAt(updatedAt);

        if (this.status != TranscriptionStatus.PROCESSING) {
            throw new IllegalStateException(
                    "Cannot mark as completed from status: " + this.status
            );
        }

        this.status = TranscriptionStatus.COMPLETED;
        this.updatedAt = updatedAt;
    }

    public void markAsFailed(LocalDateTime updatedAt) {

        validateUpdatedAt(updatedAt);

        if (this.status != TranscriptionStatus.PROCESSING) {
            throw new IllegalStateException(
                    "Cannot mark as failed from status: " + this.status
            );
        }

        this.status = TranscriptionStatus.FAILED;
        this.updatedAt = updatedAt;
    }

    private void validateMediaSource(String mediaSource) {

        if (mediaSource == null || mediaSource.isBlank()) {
            throw new IllegalArgumentException(
                    "Media source cannot be null or empty"
            );
        }
    }

    private void validateSourceHash(String sourceHash) {

        if (sourceHash == null || sourceHash.isBlank()) {
            throw new IllegalArgumentException(
                    "Source hash cannot be null or empty"
            );
        }
    }

    private void validateCreatedAt(LocalDateTime createdAt) {

        if (createdAt == null) {
            throw new IllegalArgumentException(
                    "CreatedAt cannot be null"
            );
        }
    }

    private void validateUpdatedAt(LocalDateTime updatedAt) {

        if (updatedAt == null) {
            throw new IllegalArgumentException(
                    "UpdatedAt cannot be null"
            );
        }

        if (updatedAt.isBefore(this.updatedAt)) {
            throw new IllegalArgumentException(
                    "UpdatedAt cannot be before current updatedAt"
            );
        }
    }

    public UUID getId() {
        return id;
    }

    public String getMediaSource() {
        return mediaSource;
    }

    public String getSourceHash() {
        return sourceHash;
    }

    public TranscriptionStatus getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}