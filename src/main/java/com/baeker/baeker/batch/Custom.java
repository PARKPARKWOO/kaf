package com.baeker.baeker.batch;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class Custom {
    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<ErrorResponse> ErrorResponse(HttpClientErrorException e) {
        log.error(e.getMessage());
        return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
    }

    @ExceptionHandler(org.springframework.web.client.HttpClientErrorException.class)
        public ResponseEntity<ErrorResponse> ErrorResponse2(org.springframework.web.client.HttpClientErrorException e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> ErrorResponse3(NotFoundException e) {
        log.error(e.getMessage());
        return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
    }
}
