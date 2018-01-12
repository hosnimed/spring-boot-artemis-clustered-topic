package com.sbm.artemis.demo.config;

import org.apache.activemq.artemis.api.core.DiscoveryGroupConfiguration;
import org.apache.activemq.artemis.api.core.UDPBroadcastEndpointFactory;
import org.apache.activemq.artemis.api.jms.ActiveMQJMSClient;
import org.apache.activemq.artemis.api.jms.JMSFactoryType;
import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jms.DefaultJmsListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;

import javax.jms.ConnectionFactory;

@Configuration
@EnableJms
@Profile("!local")
public class ConnectionFactoryClusteredConfig {

	@Value("${jms.topic.client_id:${random.value}}")
	String clientID;

	@Value("${jms.topic.subscription_name:${random.value}}")
	String subscriptionName;

	@Value("${jms.topic.subscription_durable:true}")
	boolean durable;
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

	@Bean("pubSubFactory")
	public JmsListenerContainerFactory<?> pubSubFactory(@Qualifier("udpConnectionFactory") ConnectionFactory connectionFactory,
														@Qualifier("jacksonJmsMessageConverter") MessageConverter jacksonJmsMessageConverter,
														DefaultJmsListenerContainerFactoryConfigurer configurer) {
		DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
		factory.setPubSubDomain(true);
		/**
		 * Clien Id needed for Durable Subscription
		 */
		factory.setClientId(clientID);
		factory.setSubscriptionDurable(durable);
		factory.setSubscriptionShared(true);
		factory.setMessageConverter(jacksonJmsMessageConverter);
		configurer.configure(factory, connectionFactory);
		return factory;
	}

	/*
	// We create a JMS listenerContainer which is a connection to  artemis server
	@Bean("listenerContainer")
	public MessageListenerContainer listenerContainer(@Qualifier("udpConnectionFactory") ConnectionFactory connectionFactory,
													  @Qualifier("jacksonJmsMessageConverter") MessageConverter jacksonJmsMessageConverter) {
		DefaultMessageListenerContainer defaultMessageListenerContainer =
				new DefaultMessageListenerContainer();
		defaultMessageListenerContainer.setDestinationName(destionationName);
		defaultMessageListenerContainer.setConnectionFactory(connectionFactory);
		defaultMessageListenerContainer.setMessageConverter(jacksonJmsMessageConverter);
		defaultMessageListenerContainer.setSessionAcknowledgeMode(1);
		defaultMessageListenerContainer.setSubscriptionDurable(true);
		defaultMessageListenerContainer.setClientId(clientID);
		defaultMessageListenerContainer.setDurableSubscriptionName(subscriptionName);
		return defaultMessageListenerContainer;
	}
	*/
}
