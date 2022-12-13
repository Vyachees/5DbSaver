package com.example.FromRmqToDbSave;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.sql.*;

@Slf4j
@Configuration
public class Db {


    private static HikariDataSource ds;
    //Hikari configuration beans
       @Bean
       @ConfigurationProperties(prefix = "spring.datasource.hikari")
       public HikariConfig hikariConfig() {
           return new HikariConfig();
       }
       @Bean
       public DataSource dataSource() {
           ds = new HikariDataSource(hikariConfig());
           return ds;
       }


       public static Connection getConnection() throws SQLException {
           return ds.getConnection();
       }

       private static final String INSERT_DATA = "INSERT INTO public.coords" +
               "  (msg_num, date_time, id_truck, lat, lon, trip_number) VALUES " +
               " (?, ?, ?, ?, ?, ?);";

       public static void insertRecord(Connection connection, String[] msg)
               throws SQLException, InterruptedException {
           //Parse part
           int msg_num = Integer.parseInt(msg[0]);

           String date_time = msg[1] + " " + msg[2];
           String id_truck = msg[3];
           double lat = Double.parseDouble(msg[4].replace(",", "."));
           double lon = Double.parseDouble(msg[5].replace(",", "."));
           int tripNumber = Integer.parseInt(msg[6]);

           // Step 1: Establishing a Connection
           try (PreparedStatement preparedStatement = connection.prepareStatement(INSERT_DATA)) {

               preparedStatement.setInt(1, msg_num);
               preparedStatement.setTimestamp(2, Timestamp.valueOf(date_time));
               preparedStatement.setString(3, id_truck);
               preparedStatement.setDouble(4, lat);
               preparedStatement.setDouble(5, lon);
               preparedStatement.setInt(6, tripNumber);
               log.info(preparedStatement.toString());
               // Step 3: Execute the query or update query
               preparedStatement.executeUpdate();
           } catch (SQLException e) {
               printSQLException(e);
               Thread.sleep(3000);
           }

       }

       //test method
       public static void simpleSelect(Connection connection) throws SQLException, InterruptedException {

           try {
               Statement stmt = connection.createStatement();
               ResultSet rs = stmt.executeQuery("Select 1 as adin");
               while (rs.next()) {
                   String adin = rs.getString("adin");
                   System.out.printf("" + adin);
                   System.out.println();
               }
           } catch (Exception e) {
               Thread.sleep(2000);
               //    Db.getConnection();
               log.info("exception " + e);
           }
       }

       public static void printSQLException(SQLException ex) {
           for (Throwable e : ex) {
               if (e instanceof SQLException) {
                   e.printStackTrace(System.err);
                   System.err.println("SQLState: " + ((SQLException) e).getSQLState());
                   System.err.println("Error Code: " + ((SQLException) e).getErrorCode());
                   System.err.println("Message: " + e.getMessage());
                   Throwable t = ex.getCause();
                   while (t != null) {
                       System.out.println("Cause: " + t);
                       t = t.getCause();
                   }
               }
           }
       }


   }
