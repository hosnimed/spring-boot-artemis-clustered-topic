package com.sbm.artemis.demo.jms;

import com.sbm.artemis.demo.models.Company;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JmsSubscriber {

	public static final Logger log = LoggerFactory.getLogger(JmsSubscriber.class);

	@Value("${jms.topic.client_id:${random.value}}")
	String clientID;

	@Value("${jms.topic.subscription_name:${random.value}}")
	String subscriptionName;

	@Value("${spring.artemis.port:unknown}")
	String port;

	@JmsListener(destination = "${jms.topic.destination}", containerFactory = "pubSubFactory", subscription = "${jms.topic.subscription_name:${random.value}", id = "${jms.topic.client_id:${random.value}}")
	public void receive(Company msg){
		try {
			Runtime.getRuntime().exec("clear");
//			System.out.print("\033[H\033[2J");
//			System.out.flush();
		} catch (IOException e) {
			log.error(e.getMessage());
		}
		System.out.println(String.format("=========================CONSUMER %s RUNNING WITH SUBSCRIPTION %s ON NODE @ PORT %s=========================", clientID, subscriptionName, port));
		System.out.println("Recieved Message: " + msg);
	}
}
