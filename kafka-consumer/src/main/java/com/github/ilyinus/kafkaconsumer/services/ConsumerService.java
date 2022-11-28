package com.github.ilyinus.kafkaconsumer.services;

import com.github.ilyinus.dto.Message;
import com.github.ilyinus.kafkaconsumer.exceptions.BadRequestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndTimestamp;
import org.apache.kafka.common.TopicPartition;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ConsumerService {

	@Value("${kafka.topic}")
	private String topic;
	private final KafkaConsumer<String, Message> kafkaConsumer;

	@KafkaListener(topics = "${kafka.topic}", groupId = "${spring.kafka.consumer.group-id}", containerFactory = "kafkaListenerContainerFactory")
	public void consume(@Payload Message message,
						@Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
						@Header(KafkaHeaders.RECEIVED_PARTITION_ID) Integer partition,
						@Header(KafkaHeaders.OFFSET) Long offset,
						// Можно получить хедеры по отдельности, а можно все сразу через @Headers
						//@Headers MessageHeaders headers,
						Acknowledgment ack) {
		log.info("Received: message=[{}]; topic=[{}]; partition=[{}]; offset=[{}]", message, topic, partition, offset);
		ack.acknowledge();
	}

	public List<Message> getMessages(String date) {
		List<Message> result = new ArrayList<>();

		List<TopicPartition> partitions = kafkaConsumer.partitionsFor(topic)
				.stream()
				.map(info -> new TopicPartition(topic, info.partition()))
				.collect(Collectors.toList());

		kafkaConsumer.assign(partitions);

		// Получение данных по времени
		if (date != null) {

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

			try {
				Date dateTime = sdf.parse(date);
				Map<TopicPartition, Long> partitionTimestampMap = partitions.stream()
						.collect(Collectors.toMap(tp -> tp, tp -> dateTime.getTime()));
				Map<TopicPartition, OffsetAndTimestamp> partitionOffsetMap = kafkaConsumer.offsetsForTimes(partitionTimestampMap);
				partitionOffsetMap.forEach((tp, offsetAndTimestamp) -> kafkaConsumer.seek(tp, offsetAndTimestamp.offset()));
			} catch (ParseException e) {
				throw new BadRequestException("Unable to convert date");
			}

		}

		ConsumerRecords<String, Message> records = kafkaConsumer.poll(Duration.ofMillis(100));

		for (ConsumerRecord<String, Message> record : records) {
			result.add(record.value());
		}

		return result;
	}
}
