package com.example.FromRmqToDbSave;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.sql.Connection;
import com.rabbitmq.client.Channel;
import org.springframework.context.ApplicationContext;


/*
* 1. Есть класс Db. В котором есть методы соединения с базой данных, записи сообщения, чтения сообщения
* 2. Есть класс Rmq. В котором есть матоды соединения с RMQ, записи сообщения, чтения сообщения.
* 3. Есть класс Sockets. В котором есть методы создания сервера, клиента, подключения клиента к серверу, отправка
* сообщения от клиента к серверу, отправка сообщения от сервера к клиенту.
* */

/*
* To add different files you can use the spring.config.location properties which takes a comma separated list of property files or file location (directories).
-Dspring.config.location=your/config/dir/
* */
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

		//Соединяемся с БД
		if(dbCon==null) {
			dbCon =	Db.getConnection(conf.getDbUrl(), conf.getDbUsername(), conf.getDbPassword());
		}
		//Соединяемся с RMQ
		if(rmqChan==null){
			rmqChan= Rmq.GetRmqConnection(conf.getRmqHost(),conf.getRmqPort());
		}
		//Вычитываем сообщение из RMQ


		//TODO добавить логирование во внешний файл
		 Rmq.receiveFromRmqAndSaveToDb("queue-for-psg",rmqChan,dbCon);//(rmq_queue,rmqChan);

		//log.info("232 "+message);

		//	Recv recv=new Recv();
	//	recv.receiveFromRmq();

	}


}
