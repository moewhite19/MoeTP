package cn.whiteg.moetp.listener;

import org.bukkit.craftbukkit.v1_15_R1.entity.CraftTNTPrimed;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;

public class antiTNT implements Listener {
    @EventHandler(priority = EventPriority.HIGH,ignoreCancelled = true)
    public void entityBoom(EntityExplodeEvent event) {
        if(event.getEntity() instanceof CraftTNTPrimed){
            TNTPrimed tnt = (CraftTNTPrimed) event.getEntity();
        }
    }
}
