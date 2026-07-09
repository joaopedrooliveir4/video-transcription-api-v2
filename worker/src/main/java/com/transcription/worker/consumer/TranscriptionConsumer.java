package com.transcription.worker.consumer;

import com.transcription.core.application.event.TranscriptionJobCreatedEvent;
import com.transcription.infra.config.RabbitMQConfig;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class TranscriptionConsumer {

    @RabbitListener(queues = RabbitMQConfig.QUEUE_NAME)
    public void consume(TranscriptionJobCreatedEvent event) {}
}