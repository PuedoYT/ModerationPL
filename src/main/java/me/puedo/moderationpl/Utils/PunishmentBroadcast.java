package me.puedo.moderationpl.Utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;

public class PunishmentBroadcast {

    public static void broadcast(String bc_msg, String perm, Boolean silent){
        if(silent) {
            for(Player p : Bukkit.getServer().getOnlinePlayers()){
                if(p.hasPermission(perm)) {
                    p.sendMessage(bc_msg);
                }
            }
        } else { Bukkit.broadcastMessage(bc_msg); }
    }
}
