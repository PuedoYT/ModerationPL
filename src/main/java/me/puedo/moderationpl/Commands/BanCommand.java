package me.puedo.moderationpl.Commands;

import me.puedo.moderationpl.Main;
import me.puedo.moderationpl.MySQL.Managers.BanManager;
import me.puedo.moderationpl.MySQL.SQLFunctions;
import me.puedo.moderationpl.Utils.BanUnit;
import me.puedo.moderationpl.Utils.GetTimeUnit;
import me.puedo.moderationpl.Utils.PunishmentBroadcast;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BanCommand implements CommandExecutor {

    FileConfiguration config = Main.getPlugin(Main.class).getConfig();
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        assert args[0].length() >= 16;
        Player staff = Bukkit.getPlayer(sender.getName()), player = Bukkit.getPlayer(args[0]);
        Long time = Long.parseLong(args[1].replaceAll("[a-z_A-Z]", "")), seconds = new GetTimeUnit().getTimeUnitFromShortcut(args[1], time);

        String reason = "";
        Boolean silent = false;
        for (int i = 2; i < args.length; i++) {
            reason += args[i] + " ";
            if(args[i].equalsIgnoreCase("-s")){
                silent = true;
            }
        }

        BanManager bm = new BanManager(player); //Creating a new instance of BanManager
        SQLFunctions func = new SQLFunctions(player, staff, reason.replace("-s", ""), Timestamp.from(Instant.now()), silent);

        if(Bukkit.getPlayer(args[0]).hasPlayedBefore())
        {
            try {
                if(!bm.isBanned(player)) {
                    func.createBan(Timestamp.from(Instant.now().plusSeconds(seconds)));
                    sender.sendMessage(ChatColor.GREEN + "Successfully banned " + player + " for " + seconds);

                    Date date = new Date(bm.getBanDurationLeft(player));
                    DateFormat f = new SimpleDateFormat("dd:hh:mm");

                    List<String> list = new ArrayList<>();
                    for (int i = 0; i < config.getList("kick-messages.ban").toArray().length; i++) {
                        list.add(config.getList("kick-messages.ban").toArray()[i].toString()
                                .replace("<time>", f.format(date))
                                .replace("<reason>", bm.getBanReason()));

                    }
                    player.kickPlayer(String.join("\n", list));
                }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            } else { sender.sendMessage(ChatColor.RED + "(!) Player has never joined before."); }
        return false;
    }
}
