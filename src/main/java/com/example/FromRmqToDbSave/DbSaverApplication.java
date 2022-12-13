package com.example.FromRmqToDbSave;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import static com.example.FromRmqToDbSave.Rmq.dbCon;
import static com.example.FromRmqToDbSave.Rmq.rmqChan;

@SpringBootApplication
public class DbSaverApplication {
	public static void main(String[] args) throws Exception {
		SpringApplication.run(DbSaverApplication.class, args);
		dbCon=Db.getConnection();
		rmqChan= Rmq.getRmqConnection();
		Rmq.createQueue(rmqChan);
		//with autoReconnect to Rmq and manualReconnect to Db
		 Rmq.receiveFromRmqAndSaveToDb();
	}
}
