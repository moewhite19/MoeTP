package cn.whiteg.moetp.listener;

import cn.whiteg.moetp.Event.PlayerFarTpEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

public class PlayerTP implements Listener {

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onPlayerTp(PlayerTeleportEvent event) {
//        if (!con(event.getCause())) return;
        Location from = event.getFrom();
        Location to = event.getTo();
        final double dis;
        if (from.getWorld() == to.getWorld()){
            dis = from.distance(to);
//            if (from.getX() > to.getX()){
//                v += from.getX() - to.getX();
//            } else {
//                v += to.getX() - from.getX();
//            }
//            if (from.getZ() > to.getZ()){
//                v += from.getZ() - to.getZ();
//            } else {
//                v += to.getZ() - from.getZ();
//            }
            if (dis < 1) return;
        } else {
            dis = Double.MAX_VALUE;
        }
        PlayerFarTpEvent tpEvent = new PlayerFarTpEvent(event,dis);
        Bukkit.getPluginManager().callEvent(tpEvent);
    }

    public void unreg() {
        PlayerTeleportEvent.getHandlerList().unregister(this);
    }
}
