package com.github.ilyinus.dto;

public class Message extends AbstractMessage {
	private String data;

	public Message() {
		super();
	}

	public Message(String data) {
		super();
		this.data = data;
	}

	public String getData() {
		return data;
	}

	@Override
	public String toString() {
		return "Message{" +
				"data='" + data + '\'' +
				'}';
	}
}
