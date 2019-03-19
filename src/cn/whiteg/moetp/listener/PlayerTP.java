package cn.whiteg.moetp.listener;

import cn.whiteg.moetp.Event.PlayerFarTpEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

public class PlayerTP implements Listener {
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void PlayerOnTP(PlayerTeleportEvent event) {
        Location from = event.getFrom();
        Location to = event.getTo();
        if (from.getWorld() == to.getWorld()){
            Double v = event.getFrom().distance(event.getTo());
            if (v < 16) return;
        }
        PlayerFarTpEvent tpEvent = new PlayerFarTpEvent(event.getPlayer(),from,to,event.getCause());
        Bukkit.getPluginManager().callEvent(tpEvent);
        event.setTo(tpEvent.getTo());
        event.setCancelled(tpEvent.isCancelled());
    }
    public void unreg(){
        PlayerFarTpEvent.getHandlerList().unregister(this);
    }
}
