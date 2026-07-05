package com.transcription.infra.persistence.repository;

import com.transcription.infra.persistence.entity.TranscriptionJobJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface SpringDataTranscriptionJobRepository extends JpaRepository<TranscriptionJobJpaEntity, UUID> {
    Optional<TranscriptionJobJpaEntity> findBySourceHash(String sourceHash);
}