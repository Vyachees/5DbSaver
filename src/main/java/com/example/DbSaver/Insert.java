package com.example.DbSaver;

/**
 * @author Vyacheslav Kirillov
 * @create 2022.11.03 19:30
 **/


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.sql.*;



@Component
@Slf4j
public class Insert {
    private final String url = "jdbc:postgresql://localhost/postgres";
    private final String user = "MyUser";
    private final String password = "4444";



    private static final String INSERT_DATA = "INSERT INTO public.\"MyTable1\"" +
            "  (msg_num, date_time, id_truck, lat, lon) VALUES " +
            " (?, ?, ?, ?, ?);";


    public void insertRecord() throws SQLException {
        log.info(INSERT_DATA);

        // Step 1: Establishing a Connection
        try (Connection connection = DriverManager.getConnection(url, user, password);


             // Step 2:Create a statement using connection object
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_DATA)) {

            preparedStatement.setInt(1, 111);
            preparedStatement.setTimestamp(2, Timestamp.valueOf("2022-09-26 16:42:09"));
            preparedStatement.setString(3, "A111AA11");
            preparedStatement.setDouble(4, 55.56578);
            preparedStatement.setDouble(5, 38.044315);
            log.info(preparedStatement.toString());
            // Step 3: Execute the query or update query
            preparedStatement.executeUpdate();

        } catch (SQLException e) {

            // print SQL exception information
            printSQLException(e);
        }

        // Step 4: try-with-resource statement will auto close the connection.
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