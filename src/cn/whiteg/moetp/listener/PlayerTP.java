package cn.whiteg.moetp.listener;

import cn.whiteg.moetp.Event.PlayerFarTpEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

public class PlayerTP implements Listener {
    final private TeleportCause[] at = {TeleportCause.UNKNOWN,TeleportCause.COMMAND,TeleportCause.PLUGIN,TeleportCause.SPECTATE};

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void PlayerOnTP(PlayerTeleportEvent event) {
        if (!con(event.getCause())) return;
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

    private boolean con(TeleportCause cause) {
        for (int i = 0; i < at.length; i++) {
            if (at[i].equals(cause)) return true;
        }
        return false;
    }

    public void unreg() {
        PlayerTeleportEvent.getHandlerList().unregister(this);
    }
}
