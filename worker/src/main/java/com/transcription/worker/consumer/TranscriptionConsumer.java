package com.transcription.worker.consumer;

import com.transcription.core.application.event.TranscriptionJobCreatedEvent;
import com.transcription.core.application.gateway.TranscriptionJobRepository;
import com.transcription.core.domain.entity.TranscriptionJob;
import com.transcription.infra.config.RabbitMQConfig;
import com.transcription.worker.service.TranscriptionProcessorService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.LocalDateTime;

@Component
public class TranscriptionConsumer {

    private final TranscriptionProcessorService transcriptionProcessorService;

    public TranscriptionConsumer(TranscriptionProcessorService transcriptionProcessorService) {
        this.transcriptionProcessorService = transcriptionProcessorService;
    }

    @RabbitListener(queues = RabbitMQConfig.QUEUE_NAME)
    public void consume(TranscriptionJobCreatedEvent event) {
        transcriptionProcessorService.process(event.jobId(), event.sourceHash());
    }
}