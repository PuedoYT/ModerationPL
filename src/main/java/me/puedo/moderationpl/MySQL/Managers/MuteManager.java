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

public class MuteManager {

    FileConfiguration config = Main.getPlugin(Main.class).getConfig();
    private MySQL sql = Main.sql;
    private Player player;
    private OfflinePlayer offplayer;
    public MuteManager(Player player){
        this.player = player;
    }

    public MuteManager(OfflinePlayer player){
        this.offplayer = player;
    }

    public boolean isMuted(Player player) throws SQLException {
        PreparedStatement ps;
        ps = sql.getConnection().prepareStatement("SELECT active FROM mutes WHERE player=? AND active=1;");
        ps.setString(1, player.getUniqueId().toString());
        ps.executeQuery();

        if(!(ps.getResultSet().getFetchSize() < 1)) return false;
        else if(ps.getResultSet().next())
        {
            return true;
        }
        return false;
    }

    public void unmute() throws SQLException {
        if(!isMuted(player)) return;
        PreparedStatement ps;
        ps = sql.getConnection().prepareStatement("UPDATE bans SET active=0 WHERE player=? AND active=1;");
        ps.setString(1, offplayer.getUniqueId().toString());
        ps.executeUpdate();
    }

    public void checkDuration() throws SQLException {
        if(!isMuted(player)) return;

        if(getMuteDurationLeft(player) == -1) return;

        if(getMuteExpireLong(player) < System.currentTimeMillis()){
            PreparedStatement ps;
            ps = sql.getConnection().prepareStatement("UPDATE mutes SET active=0 WHERE player=? AND active=1;");
            ps.setString(1, player.getUniqueId().toString());
            ps.executeUpdate();
        }
    }

    public long getMuteExpireLong(Player player) throws SQLException {

        PreparedStatement ps;
        ps = sql.getConnection().prepareStatement("SELECT expireson FROM mutes WHERE player = ? AND active=1;");
        ps.setString(1, this.player.getUniqueId().toString());
        ps.executeQuery();

        if(ps.getResultSet().next()) {

            long expires_on_milis = ps.getResultSet().getTimestamp("expireson").getTime();

            return expires_on_milis;

        }
        return 0L;
    }
    public long getMuteDurationLeft(Player player) throws SQLException {

        PreparedStatement ps;
        ps = sql.getConnection().prepareStatement("SELECT expireson FROM mutes WHERE player = ?");
        ps.setString(1, this.player.getUniqueId().toString());
        ps.executeQuery();

        if(ps.getResultSet().next()) {

            long expires_on_milis = ps.getResultSet().getTimestamp("expireson").getTime();
            long current_time = Timestamp.from(Instant.now()).getTime();
            long remaining_time = expires_on_milis - System.currentTimeMillis();

            return remaining_time;

        } else { return 0L; }
    }

    public long getMuteOriginalDuration(Player player) throws SQLException {

        PreparedStatement ps;
        ps = sql.getConnection().prepareStatement("SELECT expireson, issuedon FROM mutes WHERE player = ?");
        ps.setString(1, this.player.getUniqueId().toString());
        ps.executeQuery();

        if(ps.getResultSet().next()) {

            long expires_on_milis = ps.getResultSet().getTimestamp("expireson").getTime();
            long issued_on = ps.getResultSet().getTimestamp("issuedon").getTime();
            long remaining_time = expires_on_milis - issued_on;

            return remaining_time;

        } else { return 0L; }
    }

    public String getMuteReason() throws SQLException {
        PreparedStatement ps;
        ps = sql.getConnection().prepareStatement("SELECT reason FROM mutes WHERE player = ? AND active=1;");
        ps.setString(1, player.getUniqueId().toString());
        ps.executeQuery();
        if(ps.getResultSet().next()) {
            return ps.getResultSet().getString("reason");
        }
        return "Unknown reason";
    }

    public String isSilent() throws SQLException {
        PreparedStatement ps;
        ps = sql.getConnection().prepareStatement("SELECT silent FROM mutes WHERE player = ? AND active=1;");
        ps.setString(1, player.getUniqueId().toString());
        ps.executeQuery();
        if(ps.getResultSet().next()) {
            if (ps.getResultSet().getBoolean("silent")) return config.getString("silent-prefix");
            else return "";
        }
        return "§c[ERROR] ";
    }
}
