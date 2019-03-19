package cn.whiteg.moetp.listener;

import cn.whiteg.moetp.Event.PlayerFarTpEvent;
import cn.whiteg.moetp.MoeTP;
import org.bukkit.Bukkit;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class RideTpListener implements Listener {
    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onFarTP(PlayerFarTpEvent event) {
        final Player p = event.getPlayer();
//        if (!p.isEmpty()){
//            final List<Entity> ps = new ArrayList<>();
//            EntityTpUtils.getEntityTopList(p,ps);
//            p.eject();
//            BukkitTask br = new BukkitRunnable() {
//                int i = ps.size() - 1;
//                boolean md = true;
//                Location loc = event.getTo();
//                //未完成 ， 监听传送事件实现传送带骑乘
//                @Override
//                public void run() {
//                    if (md){
//                        if (i > 0){
//                            Entity e = ps.get(i);
//                            if (!e.isEmpty()) e.eject();
//                            e.teleport(loc);
//                            i--;
//                        } else {
//                            md = false;
//                        }
//                    } else {
//                        Entity e = ps.get(i + 1);
//                        Entity v = ps.get(i);
//                        v.addPassenger(e);
//                        i++;
//                        if (i + 1 >= ps.size()){
//                            cancel();
//                        }
//
//                    }
//                }
//            }.runTaskTimer(MoeTP.plugin,2,2);
//        }
        final Entity v = p.getVehicle();
        if (v instanceof Animals){
            Bukkit.getScheduler().runTask(MoeTP.plugin,() -> {
                v.teleport(p);
                v.addPassenger(p);
            });
        }
    }
}
