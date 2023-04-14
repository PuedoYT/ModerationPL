package me.puedo.moderationpl.MySQL;

import me.puedo.moderationpl.Main;
import org.bukkit.configuration.file.FileConfiguration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQL {

    FileConfiguration config = Main.getPlugin(Main.class).getConfig();

    private Connection connection;


    public boolean isConnected() {
        return connection != null;
    }

    public void connect(String host, int port, String database, String username, String password) throws SQLException {
        connection = DriverManager.getConnection("jdbc:mysql://" +
                        host + ":" + port + "/" + database + "?useSSL=false",
                username, password);
    }

    public void disconnect() {
        if (isConnected()) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public Connection getConnection() {
        return connection;
    }
}
