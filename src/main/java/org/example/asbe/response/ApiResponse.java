package org.example.asbe.response;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public class ApiResponse<T> {
    private int status;
    private String message;
    private T data;
    private Instant timestamp;
    private String path;
    private String traceId;
    private List<String> errors;

    public ApiResponse(int status, String message, T data, String path, List<String> errors) {
        this.status = status;
        this.message = message;
        this.data = data;
        this.timestamp = Instant.now();
        this.path = path;
        this.traceId = UUID.randomUUID().toString();
        this.errors = errors;
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public String getPath() {
        return path;
    }

    public String getTraceId() {
        return traceId;
    }

    public List<String> getErrors() {
        return errors;
    }
}