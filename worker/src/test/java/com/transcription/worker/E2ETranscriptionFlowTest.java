package com.transcription.worker;

import com.transcription.api.dto.request.CreateTranscriptionRequest;
import com.transcription.api.dto.response.TranscriptionJobResponse;
import com.transcription.core.application.gateway.TranscriptionEngine;
import com.transcription.core.application.gateway.TranscriptionJobRepository;
import com.transcription.core.application.gateway.TranscriptionOutcome;
import com.transcription.core.domain.entity.TranscriptionJob;
import com.transcription.core.domain.enums.TranscriptionStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.time.Duration;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@Testcontainers
@SpringBootTest(
        classes = WorkerApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = "spring.main.allow-bean-definition-overriding=true"
)
class E2ETranscriptionFlowTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16");

    @Container
    static RabbitMQContainer rabbit = new RabbitMQContainer("rabbitmq:3.13-management");

    @Container
    static GenericContainer<?> redis = new GenericContainer<>(DockerImageName.parse("redis:7-alpine"))
            .withExposedPorts(6379);

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);

        registry.add("spring.flyway.enabled", () -> "true");

        registry.add("spring.rabbitmq.host", rabbit::getHost);
        registry.add("spring.rabbitmq.port", rabbit::getAmqpPort);
        registry.add("spring.rabbitmq.username", rabbit::getAdminUsername);
        registry.add("spring.rabbitmq.password", rabbit::getAdminPassword);

        registry.add("spring.data.redis.host", redis::getHost);
        registry.add("spring.data.redis.port", () -> redis.getMappedPort(6379));
    }

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private TranscriptionJobRepository transcriptionJobRepository;

    @MockitoBean
    private TranscriptionEngine transcriptionEngine;

    @Test
    void deveProcessarJobCompletoDaApiAteWorker() {
        when(transcriptionEngine.transcribe(any(), any()))
                .thenReturn(TranscriptionOutcome.success("transcricao de teste e2e"));

        var request = new CreateTranscriptionRequest("https://youtube.com/watch?v=e2e-test-" + UUID.randomUUID());

        var response = restTemplate.postForEntity(
                "http://localhost:" + port + "/v2/transcriptions",
                request,
                TranscriptionJobResponse.class
        );

        assertThat(response.getStatusCode().value()).isEqualTo(201);
        UUID jobId = response.getBody().jobId();

        await().atMost(Duration.ofSeconds(10)).untilAsserted(() -> {
            TranscriptionJob job = transcriptionJobRepository.findById(jobId).orElseThrow();
            assertThat(job.getStatus()).isEqualTo(TranscriptionStatus.COMPLETED);
        });
    }

    @Test
    void deveMarcarJobComoFailedQuandoEngineFalha() {
        when(transcriptionEngine.transcribe(any(), any()))
                .thenReturn(TranscriptionOutcome.failure());

        var request = new CreateTranscriptionRequest("https://youtube.com/watch?v=e2e-fail-" + UUID.randomUUID());

        var response = restTemplate.postForEntity(
                "http://localhost:" + port + "/v2/transcriptions",
                request,
                TranscriptionJobResponse.class
        );

        UUID jobId = response.getBody().jobId();

        await().atMost(Duration.ofSeconds(10)).untilAsserted(() -> {
            TranscriptionJob job = transcriptionJobRepository.findById(jobId).orElseThrow();
            assertThat(job.getStatus()).isEqualTo(TranscriptionStatus.FAILED);
        });
    }
}