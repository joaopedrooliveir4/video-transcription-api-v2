package com.transcription.infra.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "jobs")
public class TranscriptionJobJpaEntity {

    @Id
    private UUID id;

    @Column(name = "media_source", nullable = false)
    private String mediaSource;

    @Column(name = "source_hash", nullable = false, length = 64)
    private String sourceHash;

    @Column(nullable = false)
    private String status;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    protected TranscriptionJobJpaEntity() {
    }

    public TranscriptionJobJpaEntity(
            UUID id,
            String mediaSource,
            String sourceHash,
            String status,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        this.id = id;
        this.mediaSource = mediaSource;
        this.sourceHash = sourceHash;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
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

    public String getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}