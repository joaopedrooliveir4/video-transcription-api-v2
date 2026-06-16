package com.transcription.infra.messaging;

import com.transcription.core.application.event.TranscriptionJobCreatedEvent;
import com.transcription.core.application.gateway.TranscriptionEventPublisher;
import com.transcription.infra.config.RabbitMQConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class TranscriptionEventPublisherImpl implements TranscriptionEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    public TranscriptionEventPublisherImpl(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void publishJobCreated(UUID jobId, String sourceHash) {
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE_NAME,
                RabbitMQConfig.ROUTING_KEY,
                new TranscriptionJobCreatedEvent(jobId, sourceHash)
        );
    }
}
