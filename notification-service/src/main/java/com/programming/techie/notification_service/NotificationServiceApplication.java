package com.programming.techie.notification_service;

import com.programming.techie.notification_service.event.OrderPlacedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.KafkaListener;

@SpringBootApplication
@Slf4j
@EnableKafka	// starts kafka listeners and consumer consume to kafka topics
public class NotificationServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(NotificationServiceApplication.class, args);
	}

	// kafka listener will consume all messages from kafka template
	// here in this example, when an order is placed then kafka template will push message to notificationTopic
	// here kafka listener will listen to all the messages being sent by order-service

	@KafkaListener(topics = "notificationTopic",groupId = "notificationId")
	public void handleNotification(OrderPlacedEvent orderPlacedEvent){
		// send out an email notification (can do it in another session)
		log.info("Received Notification for Order - {} ", orderPlacedEvent.getOrderNo());

	}
}
