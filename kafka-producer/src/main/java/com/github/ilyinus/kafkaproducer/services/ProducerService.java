package com.github.ilyinus.kafkaproducer.services;

import com.github.ilyinus.dto.AbstractMessage;
import com.github.ilyinus.dto.Message;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProducerService {

	private final KafkaTemplate<String, AbstractMessage> kafkaTemplate;

	@Value("${kafka.topic}")
	private String topic;

	public void produce(String message) {
		ListenableFuture<SendResult<String, AbstractMessage>> future = kafkaTemplate.send(topic, new Message(message));

		future.addCallback(new ListenableFutureCallback<>() {
			@Override
			public void onFailure(Throwable ex) {
				log.error("Unable to send message=[{}] due to : {}", message, ex.getMessage());
			}

			@Override
			public void onSuccess(SendResult<String, AbstractMessage> result) {
				RecordMetadata metadata = result.getRecordMetadata();
				log.info("Sent: message=[{}]; topic=[{}]; partition=[{}]; offset=[{}]",
						message, topic, metadata.partition(), metadata.offset());
			}
		});
	}
}
