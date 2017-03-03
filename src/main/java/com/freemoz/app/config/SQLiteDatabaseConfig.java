package com.freemoz.app.config;


import com.freemoz.app.service.Singleton;
import com.freemoz.app.util.Helpers;
import com.freemoz.app.util.Properties;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SQLiteDatabaseConfig implements IDatabaseConfig {

    private final Helpers helpers;
    private Connection connection = null;

    public SQLiteDatabaseConfig() {
        this.helpers = Singleton.getHelpers();
    }

    public synchronized Connection getConnection() throws SQLException {
        PreparedStatement preparedStatement = null;

        try {
            if (connection == null || connection.isClosed()) {
                String sqliteFile = (String) Properties.getProperties().getOrDefault(Values.SQLITE_FILE, Values.DEFAULT_SQLITE_FILE);

                Class.forName("org.sqlite.JDBC");
                connection = DriverManager.getConnection("jdbc:sqlite:" + sqliteFile);

                preparedStatement = connection.prepareStatement("PRAGMA journal_mode=WAL;");
                preparedStatement.execute();
            }
        }
        catch (ClassNotFoundException ex) {
            // TODO add logging here
        }
        finally {
            this.helpers.closeQuietly(preparedStatement);
        }

        return connection;
    }
}
