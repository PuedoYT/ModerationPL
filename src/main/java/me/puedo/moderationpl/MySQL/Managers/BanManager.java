package me.puedo.moderationpl.MySQL.Managers;

import me.puedo.moderationpl.Main;
import me.puedo.moderationpl.MySQL.MySQL;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;

public class BanManager {

    FileConfiguration config = Main.getPlugin(Main.class).getConfig();
    private MySQL sql = Main.sql;
    private Player player;
    private OfflinePlayer offplayer;
    public BanManager(Player player){
        this.player = player;
    }

    public BanManager(OfflinePlayer player){
        this.offplayer = player;
    }

    public boolean isBanned(Player player) throws SQLException {
        PreparedStatement ps;
        ps = sql.getConnection().prepareStatement("SELECT active FROM bans WHERE player=? AND active=1;");
        ps.setString(1, player.getUniqueId().toString());
        ps.executeQuery();

        if(!(ps.getResultSet().getFetchSize() < 1)) return false;
        else if(ps.getResultSet().next())
        {
            return true;
        }
        return false;
    }

    public boolean isBanned(OfflinePlayer player) throws SQLException {
        PreparedStatement ps;
        ps = sql.getConnection().prepareStatement("SELECT active FROM bans WHERE player=? AND active=1;");
        ps.setString(1, Bukkit.getOfflinePlayer(player.getName()).getUniqueId().toString());
        ps.executeQuery();

        if(!(ps.getResultSet().getFetchSize() < 1)) return false;
        else if(ps.getResultSet().next())
        {
            return true;
        }
        return false;
    }

    public void unban() throws SQLException {
        if(!isBanned(offplayer)) return;
        PreparedStatement ps;
        ps = sql.getConnection().prepareStatement("UPDATE bans SET active=0 WHERE player=? AND active=1;");
        ps.setString(1, offplayer.getUniqueId().toString());
        ps.executeUpdate();
    }

    public void checkDuration() throws SQLException {
        if(!isBanned(player)) return;

        if(getBanDurationLeft(player) == -1) return;

        if(getBanExpireLong(player) < System.currentTimeMillis()){
            PreparedStatement ps;
            ps = sql.getConnection().prepareStatement("UPDATE bans SET active=0 WHERE player=? AND active=1;");
            ps.setString(1, player.getUniqueId().toString());
            ps.executeUpdate();
        }
    }

    public long getBanExpireLong(Player player) throws SQLException {

        PreparedStatement ps;
        ps = sql.getConnection().prepareStatement("SELECT expireson FROM bans WHERE player = ? AND active=1;");
        ps.setString(1, this.player.getUniqueId().toString());
        ps.executeQuery();

        if(ps.getResultSet().next()) {

            long expires_on_milis = ps.getResultSet().getTimestamp("expireson").getTime();

            return expires_on_milis;

        }
        return 0L;
    }
    public long getBanDurationLeft(Player player) throws SQLException {

        PreparedStatement ps;
        ps = sql.getConnection().prepareStatement("SELECT expireson FROM bans WHERE player = ?");
        ps.setString(1, this.player.getUniqueId().toString());
        ps.executeQuery();

        if(ps.getResultSet().next()) {

            long expires_on_milis = ps.getResultSet().getTimestamp("expireson").getTime();
            long current_time = Timestamp.from(Instant.now()).getTime();
            long remaining_time = expires_on_milis - System.currentTimeMillis();

            return remaining_time;

        } else { return 0L; }
    }

    public long getBanOriginalDuration(Player player) throws SQLException {

        PreparedStatement ps;
        ps = sql.getConnection().prepareStatement("SELECT expireson, issuedon FROM bans WHERE player = ?");
        ps.setString(1, this.player.getUniqueId().toString());
        ps.executeQuery();

        if(ps.getResultSet().next()) {

            long expires_on_milis = ps.getResultSet().getTimestamp("expireson").getTime();
            long issued_on = ps.getResultSet().getTimestamp("issuedon").getTime();
            long remaining_time = expires_on_milis - issued_on;

            return remaining_time;

        } else { return 0L; }
    }

    public String getBanReason() throws SQLException {
        PreparedStatement ps;
        ps = sql.getConnection().prepareStatement("SELECT reason FROM bans WHERE player = ? AND active=1;");
        ps.setString(1, player.getUniqueId().toString());
        ps.executeQuery();
        if(ps.getResultSet().next()) {
            return ps.getResultSet().getString("reason");
        }
        return "Unknown reason";
    }

    public String isSilent() throws SQLException {
        PreparedStatement ps;
        ps = sql.getConnection().prepareStatement("SELECT silent FROM bans WHERE player = ? AND active=1;");
        ps.setString(1, player.getUniqueId().toString());
        ps.executeQuery();
        if(ps.getResultSet().next()) {
            if (ps.getResultSet().getBoolean("silent")) return config.getString("silent-prefix");
            else return "";
        }
        return "§c[ERROR] ";
    }
}
