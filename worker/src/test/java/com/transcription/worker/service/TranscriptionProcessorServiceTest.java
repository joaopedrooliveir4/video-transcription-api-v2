package com.transcription.worker.service;

import com.transcription.core.application.gateway.TranscriptionEngine;
import com.transcription.core.application.gateway.TranscriptionJobRepository;
import com.transcription.core.application.gateway.TranscriptionOutcome;
import com.transcription.core.application.gateway.TranscriptionResultRepository;
import com.transcription.core.domain.entity.TranscriptionJob;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TranscriptionProcessorServiceTest {

    @Mock
    private TranscriptionJobRepository jobRepository;

    @Mock
    private TranscriptionResultRepository resultRepository;

    @Mock
    private TranscriptionEngine engine;

    private Clock clock;
    private TranscriptionProcessorService service;

    @BeforeEach
    void setUp() {
        clock = Clock.fixed(Instant.parse("2026-01-01T10:00:00Z"), ZoneId.of("UTC"));
        service = new TranscriptionProcessorService(jobRepository, resultRepository, engine, clock);
    }

    @Test
    void deveMarcarJobComoCompletedQuandoEngineRetornaSucesso() {
        // Arrange
        UUID jobId = UUID.randomUUID();
        TranscriptionJob job = new TranscriptionJob("https://youtube.com/watch?v=abc", "hash-1", LocalDateTime.now(clock));
        when(jobRepository.findById(jobId)).thenReturn(Optional.of(job));
        when(engine.transcribe(eq(jobId), anyString())).thenReturn(TranscriptionOutcome.success("texto transcrito"));

        // Act
        service.process(jobId, "hash-1");

        // Assert
        assertEquals(com.transcription.core.domain.enums.TranscriptionStatus.COMPLETED, job.getStatus());
        verify(resultRepository, times(1)).save(any());
        verify(jobRepository, times(2)).save(job); // uma vez ao marcar PROCESSING, outra ao marcar COMPLETED
    }

    @Test
    void deveMarcarJobComoFailedQuandoEngineRetornaFalha() {
        // Arrange
        UUID jobId = UUID.randomUUID();
        TranscriptionJob job = new TranscriptionJob("https://youtube.com/watch?v=abc", "hash-1", LocalDateTime.now(clock));
        when(jobRepository.findById(jobId)).thenReturn(Optional.of(job));
        when(engine.transcribe(eq(jobId), anyString())).thenReturn(TranscriptionOutcome.failure());

        // Act
        service.process(jobId, "hash-1");

        // Assert
        assertEquals(com.transcription.core.domain.enums.TranscriptionStatus.FAILED, job.getStatus());
        verify(resultRepository, never()).save(any());
    }

    @Test
    void deveMarcarJobComoFailedQuandoEngineLancaExcecao() {
        // Arrange
        UUID jobId = UUID.randomUUID();
        TranscriptionJob job = new TranscriptionJob("https://youtube.com/watch?v=abc", "hash-1", LocalDateTime.now(clock));
        when(jobRepository.findById(jobId)).thenReturn(Optional.of(job));
        when(engine.transcribe(eq(jobId), anyString())).thenThrow(new RuntimeException("falha inesperada"));

        // Act
        service.process(jobId, "hash-1");

        // Assert
        assertEquals(com.transcription.core.domain.enums.TranscriptionStatus.FAILED, job.getStatus());
        verify(resultRepository, never()).save(any());
    }

    @Test
    void deveLancarExcecaoQuandoJobNaoExiste() {
        // Arrange
        UUID jobId = UUID.randomUUID();
        when(jobRepository.findById(jobId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NoSuchElementException.class, () -> service.process(jobId, "hash-1"));
        verify(engine, never()).transcribe(any(), anyString());
    }
}