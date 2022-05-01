package de.coerdevelopment.standalone.repository;

import de.coerdevelopment.standalone.util.DebugMessage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQL {

    private static SQL instance;

    public static SQL getSQL() {
        return instance;
    }

    public static SQL getSQL(String host, String username, String password, String database, String port) {
        if (instance == null) {
            instance = new SQL(host, username, password, database, port);
        }
        return instance;
    }

    public static SQL newSQL(String host, String username, String password, String database, String port) {
        instance = new SQL(host, username, password, database, port);
        return instance;
    }

    /**
     * For SQLite
     */
    @Deprecated
    public static SQL newSQL(String database_path) {
        instance = new SQL(database_path);
        return instance;
    }

    private String host;
    private String username;
    private String password;
    private String database;
    private String port;
    private String database_path;

    private Connection connection;

    private SQL(String host, String username, String password, String database) {
        this(host, username, password, database, "1521");
    }

    private SQL(String host, String username, String password, String database, String port) {
        this.host = host;
        this.username = username;
        this.password = password;
        this.database = database;
        this.port = port;
    }
    private SQL(String database_path) {
        this.database_path = database_path;
    }

    public void connect() throws SQLException {
        if (isConnected()) {
            return;
        }
        connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database, username, password);
    }

    public void disconnect() throws SQLException {
        if (isConnected()) {
            connection.close();
            connection = null;
        }
    }

    /**
     * Returns true if the initialization was successful, otherwise false
     */
    public boolean initSQL() {
        try {
            connect();
            return isConnected();
        } catch (SQLException e) {
            DebugMessage.sendErrorMessage("An Error occurred initializing sql-connection!");
            e.printStackTrace();
        }
        return false;
    }

    public boolean isConnected() throws SQLException {
        if (connection != null) {
            return !connection.isClosed();
        }
        return false;
    }

    public synchronized Connection getConnection() {
        return connection;
    }

}
