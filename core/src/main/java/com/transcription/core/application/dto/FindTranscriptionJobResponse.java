package com.transcription.core.application.dto;

import com.transcription.core.domain.enums.TranscriptionStatus;

public record FindTranscriptionJobResponse(
        TranscriptionStatus status,
        String text,
        String errorMessage
) {}