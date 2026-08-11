package com.transcription.infra.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "transcription_results")
public class TranscriptionResultJpaEntity {

    @Id
    private UUID id;

    @Column(name = "job_id", nullable = false)
    private UUID jobId;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String text;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    protected TranscriptionResultJpaEntity() {}

    public TranscriptionResultJpaEntity(UUID id, UUID jobId, String text, LocalDateTime createdAt) {
        this.id = id;
        this.jobId = jobId;
        this.text = text;
        this.createdAt = createdAt;
    }

    public UUID getId() { return id; }
    public UUID getJobId() { return jobId; }
    public String getText() { return text; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}