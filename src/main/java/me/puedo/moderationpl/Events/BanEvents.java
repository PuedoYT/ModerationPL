package me.puedo.moderationpl.Events;

import me.puedo.moderationpl.Main;
import me.puedo.moderationpl.MySQL.Managers.BanManager;
import me.puedo.moderationpl.MySQL.SQLFunctions;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BanEvents implements Listener {

    FileConfiguration config = Main.getPlugin(Main.class).getConfig();
    @EventHandler
    public void onLogin(PlayerLoginEvent event) throws SQLException {
        Player player = event.getPlayer();
        BanManager bm = new BanManager(player);
        System.out.println("IS PLAYER BANNED: " + bm.isBanned(player));

        Date date = new Date(bm.getBanDurationLeft(player));
        DateFormat f = new SimpleDateFormat("dd:hh:mm");

        bm.checkDuration();
        if(bm.isBanned(player))
        {
            event.setResult(PlayerLoginEvent.Result.KICK_BANNED);
            List<String> list = new ArrayList<>();
            for(int i = 0; i < config.getList("kick-messages.ban").toArray().length; i++){
                list.add(config.getList("kick-messages.ban").toArray()[i].toString()
                        .replace("<time>", f.format(date))
                        .replace("<reason>", bm.getBanReason())
                );
            }
            event.setKickMessage(String.join("\n", list));
        }
    }
}
