package com.transcription.core.domain.entity;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class TranscriptionResultTest {

    @Test
    void deveCriarResultComIdGeradoAutomaticamente() {
        // Arrange
        UUID jobId = UUID.randomUUID();
        String texto = "Transcrição de teste";
        LocalDateTime agora = LocalDateTime.now();

        // Act
        TranscriptionResult result = new TranscriptionResult(jobId, texto, agora);

        // Assert
        assertNotNull(result.getId());
        assertEquals(jobId, result.getJobId());
        assertEquals(texto, result.getText());
        assertEquals(agora, result.getCreatedAt());
    }

    @Test
    void doisResultsDevemTerIdsDiferentes() {
        // Arrange
        UUID jobId = UUID.randomUUID();
        LocalDateTime agora = LocalDateTime.now();

        // Act
        TranscriptionResult result1 = new TranscriptionResult(jobId, "texto 1", agora);
        TranscriptionResult result2 = new TranscriptionResult(jobId, "texto 2", agora);

        // Assert
        assertNotEquals(result1.getId(), result2.getId());
    }
}