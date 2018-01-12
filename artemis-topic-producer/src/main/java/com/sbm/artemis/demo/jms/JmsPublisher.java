package com.sbm.artemis.demo.jms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import com.sbm.artemis.demo.models.Company;

@Component
public class JmsPublisher {

	public static final Logger log = LoggerFactory.getLogger(JmsPublisher.class);

	@Autowired
	JmsTemplate jmsTemplate;
	
	@Value("${jms.topic.destination}")
	String topic;
	
	public void send(Company company){
		System.out.println("Sending Message: " + company.toString());
		jmsTemplate.convertAndSend(topic, company);
	}
}
