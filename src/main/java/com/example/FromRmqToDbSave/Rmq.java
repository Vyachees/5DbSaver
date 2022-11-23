package com.example.FromRmqToDbSave;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.concurrent.TimeoutException;

@Slf4j
@Component
public class Rmq {

    public static Channel GetRmqConnection(String host, Integer port) throws IOException {
        log.info("Connection to Rmq start");
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(host);
        factory.setPort(port);
        Connection connection = null;
        try {
            connection = factory.newConnection();
        } catch (IOException | TimeoutException e ) {
            log.error("GetRmqConnection IOException | TimeoutException" + e);
        }
        assert connection != null;
        log.info("Connection to Rmq successful");
        return connection.createChannel();
    }

    public static void receiveFromRmqAndSaveToDb(String QUEUE_NAME, Channel channel, java.sql.Connection connection) throws Exception {
        log.info("Checking queue existing");
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        channel.queueBind(QUEUE_NAME, "TestData3", "#");
        channel.basicQos(1);
        log.info("Receive starting");

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
            log.info("Received");

            //парсим в массив и разбираем на переменные для записи в БД
            String[] msgArr=message.split(" ");
            log.info("Parsed "+ Arrays.toString(msgArr));
            try {
                Db.insertRecord(connection,msgArr);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            finally {
                channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
            }
            log.info("Inserted");
            log.info("");


        };
          channel.basicConsume(QUEUE_NAME, false, deliverCallback, consumerTag -> { });

    }

}
