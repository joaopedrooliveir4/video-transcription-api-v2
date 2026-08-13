package com.transcription.core.domain.entity;

import com.transcription.core.domain.enums.TranscriptionStatus;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class TranscriptionJobTest {

    @Test
    void deveCriarJobComStatusPendingPorPadrao() {
        // Arrange & Act
        TranscriptionJob job = new TranscriptionJob(
                "https://youtube.com/watch?v=abc123",
                "hash-123",
                LocalDateTime.now()
        );

        // Assert
        assertEquals(TranscriptionStatus.PENDING, job.getStatus());
        assertNotNull(job.getId());
    }

    @Test
    void deveMarcarComoProcessingQuandoEstiverPending() {
        // Arrange
        LocalDateTime criadoEm = LocalDateTime.now();
        TranscriptionJob job = new TranscriptionJob("url", "hash", criadoEm);

        // Act
        job.markAsProcessing(criadoEm.plusMinutes(1));

        // Assert
        assertEquals(TranscriptionStatus.PROCESSING, job.getStatus());
    }

    @Test
    void naoDeveMarcarComoProcessingSeJaEstiverProcessing() {
        // Arrange
        LocalDateTime criadoEm = LocalDateTime.now();
        TranscriptionJob job = new TranscriptionJob("url", "hash", criadoEm);
        job.markAsProcessing(criadoEm.plusMinutes(1));

        // Act & Assert
        assertThrows(IllegalStateException.class, () ->
                job.markAsProcessing(criadoEm.plusMinutes(2))
        );
    }

    @Test
    void deveMarcarComoCompletedQuandoEstiverProcessing() {
        // Arrange
        LocalDateTime criadoEm = LocalDateTime.now();
        TranscriptionJob job = new TranscriptionJob("url", "hash", criadoEm);
        job.markAsProcessing(criadoEm.plusMinutes(1));

        // Act
        job.markAsCompleted(criadoEm.plusMinutes(2));

        // Assert
        assertEquals(TranscriptionStatus.COMPLETED, job.getStatus());
    }

    @Test
    void naoDeveMarcarComoCompletedSeEstiverPending() {
        // Arrange
        TranscriptionJob job = new TranscriptionJob("url", "hash", LocalDateTime.now());

        // Act & Assert
        assertThrows(IllegalStateException.class, () ->
                job.markAsCompleted(LocalDateTime.now())
        );
    }

    @Test
    void deveMarcarComoFailedQuandoEstiverProcessing() {
        // Arrange
        LocalDateTime criadoEm = LocalDateTime.now();
        TranscriptionJob job = new TranscriptionJob("url", "hash", criadoEm);
        job.markAsProcessing(criadoEm.plusMinutes(1));

        // Act
        job.markAsFailed(criadoEm.plusMinutes(2));

        // Assert
        assertEquals(TranscriptionStatus.FAILED, job.getStatus());
    }

    @Test
    void naoDeveAceitarMediaSourceNuloOuVazio() {
        assertThrows(IllegalArgumentException.class, () ->
                new TranscriptionJob(null, "hash", LocalDateTime.now())
        );
        assertThrows(IllegalArgumentException.class, () ->
                new TranscriptionJob("  ", "hash", LocalDateTime.now())
        );
    }

    @Test
    void naoDeveAceitarSourceHashNuloOuVazio() {
        assertThrows(IllegalArgumentException.class, () ->
                new TranscriptionJob("url", null, LocalDateTime.now())
        );
    }

    @Test
    void naoDeveAceitarUpdatedAtAnteriorAoAtual() {
        // Arrange
        LocalDateTime criadoEm = LocalDateTime.now();
        TranscriptionJob job = new TranscriptionJob("url", "hash", criadoEm);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
                job.markAsProcessing(criadoEm.minusMinutes(1))
        );
    }

    @Test
    void naoDeveMarcarComoFailedSeEstiverPending() {
        // Arrange
        TranscriptionJob job = new TranscriptionJob("url", "hash", LocalDateTime.now());

        // Act & Assert
        assertThrows(IllegalStateException.class, () ->
                job.markAsFailed(LocalDateTime.now())
        );
    }
}