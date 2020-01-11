package cn.whiteg.moetp.Event;

import com.sun.istack.internal.NotNull;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
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

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
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
}
