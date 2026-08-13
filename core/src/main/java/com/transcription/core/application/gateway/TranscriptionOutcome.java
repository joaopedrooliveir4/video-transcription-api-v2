package com.transcription.core.application.gateway;

public class TranscriptionOutcome {

    private final boolean success;
    private final String text;

    private TranscriptionOutcome(boolean success, String text) {
        this.success = success;
        this.text = text;
    }

    public static TranscriptionOutcome success(String text) {
        return new TranscriptionOutcome(true, text);
    }

    public static TranscriptionOutcome failure() {
        return new TranscriptionOutcome(false, null);
    }

    public boolean isSuccess() {
        return success;
    }

    public String getText() {
        return text;
    }
}