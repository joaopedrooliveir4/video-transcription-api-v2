package com.transcription.api.controller;

import com.transcription.api.dto.request.CreateTranscriptionRequest;
import com.transcription.api.dto.response.TranscriptionJobResponse;
import com.transcription.api.dto.response.TranscriptionStatusResponse;
import com.transcription.api.mapper.TranscriptionHttpMapper;
import com.transcription.core.application.usecase.CreateTranscriptionJobUseCase;
import com.transcription.core.application.usecase.FindTranscriptionJobUseCase;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/v2/transcriptions")
public class TranscriptionController {

    private final CreateTranscriptionJobUseCase createTranscriptionJobUseCase;
    private final FindTranscriptionJobUseCase findTranscriptionJobUseCase;

    public TranscriptionController(CreateTranscriptionJobUseCase createTranscriptionJobUseCase, FindTranscriptionJobUseCase findTranscriptionJobUseCase) {
        this.createTranscriptionJobUseCase = createTranscriptionJobUseCase;
        this.findTranscriptionJobUseCase = findTranscriptionJobUseCase;
    }

    @PostMapping
    public ResponseEntity<TranscriptionJobResponse> create (
            @RequestBody @Valid CreateTranscriptionRequest request
    ) {
        var response = createTranscriptionJobUseCase.execute(TranscriptionHttpMapper.toUseCaseInput(request));

        return ResponseEntity.status(HttpStatus.CREATED).body(TranscriptionHttpMapper.toResponse(response));
    }

    @GetMapping("/{jobId}")
    public ResponseEntity<TranscriptionStatusResponse> find(@PathVariable UUID jobId) {
        var response = findTranscriptionJobUseCase.execute(jobId);

        return ResponseEntity.ok(TranscriptionHttpMapper.toResponse(response));
    }
}