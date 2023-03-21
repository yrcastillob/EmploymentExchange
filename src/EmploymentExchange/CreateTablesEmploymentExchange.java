package EmploymentExchange;

import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

public class CreateTablesEmploymentExchange {

    public static void main (String[] args) throws Exception {

        // Location of the file in the database
        String url = "jdbc:h2:file:./data/database/employmentExchange";

        // Connection with the database driver
        ConnectionSource connection = new JdbcConnectionSource(url);

        // Creation of the database and the table
        TableUtils.createTable(connection, CurriculumVitae.class);
        System.out.println("Table successfully created.");

        // Close the connection
        connection.close();
    }

}

