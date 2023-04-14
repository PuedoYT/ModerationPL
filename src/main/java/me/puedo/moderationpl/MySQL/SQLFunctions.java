package me.puedo.moderationpl.MySQL;

import javafx.scene.text.Text;
import me.puedo.moderationpl.Main;
import me.puedo.moderationpl.MySQL.Managers.BanManager;
import me.puedo.moderationpl.Utils.PunishmentBroadcast;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class SQLFunctions {

    FileConfiguration config = Main.getPlugin(Main.class).getConfig();
    private MySQL sql = Main.sql;
    private PunishmentBroadcast bc = new PunishmentBroadcast();

    private Player player;
    private Player staff;
    private String reason;
    private Timestamp issuedon;
    private Boolean silent;
    private BanManager bm;
    public SQLFunctions(Player player, Player staff, String reason, Timestamp issuedon, Boolean silent){
        this.player = player;
        this.staff = staff;
        this.reason = reason;
        this.issuedon = issuedon;
        this.silent = silent;
    }

    public SQLFunctions() {}

    public void createDefaultTables() throws SQLException {
        PreparedStatement ps;
        PreparedStatement ps2;
        PreparedStatement ps3;
        ps = sql.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS bans (id INT(10) AUTO_INCREMENT, staff VARCHAR(36), player VARCHAR(36), reason VARCHAR(255), issuedon TIMESTAMP, expireson TIMESTAMP NOT NULL, silent BOOLEAN, active BOOLEAN, PRIMARY KEY (id))");
        ps2 = sql.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS mutes (id INT(10) AUTO_INCREMENT, staff VARCHAR(36), player VARCHAR(36), reason VARCHAR(255), issuedon TIMESTAMP, expireson TIMESTAMP NOT NULL, silent BOOLEAN, active BOOLEAN, PRIMARY KEY (id))");
        ps3 = sql.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS warns (id INT(10) AUTO_INCREMENT, staff VARCHAR(36), player VARCHAR(36), reason VARCHAR(255), issuedon TIMESTAMP, silent BOOLEAN, active BOOLEAN, PRIMARY KEY (id))");
        ps.executeUpdate();
        ps2.executeUpdate();
        ps3.executeUpdate();
    }

    public void createBan(
            Timestamp expireson
    ) throws SQLException {
        PreparedStatement ps;
        ps = sql.getConnection().prepareStatement("INSERT INTO mutes (staff, player, reason, issuedon, expireson, silent, active) VALUES (?, ?, ?, ?, ?, ?, ?)");
        ps.setString(1, staff.getUniqueId().toString());
        ps.setString(2, player.getUniqueId().toString());
        ps.setString(3, reason);
        ps.setTimestamp(4, issuedon);
        ps.setTimestamp(5, expireson);
        ps.setBoolean(6, silent);
        ps.setBoolean(7, true);
        ps.executeUpdate();

        bm = new BanManager(player);
        bc.broadcast(config.getString("punishment-broadcasts.ban")
                .replace("<silent>", bm.isSilent())
                .replace("<staff>", staff.getDisplayName())
                .replace("<player>", player.getDisplayName()), "staff.broadcasts", silent);
    }

    public void createMute(
            Timestamp expireson
    ) throws SQLException {
        PreparedStatement ps;
        ps = sql.getConnection().prepareStatement("INSERT INTO mutes (staff, player, reason, issuedon, expireson, silent, active) VALUES (?, ?, ?, ?, ?, ?, ?)");
        ps.setString(1, staff.getUniqueId().toString());
        ps.setString(2, player.getUniqueId().toString());
        ps.setString(3, reason);
        ps.setTimestamp(4, issuedon);
        ps.setTimestamp(5, expireson);
        ps.setBoolean(6, silent);
        ps.setBoolean(7, true);
        ps.executeUpdate();
    }

    public void createWarn(
    ) throws SQLException {
        PreparedStatement ps;
        ps = sql.getConnection().prepareStatement("INSERT INTO warns (staff, player, reason, issuedon, silent, active) VALUES (?, ?, ?, ?, ?, ?)");
        ps.setString(1, staff.getUniqueId().toString());
        ps.setString(2, player.getUniqueId().toString());
        ps.setString(3, reason);
        ps.setTimestamp(4, issuedon);
        ps.setBoolean(5, silent);
        ps.setBoolean(6, true);
        ps.executeUpdate();
    }


}

