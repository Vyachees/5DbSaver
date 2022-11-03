package com.example.DbSaver;

/*
 * @author Vyacheslav Kirillov
 * @create 2022.10.27 22:47
 */
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;

import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.sql.SQLException;

@Slf4j
@Component
public class Recv {

    private final static String QUEUE_NAME = "queue-for-psg";

    @Bean
    //@EventListener(ApplicationReadyEvent.class)
    public void receiveFromRmq() throws Exception {
        log.info("start");
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setPort(5672);
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

       // channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
        Insert insert=new Insert();


        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
            System.out.println(" [x] Received '" + message + "'");
            try {
                insert.insertRecord();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }


        };
        channel.basicConsume(QUEUE_NAME, true, deliverCallback, consumerTag -> { });
    }
}