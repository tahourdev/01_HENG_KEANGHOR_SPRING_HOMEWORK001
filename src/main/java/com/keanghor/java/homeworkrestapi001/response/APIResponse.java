package com.keanghor.java.homeworkrestapi001.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data
@RequiredArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class APIResponse<T> {
    private final boolean success;
    private final String message;
    private final HttpStatus status;
    private final T payload;
    private LocalDateTime timestamp = LocalDateTime.now();
}
