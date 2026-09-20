package com.transcription.infra.persistence.repository;

import com.transcription.infra.persistence.entity.TranscriptionResultJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface SpringDataTranscriptionResultRepository extends JpaRepository<TranscriptionResultJpaEntity, UUID> {
    Optional<TranscriptionResultJpaEntity> findByJobId(UUID jobId);
}