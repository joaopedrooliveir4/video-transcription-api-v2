package com.transcription.core.application.usecase;

import com.transcription.core.application.dto.FindTranscriptionJobResponse;

import java.util.UUID;

public interface FindTranscriptionJobUseCase {
    FindTranscriptionJobResponse execute(UUID jobId);
}