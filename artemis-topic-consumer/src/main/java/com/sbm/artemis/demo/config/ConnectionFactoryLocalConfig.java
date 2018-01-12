package com.sbm.artemis.demo.config;

import org.apache.activemq.artemis.api.jms.ActiveMQJMSClient;
import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;
import org.apache.activemq.artemis.jms.client.ActiveMQTopicConnectionFactory;
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
@Profile("local")
public class ConnectionFactoryLocalConfig {

  @Value("${spring.artemis.user:guest}")
  String user;

  @Value("${spring.artemis.password:guest}")
  String password;

  @Value("${jms.topic.client_id:${random.value}}")
  String clientID;

  @Value("${jms.topic.subscription_durable:true}")
  boolean durable;

  @Value("${spring.artemis.broker-url:vm://embedded?broker.persistent=false,useShutdownHook=false}")
  String brokerUrl;


  /*
   * Initial ConnectionFactory
   */
  @Bean("localConnectionFactory")
  public ConnectionFactory connectionFactory() {
    ActiveMQConnectionFactory connectionFactory = new ActiveMQTopicConnectionFactory();
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
  public JmsListenerContainerFactory<?> pubSubFactory(@Qualifier("localConnectionFactory") ConnectionFactory connectionFactory,
                                                      @Qualifier("jacksonJmsMessageConverter") MessageConverter jacksonJmsMessageConverter,
                                                      DefaultJmsListenerContainerFactoryConfigurer configurer) {
    DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
    factory.setPubSubDomain(true);
    /**
     * Clien Id needed for Durable Subscription
     */
    factory.setClientId(clientID);
    factory.setSubscriptionDurable(durable);

    factory.setMessageConverter(jacksonJmsMessageConverter);

    configurer.configure(factory, connectionFactory);
    return factory;
  }

}
