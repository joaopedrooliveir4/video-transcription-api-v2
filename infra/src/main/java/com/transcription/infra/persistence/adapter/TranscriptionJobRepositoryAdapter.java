package com.transcription.infra.persistence.adapter;

import com.transcription.core.application.gateway.TranscriptionJobRepository;
import com.transcription.core.domain.entity.TranscriptionJob;
import com.transcription.infra.persistence.mapper.TranscriptionJobMapper;
import com.transcription.infra.persistence.repository.SpringDataTranscriptionJobRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class TranscriptionJobRepositoryAdapter
        implements TranscriptionJobRepository {

    private final SpringDataTranscriptionJobRepository repository;

    public TranscriptionJobRepositoryAdapter(
            SpringDataTranscriptionJobRepository repository
    ) {
        this.repository = repository;
    }

    @Override
    public void save(TranscriptionJob job) {
        repository.save(
                TranscriptionJobMapper.toJpaEntity(job)
        );
    }

    @Override
    public Optional<TranscriptionJob> findBySourceHash(String sourceHash) {
        return repository.findBySourceHash(sourceHash).map(TranscriptionJobMapper::toDomain);
    }
}
