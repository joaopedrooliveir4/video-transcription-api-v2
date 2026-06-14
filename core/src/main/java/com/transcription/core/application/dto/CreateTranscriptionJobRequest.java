package com.transcription.core.application.dto;

public record CreateTranscriptionJobRequest(String mediaSource) {
    public CreateTranscriptionJobRequest {
        if (mediaSource == null || mediaSource.isBlank()) {
            throw new IllegalArgumentException("Media source cannot be null or empty");
        }
    }
}