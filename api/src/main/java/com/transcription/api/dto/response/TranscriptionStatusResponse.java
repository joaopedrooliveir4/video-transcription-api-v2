package com.transcription.api.dto.response;

import com.transcription.core.domain.enums.TranscriptionStatus;

public record TranscriptionStatusResponse(
        TranscriptionStatus status,
        String text,
        String errorMessage
) {}