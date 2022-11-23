package com.example.FromRmqToDbSave;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import java.sql.*;

@Slf4j
@Component
public class Db {
    private static Connection con;
    public static Connection getConnection(String url, String username, String password) {

        try {
            log.info("Connection to Db start");
            con = DriverManager.getConnection(url, username, password);
            log.info("Connection to Db successful");
        } catch (SQLException ex) {
            System.out.println("Failed to connect to Db");
        }
        return con;
    }


    private static final String INSERT_DATA = "INSERT INTO public.\"MyTable1\"" +
            "  (msg_num, date_time, id_truck, lat, lon) VALUES " +
            " (?, ?, ?, ?, ?);";

    public static void insertRecord
            //(Connection connection,Integer msg_num,String date_time,String id_truck,Double lat, Double lon)
    (Connection connection,String[] msg)
    throws SQLException {
        //Parse part
        int msg_num= Integer.parseInt(msg[0]);

        String date_time=msg[1]+" "+msg[2];
        String id_truck=msg[3];
        double lat= Double.parseDouble(msg[4].replace(",","."));
        double lon= Double.parseDouble(msg[5].replace(",","."));

        // Step 1: Establishing a Connection

        try (PreparedStatement preparedStatement = connection.prepareStatement(INSERT_DATA)) {

            preparedStatement.setInt(1, msg_num);
            preparedStatement.setTimestamp(2, Timestamp.valueOf(date_time));
            preparedStatement.setString(3, id_truck);
            preparedStatement.setDouble(4, lat);
            preparedStatement.setDouble(5, lon);
            log.info(preparedStatement.toString());
            // Step 3: Execute the query or update query
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            printSQLException(e);
        }

    }

    public static void printSQLException(SQLException ex) {
        for (Throwable e: ex) {
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
