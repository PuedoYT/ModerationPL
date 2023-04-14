package me.puedo.moderationpl.API;

import me.puedo.moderationpl.Main;
import me.puedo.moderationpl.MySQL.MySQL;
import org.bukkit.configuration.file.FileConfiguration;

import java.sql.Connection;
import java.sql.SQLException;

public class SQLConnector {

    public static FileConfiguration config;

    private String host = config.getString("sql.host");
    private int port = config.getInt("sql.port");
    private String database = config.getString("sql.database");
    private String username = config.getString("sql.username");
    private String password = config.getString("sql.password");

    private MySQL sql;
    private Main main;

    public SQLConnector(String host, int port, String database, String username, String password)
    {
        this.host = host;
        this.port = port;
        this.database = database;
        this.username = username;
        this.password = password;
    }

    public void connect() throws SQLException {
        sql.connect(host, port, database, username, password);
    }

    public boolean isConnected() { return sql.isConnected(); }

    public Connection getConnection() { return sql.getConnection(); }

    public void disconnect() { sql.disconnect(); }

    public void setupMySQLCredentials(String host, int port, String database, String username, String password)
    {
        config.set("sql.host", host);
        config.set("sql.port", port);
        config.set("sql.database", database);
        config.set("sql.username", username);
        config.set("sql.password", password);
        main.saveConfig();
    }

}
