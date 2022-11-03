package com.example.DbSaver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DbSaverApplication {

	public static void main(String[] args) throws Exception {
		SpringApplication.run(DbSaverApplication.class, args);
		Recv recv=new Recv();
		recv.receiveFromRmq();


	}

}
