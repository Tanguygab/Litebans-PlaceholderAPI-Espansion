package io.github.tanguygab.litebansexpansion;

import litebans.api.Entry;
import litebans.api.Events;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.clip.placeholderapi.expansion.Taskable;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;

import java.util.*;

public final class LiteBansExpansion extends PlaceholderExpansion implements Taskable {

    public static Map<UUID,PlayerData> playerdatas = new HashMap<>();
    public Events.Listener event;

    @Override
    public String getIdentifier() {
        return "litebans";
    }

    @Override
    public String getAuthor() {
        return "Tanguygab";
    }

    @Override
    public String getVersion() {
        return "1.0";
    }

    @Override
    public String getRequiredPlugin() {
        return "LiteBans";
    }

    @Override
    public List<String> getPlaceholders() {
        List<String> list = new ArrayList<>(Arrays.asList(
                "is_banned",
                "is_tempbanned",
                "is_muted",
                "is_tempmuted",
                "is_warned",

                "count_bans",
                "count_tempbans",
                "count_mutes",
                "count_tempmutes",
                "count_kicks",
                "count_warns",

                "last_ban_reason",
                "last_tempban_reason",
                "last_mute_reason",
                "last_tempmute_reason",
                "last_kick_reason",
                "last_warn_reason",

                "last_ban_date",
                "last_tempban_date",
                "last_mute_date",
                "last_tempmute_date",
                "last_kick_date",
                "last_warn_date",

                "expiry_tempban",
                "expiry_tempmute",

                "ip"));
        for (String pl : list)
            list.set(list.indexOf(pl),"%litebans_"+pl+"%");

        return list;
    }

    @Override
    public String onRequest(OfflinePlayer player, String params) {
        UUID uuid = player.getUniqueId();
        if (!playerdatas.containsKey(uuid)) {
            playerdatas.put(uuid, new PlayerData(uuid));
            return "Loading...";
        }
        PlayerData p = playerdatas.get(uuid);

        if (params.startsWith("is_")) {
            params = params.replace("is_","");
            switch (params) {
                case "banned":
                    return p.isBanned()+"";
                case "tempbanned":
                    return p.isTempBanned()+"";
                case "muted":
                    return p.isMuted()+"";
                case "tempmuted":
                    return p.isTempMuted()+"";
                case "warned":
                    return p.isWarned()+"";
            }
        }

        if (params.startsWith("count_")) {
            params = params.replace("count_", "");
            switch (params) {
                case "bans":
                    return p.countBans()+"";
                case "tempbans":
                    return p.countTempBans()+"";
                case "mutes":
                    return p.countMutes()+"";
                case "tempmutes":
                    return p.countTempMutes()+"";
                case "kicks":
                    return p.countKicks()+"";
                case "warns":
                    return p.countWarns()+"";
            }
        }



        return null;
    }

    @Override
    public void start() {
        for (Player p : Bukkit.getServer().getOnlinePlayers())
            playerdatas.put(p.getUniqueId(),new PlayerData(p.getUniqueId()));
        PluginManager pm = Bukkit.getServer().getPluginManager();
        pm.registerEvents(new JoinListener(),pm.getPlugin("PlaceholderAPI"));

        event = new Events.Listener() {
            @Override
            public void entryAdded(Entry entry) {
                Utils.updateEntry(entry);
            }

            @Override
            public void entryRemoved(Entry entry) {
                Utils.updateEntry(entry);
            }
        };

        Events.get().register(event);
    }

    @Override
    public void stop() {
        Events.get().unregister(event);
    }
}
