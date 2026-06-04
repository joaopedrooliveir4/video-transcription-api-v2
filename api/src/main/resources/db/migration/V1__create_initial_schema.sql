CREATE TABLE transcriptions (
    id UUID PRIMARY KEY,
    source_hash VARCHAR(64) NOT NULL,
    content TEXT NOT NULL,
    created_at TIMESTAMPTZ NOT NULL,

    CONSTRAINT uq_transcriptions_source_hash
        UNIQUE (source_hash)
);

CREATE TABLE jobs (
    id UUID PRIMARY KEY,
    media_source TEXT NOT NULL,
    source_hash VARCHAR(64) NOT NULL,
    status VARCHAR(50) NOT NULL,
    created_at TIMESTAMPTZ NOT NULL,
    updated_at TIMESTAMPTZ NOT NULL
);

CREATE TABLE job_logs (
    id UUID PRIMARY KEY,
    job_id UUID NOT NULL,
    step VARCHAR(100) NOT NULL,
    status VARCHAR(50) NOT NULL,
    message TEXT NOT NULL,
    timestamp TIMESTAMPTZ NOT NULL,

    CONSTRAINT fk_job_logs_job
        FOREIGN KEY (job_id)
        REFERENCES jobs (id)
);

CREATE INDEX idx_jobs_status
    ON jobs (status);

CREATE INDEX idx_jobs_source_hash
    ON jobs (source_hash);

CREATE INDEX idx_job_logs_job_id
    ON job_logs (job_id);

CREATE INDEX idx_job_logs_timestamp
    ON job_logs (timestamp);