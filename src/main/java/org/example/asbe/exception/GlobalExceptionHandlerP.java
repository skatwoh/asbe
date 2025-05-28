package org.example.asbe.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.example.asbe.config.slack.SlackNotifier;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandlerP {

    private final SlackNotifier slackNotifier;

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleAllExceptions(HttpServletRequest request, Exception ex) {

        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String environment = System.getenv().getOrDefault("APP_ENV", "dev");
        String path = request.getRequestURI();

        String title = String.format("[%s] ❌ Lỗi hệ thống tại %s", environment.toUpperCase(), path);

        String message = String.format("""
                        ⏰ Time: %s
                        🌐 Environment: %s
                        🚀 Endpoint: %s
                        📦 Exception: %s
                        🧨 Message: %s
                        
                        Stack Trace:
                        %s
                        """, timestamp, environment, path,
                ex.getClass().getSimpleName(), ex.getMessage(),
                getStackTraceAsString(ex)
        );


        slackNotifier.sendError(title, message);

        return ResponseEntity.internalServerError().body("Lỗi hệ thống");
    }

    private String getStackTraceAsString(Exception e) {
        StringBuilder sb = new StringBuilder();
        for (StackTraceElement element : e.getStackTrace()) {
            sb.append("  at ").append(element.toString()).append("\n");
        }
        return sb.toString();
    }
}

