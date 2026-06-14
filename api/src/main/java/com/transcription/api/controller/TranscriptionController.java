package com.transcription.api.controller;

import com.transcription.api.dto.request.CreateTranscriptionRequest;
import com.transcription.api.dto.response.TranscriptionJobResponse;
import com.transcription.api.mapper.TranscriptionHttpMapper;
import com.transcription.core.application.usecase.CreateTranscriptionJobUseCase;
import com.transcription.infra.persistence.mapper.TranscriptionJobMapper;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v2/transcriptions")
public class TranscriptionController {

    private final CreateTranscriptionJobUseCase createTranscriptionJobUseCase;

    public TranscriptionController(CreateTranscriptionJobUseCase createTranscriptionJobUseCase) {
        this.createTranscriptionJobUseCase = createTranscriptionJobUseCase;
    }

    @PostMapping
    public ResponseEntity<TranscriptionJobResponse> create (
            @RequestBody @Valid CreateTranscriptionRequest request
            ) {
        var response = createTranscriptionJobUseCase.execute(TranscriptionHttpMapper.toUseCaseInput(request));

        return ResponseEntity.status(HttpStatus.CREATED).body(TranscriptionHttpMapper.toResponse(response));
    }
}
