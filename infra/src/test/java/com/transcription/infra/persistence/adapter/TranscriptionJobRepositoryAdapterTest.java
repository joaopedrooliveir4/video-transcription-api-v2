package com.transcription.infra.persistence.adapter;

import com.transcription.core.domain.entity.TranscriptionJob;
import com.transcription.core.domain.enums.TranscriptionStatus;
import com.transcription.infra.TestApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@SpringBootTest(classes = TestApplication.class)
class TranscriptionJobRepositoryAdapterTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    private TranscriptionJobRepositoryAdapter adapter;

    @Test
    void deveSalvarEBuscarJobPorId() {
        // Arrange
        TranscriptionJob job = new TranscriptionJob("https://youtube.com/watch?v=abc", "hash-123", LocalDateTime.now());

        // Act
        adapter.save(job);
        Optional<TranscriptionJob> encontrado = adapter.findById(job.getId());

        // Assert
        assertTrue(encontrado.isPresent());
        assertEquals(job.getId(), encontrado.get().getId());
        assertEquals(job.getMediaSource(), encontrado.get().getMediaSource());
        assertEquals(TranscriptionStatus.PENDING, encontrado.get().getStatus());
    }

    @Test
    void deveBuscarJobPorSourceHash() {
        // Arrange
        TranscriptionJob job = new TranscriptionJob("https://youtube.com/watch?v=xyz", "hash-unico-456", LocalDateTime.now());
        adapter.save(job);

        // Act
        Optional<TranscriptionJob> encontrado = adapter.findBySourceHash("hash-unico-456");

        // Assert
        assertTrue(encontrado.isPresent());
        assertEquals(job.getId(), encontrado.get().getId());
    }

    @Test
    void deveRetornarVazioQuandoJobNaoExiste() {
        // Act
        Optional<TranscriptionJob> encontrado = adapter.findById(UUID.randomUUID());

        // Assert
        assertTrue(encontrado.isEmpty());
    }

    @Test
    void deveAtualizarStatusAoSalvarJobExistente() {
        // Arrange
        TranscriptionJob job = new TranscriptionJob("https://youtube.com/watch?v=update", "hash-update", LocalDateTime.now());
        adapter.save(job);

        // Act
        job.markAsProcessing(LocalDateTime.now().plusMinutes(1));
        adapter.save(job);
        Optional<TranscriptionJob> atualizado = adapter.findById(job.getId());

        // Assert
        assertTrue(atualizado.isPresent());
        assertEquals(TranscriptionStatus.PROCESSING, atualizado.get().getStatus());
    }
}