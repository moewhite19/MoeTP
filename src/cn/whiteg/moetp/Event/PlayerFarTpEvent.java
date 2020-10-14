package cn.whiteg.moetp.Event;

import com.sun.istack.internal.NotNull;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.player.PlayerTeleportEvent;

public class PlayerFarTpEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();

    @NotNull
    private final PlayerTeleportEvent playerTeleportEvent;
    private double dis;

    public PlayerFarTpEvent(@NotNull PlayerTeleportEvent event) {
        playerTeleportEvent = event;
        updataDis();
    }

    public PlayerFarTpEvent(@NotNull PlayerTeleportEvent event,double dis) {
        playerTeleportEvent = event;
        this.dis = dis;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    @Override
    public boolean isCancelled() {
        return playerTeleportEvent.isCancelled();
    }

    @Override
    public void setCancelled(boolean b) {
        playerTeleportEvent.setCancelled(b);
    }

    public Location getFrom() {
        return playerTeleportEvent.getFrom();
    }

    public Location getTo() {
        return playerTeleportEvent.getTo();
    }

    public void setTo(Location location) {
        playerTeleportEvent.setTo(location);
        updataDis();
    }

    public PlayerTeleportEvent.TeleportCause getCause() {
        return playerTeleportEvent.getCause();
    }

    @NotNull
    public PlayerTeleportEvent getPlayerTeleportEvent() {
        return playerTeleportEvent;
    }

    public double getDistance() {
        return dis;
    }

    public Player getPlayer() {
        return playerTeleportEvent.getPlayer();
    }

    public double updataDis() {
        if (getFrom().getWorld() == getTo().getWorld()){
            dis = playerTeleportEvent.getFrom().distance(getTo());
        } else {
            dis = Double.MAX_VALUE;
        }
        return dis;
    }

    public static class listener implements Listener {
        @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
        public void onPlayerTp(PlayerTeleportEvent event) {
//        if (!con(event.getCause())) return;
            Location from = event.getFrom();
            Location to = event.getTo();
            final double dis;
            if (from.getWorld() == to.getWorld()){
                dis = from.distance(to);
                if (dis < 1) return;
            } else {
                dis = Double.MAX_VALUE;
            }
            PlayerFarTpEvent tpEvent = new PlayerFarTpEvent(event,dis);
            Bukkit.getPluginManager().callEvent(tpEvent);
        }
    }
}
