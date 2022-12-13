package com.example.FromRmqToDbSave;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import lombok.extern.slf4j.Slf4j;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.concurrent.TimeoutException;

@Slf4j
public class Rmq {
    private static final String queueName="queue-for-psg";

    public static Channel getRmqConnection() throws IOException {
        log.info("Connection to Rmq start");
        ConnectionFactory factory = new ConnectionFactory();
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

    public static void createQueue(Channel channel) throws IOException {
        log.info("Checking queue existing");
        channel.queueDeclare(queueName, false, false, false, null);
        channel.queueBind(queueName, "TestData3", "#");
        channel.basicQos(10);
    }

    static java.sql.Connection dbCon;
    static Channel rmqChan;
    public static void receiveFromRmqAndSaveToDb() throws Exception {
        log.info("Receive starting");
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
            log.info("Received");
            //Parse to array and get values for insert into DB
            String[] msgArr=message.split(" ");
            log.info("Parsed "+ Arrays.toString(msgArr));
            try {
                if(dbCon.isClosed()){
                    Thread.sleep(5000);
                    dbCon=Db.getConnection();
                }
                Db.insertRecord(dbCon,msgArr);
            } catch (SQLException | InterruptedException e) {
                throw new RuntimeException(e);
            }
            finally {
                rmqChan.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
            }
            log.info("Inserted");
            log.info("");
        };
        rmqChan.basicConsume(queueName, false, deliverCallback, consumerTag -> { });
    }
}
