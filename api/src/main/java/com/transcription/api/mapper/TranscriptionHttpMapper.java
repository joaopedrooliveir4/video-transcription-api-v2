package com.transcription.api.mapper;

import com.transcription.api.dto.request.CreateTranscriptionRequest;
import com.transcription.api.dto.response.TranscriptionJobResponse;
import com.transcription.core.application.dto.CreateTranscriptionJobRequest;
import com.transcription.core.application.dto.CreateTranscriptionJobResponse;

public class TranscriptionHttpMapper {
    private TranscriptionHttpMapper() {}

    public static CreateTranscriptionJobRequest toUseCaseInput(CreateTranscriptionRequest request) {
        return new CreateTranscriptionJobRequest(request.mediaSource());
    }

    public static TranscriptionJobResponse toResponse(CreateTranscriptionJobResponse response) {
        return new TranscriptionJobResponse(response.jobId());
    }
}
