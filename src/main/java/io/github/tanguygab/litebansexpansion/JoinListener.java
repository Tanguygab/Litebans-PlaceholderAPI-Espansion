package io.github.tanguygab.litebansexpansion;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        LiteBansExpansion.playerdatas.put(e.getPlayer().getUniqueId(),new PlayerData(e.getPlayer().getUniqueId()));
    }
}
