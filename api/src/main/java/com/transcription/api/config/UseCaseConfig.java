package com.transcription.api.config;

import com.transcription.core.application.gateway.SourceHashGenerator;
import com.transcription.core.application.gateway.TranscriptionEventPublisher;
import com.transcription.core.application.gateway.TranscriptionJobRepository;
import com.transcription.core.application.usecase.CreateTranscriptionJobUseCase;
import com.transcription.core.application.usecase.impl.CreateTranscriptionJobUseCaseImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

@Configuration
public class UseCaseConfig {

    @Bean
    public CreateTranscriptionJobUseCase createTranscriptionJobUseCase(
            TranscriptionEventPublisher transcriptionEventPublisher,
            TranscriptionJobRepository repository,
            SourceHashGenerator hashGenerator,
            Clock clock
    ) {
        return new CreateTranscriptionJobUseCaseImpl(transcriptionEventPublisher, repository, hashGenerator, clock);
    }

    @Bean
    public Clock clock() {
        return Clock.systemDefaultZone();
    }
}
