package io.github.tanguygab.litebansexpansion;

import litebans.api.Database;
import litebans.api.Entry;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Utils {

    public static BukkitScheduler sch = Bukkit.getServer().getScheduler();;
    public static Plugin plugin = Bukkit.getPluginManager().getPlugin("PlaceholderAPI");

    public static void checkIs(PlayerData p, DataType datatype, String typename, boolean temp) {
        Utils.sch.runTaskAsynchronously(plugin,() -> {
            String query = "SELECT active FROM "+typename+" WHERE uuid = \""+p.uuid+"\"  AND "+(temp ? "until <> -1" : "until = -1");
            try (PreparedStatement st = Database.get().prepareStatement(query)) {
                try (ResultSet rs = st.executeQuery()) {
                    if (rs.last()) {
                        p.data.put(datatype,rs.getBoolean(1));
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    public static void count(PlayerData p, DataType datatype, String typename, boolean temp) {
        sch.runTaskAsynchronously(plugin,() -> {
            String query = "SELECT COUNT(*) FROM "+typename+" WHERE uuid = \""+p.uuid+"\" AND "+ (temp ? "until <> -1" : "until = -1");
            try (PreparedStatement st = Database.get().prepareStatement(query)) {
                try (ResultSet rs = st.executeQuery()) {
                    if (rs.next()) {
                        p.data.put(datatype,rs.getInt(1));
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    public static void updateEntry(Entry entry) {
        List<UUID> list = new ArrayList<>();
        if (entry.getUuid() != null) list.add(UUID.fromString(entry.getUuid()));
        if (entry.getIp() != null && !entry.getIp().equals("#")) list.addAll(Database.get().getUsersByIP(entry.getIp()));

        for (UUID uuid : list) {
            PlayerData p = LiteBansExpansion.playerdatas.get(uuid);
            if (p == null) continue;

            String type = entry.getType();
            if (type.equals("ban") && entry.isPermanent()) {
                p.isBanned();
                p.countBans();
            }
            if (type.equals("ban") && !entry.isPermanent()) {
                p.isTempBanned();
                p.countTempBans();
            }
            if (type.equals("mute") && entry.isPermanent()) {
                p.isMuted();
                p.countMutes();
            }
            if (type.equals("mute") && !entry.isPermanent()) {
                p.isTempMuted();
                p.countTempMutes();
            }
        }
    }
}
