package com.github.ilyinus.kafkaconsumer.exceptions;

public class BadRequestException extends RuntimeException{
	public BadRequestException(String message) {
		super(message);
	}
}
