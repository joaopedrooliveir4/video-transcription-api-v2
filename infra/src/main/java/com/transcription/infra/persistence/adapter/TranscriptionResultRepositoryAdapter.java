package com.transcription.infra.persistence.adapter;

import com.transcription.core.application.gateway.TranscriptionResultRepository;
import com.transcription.core.domain.entity.TranscriptionResult;
import com.transcription.infra.persistence.mapper.TranscriptionResultMapper;
import com.transcription.infra.persistence.repository.SpringDataTranscriptionResultRepository;
import org.springframework.stereotype.Repository;

@Repository
public class TranscriptionResultRepositoryAdapter implements TranscriptionResultRepository {

    private final SpringDataTranscriptionResultRepository repository;

    public TranscriptionResultRepositoryAdapter(SpringDataTranscriptionResultRepository repository) {
        this.repository = repository;
    }

    @Override
    public void save(TranscriptionResult result) {
        repository.save(TranscriptionResultMapper.toJpaEntity(result));
    }
}