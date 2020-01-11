package cn.whiteg.moetp.listener;

import cn.whiteg.moetp.utils.WarpManager;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

public class PlayerReSpawnListener implements Listener {
    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void playerReSpawn(PlayerRespawnEvent event) {
        final Location loc = WarpManager.getSpawn();
        event.setRespawnLocation(loc);
    }
}
