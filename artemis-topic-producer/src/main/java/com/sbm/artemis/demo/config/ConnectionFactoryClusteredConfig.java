package com.sbm.artemis.demo.config;

import org.apache.activemq.artemis.api.core.DiscoveryGroupConfiguration;
import org.apache.activemq.artemis.api.core.UDPBroadcastEndpointFactory;
import org.apache.activemq.artemis.api.jms.ActiveMQJMSClient;
import org.apache.activemq.artemis.api.jms.JMSFactoryType;
import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;

import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;

@Configuration
@EnableJms
@Profile("clustered-topic")
public class ConnectionFactoryClusteredConfig {

	@Value("${jsa.activemq.client_id:${random.value}}")
	String clientID;

	/*
	 * Initial ConnectionFactory
	 */
	@Bean("udpConnectionFactory")
	public ConnectionFactory udpConnectionFactory(){
		ActiveMQConnectionFactory connectionFactory = null;
		try {
			UDPBroadcastEndpointFactory udpCfg = new UDPBroadcastEndpointFactory();
			udpCfg.setGroupAddress("231.7.7.7").setGroupPort(9876);
			DiscoveryGroupConfiguration groupConfiguration = new DiscoveryGroupConfiguration();
			groupConfiguration.setBroadcastEndpointFactory(udpCfg);
			connectionFactory = ActiveMQJMSClient.createConnectionFactoryWithoutHA(groupConfiguration, JMSFactoryType.TOPIC_XA_CF);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return connectionFactory;
	}

	@Bean("jacksonJmsMessageConverter")// Serialize message content to json using TextMessage
	public MessageConverter jacksonJmsMessageConverter() {
		MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
		converter.setTargetType(MessageType.TEXT);
		converter.setTypeIdPropertyName("_type");
		return converter;
	}
    
    /*
     * Used for sending Messages.
     */
	@Bean
	public JmsTemplate jmsTemplate(@Qualifier("udpConnectionFactory") ConnectionFactory connectionFactory,
								   @Qualifier("jacksonJmsMessageConverter") MessageConverter jacksonJmsMessageConverter){
	    JmsTemplate template = new JmsTemplate();
	    template.setConnectionFactory(connectionFactory);
	    template.setMessageConverter(jacksonJmsMessageConverter);
	    template.setPubSubDomain(true);
	    template.setDeliveryMode(DeliveryMode.PERSISTENT);

	    return template;
	}
}
