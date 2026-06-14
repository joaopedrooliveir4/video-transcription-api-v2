package com.transcription.api.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CreateTranscriptionRequest(
        @NotBlank String mediaSource
) {}