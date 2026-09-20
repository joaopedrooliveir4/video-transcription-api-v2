package com.transcription.api.mapper;

import com.transcription.api.dto.request.CreateTranscriptionRequest;
import com.transcription.api.dto.response.TranscriptionJobResponse;
import com.transcription.api.dto.response.TranscriptionStatusResponse;
import com.transcription.core.application.dto.CreateTranscriptionJobRequest;
import com.transcription.core.application.dto.CreateTranscriptionJobResponse;
import com.transcription.core.application.dto.FindTranscriptionJobResponse;

public class TranscriptionHttpMapper {
    private TranscriptionHttpMapper() {}

    public static CreateTranscriptionJobRequest toUseCaseInput(CreateTranscriptionRequest request) {
        return new CreateTranscriptionJobRequest(request.mediaSource());
    }

    public static TranscriptionJobResponse toResponse(CreateTranscriptionJobResponse response) {
        return new TranscriptionJobResponse(response.jobId());
    }

    public static TranscriptionStatusResponse toResponse(FindTranscriptionJobResponse response) {
        return new TranscriptionStatusResponse(response.status(), response.text(), response.errorMessage());
    }
}