package com.github.ilyinus.kafkaconsumer.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionControllerAdvice {
	@ExceptionHandler
	public ResponseEntity<?> handleResourceNotFoundException(BadRequestException exception) {
		AppError error = new AppError(HttpStatus.NOT_FOUND.value(), exception.getMessage());
		return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
	}
}
