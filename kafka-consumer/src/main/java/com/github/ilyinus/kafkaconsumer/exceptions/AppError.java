package com.github.ilyinus.kafkaconsumer.exceptions;

import lombok.Data;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;

@Data
public class AppError {
	private final Date timestamp;
	private final int status;
	private final Collection<String> messages;

	public AppError(int status, String... messages) {
		this.status = status;
		this.messages = Arrays.asList(messages);
		this.timestamp = new Date();
	}

	public AppError(int status, Collection<String> messages) {
		this.status = status;
		this.messages = messages;
		this.timestamp = new Date();
	}
}
