package com.transcription.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.transcription.api.dto.request.CreateTranscriptionRequest;
import com.transcription.core.application.dto.CreateTranscriptionJobResponse;
import com.transcription.core.application.usecase.CreateTranscriptionJobUseCase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TranscriptionController.class)
class TranscriptionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CreateTranscriptionJobUseCase createTranscriptionJobUseCase;

    @Test
    void deveCriarTranscricaoComSucesso() throws Exception {
        UUID jobId = UUID.randomUUID();
        when(createTranscriptionJobUseCase.execute(any()))
                .thenReturn(new CreateTranscriptionJobResponse(jobId));

        var request = new CreateTranscriptionRequest("https://youtube.com/watch?v=abc123");

        mockMvc.perform(post("/v2/transcriptions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.jobId").value(jobId.toString()));
    }

    @Test
    void deveRetornar400QuandoMediaSourceEmBranco() throws Exception {
        var request = new CreateTranscriptionRequest("");

        mockMvc.perform(post("/v2/transcriptions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").exists());
    }

    @Test
    void deveRetornar400QuandoMediaSourceForNulo() throws Exception {
        String requestJson = "{\"mediaSource\": null}";

        mockMvc.perform(post("/v2/transcriptions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").exists());
    }

    @Test
    void deveRetornar400QuandoUseCaseLancarIllegalArgumentException() throws Exception {
        when(createTranscriptionJobUseCase.execute(any()))
                .thenThrow(new IllegalArgumentException("mediaSource invalido"));

        var request = new CreateTranscriptionRequest("fonte-invalida");

        mockMvc.perform(post("/v2/transcriptions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("mediaSource invalido"));
    }
}