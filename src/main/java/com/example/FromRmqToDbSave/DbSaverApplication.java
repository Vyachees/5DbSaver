package com.example.FromRmqToDbSave;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.sql.Connection;
import com.rabbitmq.client.Channel;
import org.springframework.context.ApplicationContext;


@SpringBootApplication
@Slf4j
public class DbSaverApplication {
	private static Connection dbCon=null;
	private static Channel rmqChan=null;

//https://www.concretepage.com/spring-boot/spring-boot-configurationproperties
	public static void main(String[] args) throws Exception {
		final ApplicationContext ctx = SpringApplication.run(DbSaverApplication.class, args);
		final ConfigProperties conf = ctx.getBean(ConfigProperties.class);
		log.info(conf.getDbUrl());

		//Connect to DB
		if(dbCon==null) {
			dbCon =	Db.getConnection(conf.getDbUrl(), conf.getDbUsername(), conf.getDbPassword());
		}
		//Connect to RMQ
		if(rmqChan==null){
			rmqChan= Rmq.GetRmqConnection(conf.getRmqHost(),conf.getRmqPort());
		}
		//Read from RMQ

		 Rmq.receiveFromRmqAndSaveToDb("queue-for-psg",rmqChan,dbCon);//(rmq_queue,rmqChan);

	}


}
