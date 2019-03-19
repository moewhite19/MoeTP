package cn.whiteg.moetp.listener;

import cn.whiteg.moetp.Event.PlayerFarTpEvent;
import cn.whiteg.moetp.Setting;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.WorldBorder;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class SafeTpListener implements Listener {
    public static String msg = "§b你无法传送到边界外";

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onPlayerTP(PlayerFarTpEvent event) {
        final Location loc = event.getTo();
        Location bl = loc.clone();
        final World bw = bl.getWorld();

        //防止传送到边界外
        if (Setting.outOfBorder){
            final WorldBorder wb = bw.getWorldBorder();
            bl.add(wb.getCenter());

            if (!wb.isInside(bl)){
                event.getPlayer().sendMessage(msg);
                event.setCancelled(true);
                return;
            }
        }

        //防止传送到地狱基岩上
        if (Setting.outOfNether){
            byte high = 125;
            bl = loc.clone();
            if (bw.getEnvironment() == World.Environment.NETHER){
                if (bl.getY() > high){
                    for (; high > 0; high--) {
                        bl.setY(high);
                        Block block = bl.getBlock();
                        Material type = block.getType();
                        if (type != Material.LAVA && !type.isSolid()){
                            high--;
                            bl.setY(high);
                            block = bl.getBlock();
                            if (block.getType().isSolid()){
                                bl.setY(high + 1);
                                bl.setX(bl.getBlockX() + 0.5D);
                                bl.setZ(bl.getBlockZ() + 0.5D);
                                event.setTo(bl);
                                return;
                            }
                        }
                    }
                    bl.setY(100);
                    event.setTo(bl);
                }
            }
        }
    }
}
