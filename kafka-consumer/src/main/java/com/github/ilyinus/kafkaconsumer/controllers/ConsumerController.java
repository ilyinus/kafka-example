package com.github.ilyinus.kafkaconsumer.controllers;

import com.github.ilyinus.dto.Message;
import com.github.ilyinus.kafkaconsumer.services.ConsumerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class ConsumerController {

	private final ConsumerService consumerService;

	@GetMapping("/messages")
	public List<Message> getMessages(@RequestParam(required = false, name = "date") String date) {
		return consumerService.getMessages(date);
	}
}
