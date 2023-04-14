package me.puedo.moderationpl.Commands;

import me.puedo.moderationpl.Main;
import me.puedo.moderationpl.MySQL.Managers.BanManager;
import me.puedo.moderationpl.MySQL.SQLFunctions;
import me.puedo.moderationpl.Utils.GetTimeUnit;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
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

public class UnbanCommand implements CommandExecutor {

    FileConfiguration config = Main.getPlugin(Main.class).getConfig();
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        assert args[0].length() >= 16;
        Player staff = Bukkit.getPlayer(sender.getName());
        OfflinePlayer player = Bukkit.getOfflinePlayer(args[0]);

        String reason = "";
        for (int i = 1; i < args.length; i++) {
            reason += args[i] + " ";
        }
        for (int i = 1; i < args.length; i++) {
            if(args[i].equalsIgnoreCase("-s")){}
        }

        BanManager bm = new BanManager(player); //Creating a new instance of BanManager

        if(player.hasPlayedBefore())
        {
            /*                          */
            try {
                /*                          */
                if(bm.isBanned(player)) {
                    bm.unban();
                }
                /*                  */
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            /*                          */
            } else { sender.sendMessage(ChatColor.RED + "(!) Player has never joined before."); }
        return false;
    }
}
