package cn.whiteg.moetp.listener;

import cn.whiteg.moetp.Event.PlayerFarTpEvent;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldBorder;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class SafeTpListener implements Listener {

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onPlayerTP(PlayerFarTpEvent event) {

        final Location loc = event.getTo();
        final Location bl = loc.clone();
        final World bw = bl.getWorld();
        final WorldBorder wb = bw.getWorldBorder();
        bl.add(wb.getCenter());
        if (Math.abs(bl.getX()) * 2 > wb.getSize() || Math.abs(bl.getZ()) * 2 > wb.getSize()){
            event.getPlayer().sendMessage("§b你无法传送到边界外");
            event.setCancelled(true);
            return;
        }
        //防地狱基岩上
        if (loc.getWorld().getEnvironment() == World.Environment.NETHER){
            if (loc.getY() > 125){
                loc.setY(100);
            }
        }
    }
}
