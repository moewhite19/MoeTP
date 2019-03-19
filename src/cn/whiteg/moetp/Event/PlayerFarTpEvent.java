package cn.whiteg.moetp.Event;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public class PlayerFarTpEvent extends PlayerEvent implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private boolean cancel = false;
    private final Location from;
    private Location to;
    private PlayerTeleportEvent.TeleportCause cause;

    public PlayerFarTpEvent(Player who,Location from,Location to,PlayerTeleportEvent.TeleportCause cause) {
        super(who);
        this.from = from;
        this.to = to;
        this.cause = cause;
    }

    public PlayerFarTpEvent(Player who,Location from,Location to) {
        super(who);
        this.from = from;
        this.to = to;
        cause = PlayerTeleportEvent.TeleportCause.UNKNOWN;
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
        return cancel;
    }

    @Override
    public void setCancelled(boolean b) {
        cancel = b;
    }

    public Location getFrom() {
        return from;
    }

    public Location getTo() {
        return to;
    }

    public void setTo(Location location) {
        to = location;
    }

    public PlayerTeleportEvent.TeleportCause getCause() {
        return cause;
    }
}
