package com.example.FromRmqToDbSave;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@ConfigurationProperties(prefix = "cfg")
public class ConfigProperties {

    private String dbUrl;
    private String dbUsername;
    private String dbPassword;
    private String rmqHost;
    private Integer rmqPort;
    private String rmqUsername;
    private String rmqPassword;
    private String rmqQueue;

    public String getDbUsername() {
        return dbUsername;
    }

    public void setDbUsername(String dbUsername) {
        this.dbUsername = dbUsername;
    }

    public String getDbPassword() {
        return dbPassword;
    }

    public void setDbPassword(String dbPassword) {
        this.dbPassword = dbPassword;
    }

    public String getRmqHost() {
        return rmqHost;
    }

    public void setRmqHost(String rmqHost) {
        this.rmqHost = rmqHost;
    }

    public Integer getRmqPort() {
        return rmqPort;
    }

    public void setRmqPort(Integer rmqPort) {
        this.rmqPort = rmqPort;
    }

    public String getRmqUsername() {
        return rmqUsername;
    }

    public void setRmqUsername(String rmqUsername) {
        this.rmqUsername = rmqUsername;
    }

    public String getRmqPassword() {
        return rmqPassword;
    }

    public void setRmqPassword(String rmqPassword) {
        this.rmqPassword = rmqPassword;
    }

    public String getRmqQueue() {
        return rmqQueue;
    }

    public void setRmqQueue(String rmqQueue) {
        this.rmqQueue = rmqQueue;
    }



    public  String getDbUrl() {
        return dbUrl;
    }
    public void setDbUrl(String dbUrl) {
        this.dbUrl = dbUrl;
    }
}
