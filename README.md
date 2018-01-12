# Spring Boot Artemis Clustered Queue
  A sample project to demonstrate the asynchronous communication between two spring boot apps producer and consumer through Apache ActiveMQ Artemis 2.4.0 using `topic` (publish-subscribe) in a cluster mode.

# Introduction
- Apache ActiveMQ Artemis is a combined feature-set of ActiveMQ/HornetQ/Apollo. It provides a non blocking architecture for an outstanding performance.
- The project is configured to run on `local mode` (embedded server) and `clustered mode`  (different nodes) .

# Technologies
- Java 8
- Maven 3
- Spring Boot: 1.5.9.RELEASE
- Apache Artemis 2.4.0

# [ Optional ]  Run the project in `Local` Mode

  ```sh
${project-home} $ mvn clean package
${topic-producer-home}  $ mvn spring-boot:run -Dspring.profiles.active=local
${topic-cosnsumer-home} $ mvn spring-boot:run -Dspring.profiles.active=local
```

# Run the project in `Cluster` Mode


**Create the Cluster Brokers**

```sh
$ ${apache-artemis-home}\bin\artemis create --force ${apache-artemis-broker-dest}\broker1
$ ${apache-artemis-home}\bin\artemis create --force ${apache-artemis-broker-dest}\broker2
$ ${apache-artemis-home}\bin\artemis create --force ${apache-artemis-broker-dest}\broker3
```

**Configure Cluster Brokers**
  - Go to `${apache-artemis-broker-dest}`\broker`1`\etc and replace content with `${apache-artemis-home}\etc1\`
  - Go to `${apache-artemis-broker-dest}`\broker`2`\etc and replace content with `${apache-artemis-home}\etc2\`
  - Go to `${apache-artemis-broker-dest}`\broker`3`\etc and replace content with `${apache-artemis-home}\etc3\`
  
**Run the Nodes in the Cluster**
```sh
$ ${apache-artemis-broker-dest}\broker1\bin\artemis run
$ ${apache-artemis-broker-dest}\broker2\bin\artemis run
$ ${apache-artemis-broker-dest}\broker3\bin\artemis run
```

**Run the project in Cluster Mode**
  
  ```sh
${project-home} $ mvn clean package
${topic-producer-home}  $ mvn spring-boot:run -Dspring.profiles.active=clustered-queue
${topic-cosnsumer-home} $ mvn spring-boot:run -Dspring.profiles.active=node1
${topic-cosnsumer-home} $ mvn spring-boot:run -Dspring.profiles.active=node2
${topic-cosnsumer-home} $ mvn spring-boot:run -Dspring.profiles.active=node3
```


# References

- https://activemq.apache.org/artemis/docs/latest/clusters.html
- https://activemq.apache.org/artemis/docs/latest/examples.html
- https://github.com/apache/activemq-artemis/tree/master/examples/features/clustered
- http://javasampleapproach.com/java-integration/activemq-work-spring-jms-activemq-topic-publisher-subcribers-pattern-using-springboot
 