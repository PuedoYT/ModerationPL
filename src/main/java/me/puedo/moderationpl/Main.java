package me.puedo.moderationpl;

import me.puedo.moderationpl.Commands.BanCommand;
import me.puedo.moderationpl.Commands.MuteCommand;
import me.puedo.moderationpl.Commands.UnbanCommand;
import me.puedo.moderationpl.Events.BanEvents;
import me.puedo.moderationpl.Events.MuteEvents;
import me.puedo.moderationpl.MySQL.MySQL;
import me.puedo.moderationpl.MySQL.SQLFunctions;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;

public final class Main extends JavaPlugin {

    public static FileConfiguration config;
    public static MySQL sql;
    @Override
    public void onEnable() {
        getConfig().options().copyDefaults();
        saveDefaultConfig();
        config = getConfig();

        regiserCommands();
        registerEvents();

        sql = new MySQL();
        try {
            sql.connect(config.getString("sql.host"), config.getInt("sql.port"), config.getString("sql.database"), config.getString("sql.username"), config.getString("sql.password"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        if(sql.isConnected()) {
            getLogger().info("Successfully connected to MySQL database. Creating tables...");
            SQLFunctions sqls = new SQLFunctions();
            try {
                sqls.createDefaultTables();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void onDisable() {
    }

    public void regiserCommands()
    {

        getCommand("ban").setExecutor(new BanCommand());
        getCommand("unban").setExecutor(new UnbanCommand());
        getCommand("mute").setExecutor(new MuteCommand());
    }

    public void registerEvents()
    {

        Bukkit.getPluginManager().registerEvents(new BanEvents(), this);
        Bukkit.getPluginManager().registerEvents(new MuteEvents(), this);
    }
}
