package com.transcription.core.application.usecase;

import com.transcription.core.application.dto.CreateTranscriptionJobRequest;
import com.transcription.core.application.dto.CreateTranscriptionJobResponse;

public interface CreateTranscriptionJobUseCase {
    CreateTranscriptionJobResponse execute(
            CreateTranscriptionJobRequest request
    );
}