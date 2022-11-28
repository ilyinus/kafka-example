package com.github.ilyinus.kafkaproducer.controllers;

import com.github.ilyinus.kafkaproducer.services.ProducerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/")
public class ProducerController {

	private final ProducerService producerService;

	@PostMapping("/messages")
	public void produce(@RequestParam String message) {
		producerService.produce(message);
	}
}
