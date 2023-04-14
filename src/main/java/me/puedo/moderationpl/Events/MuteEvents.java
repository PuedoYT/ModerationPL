package me.puedo.moderationpl.Events;

import me.puedo.moderationpl.Main;
import me.puedo.moderationpl.MySQL.Managers.BanManager;
import me.puedo.moderationpl.MySQL.Managers.MuteManager;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerLoginEvent;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MuteEvents implements Listener {

    FileConfiguration config = Main.getPlugin(Main.class).getConfig();
    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) throws SQLException {
        Player player = event.getPlayer();
        MuteManager mm = new MuteManager(player);

        mm.checkDuration();
        if(mm.isMuted(player)){
            event.setCancelled(true);

            Date date = new Date(mm.getMuteDurationLeft(player));
            DateFormat f = new SimpleDateFormat("dd:hh:mm");

            List<String> list = new ArrayList<>();
            for (int i = 0; i < config.getList("mute").toArray().length; i++) {
                list.add(config.getList("mute").toArray()[i].toString()
                        .replace("<time>", f.format(date))
                        .replace("<reason>", mm.getMuteReason()));

            }
            player.sendMessage(String.join("\n", list));
        }
    }
}
