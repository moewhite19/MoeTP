package cn.whiteg.moetp.utils;

import cn.whiteg.mmocore.DataCon;
import cn.whiteg.mmocore.MMOCore;
import cn.whiteg.moetp.MoeTP;
import cn.whiteg.moetp.Setting;
import cn.whiteg.rpgArmour.utils.RideManage;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class EntityTpUtils {
    public static boolean PlayerTP(Player p,Location loc) {
        DataCon dc = MMOCore.getPlayerData(p);
        long now = System.currentTimeMillis();
        if (now - dc.tpTime < 3000){
            p.sendMessage("§b传送还在冷却中");
            return false;
        }
        dc.tpTime = now;
        if (!p.isEmpty() || p.getVehicle() != null) return RideTP(p,loc);
        p.teleport(loc);
        return true;
    }

    public static boolean RideTP(Entity entity,Location loc) {
        List<Entity> top = new ArrayList<>();
        List<Entity> vers = new ArrayList<>();
        List<Entity> el = new ArrayList<>();
        getEntityTopList(entity,top);
        getEntityDownList(entity,vers);
        if (!top.isEmpty()) for (int i = top.size() - 1; i >= 0; i--) {
            el.add(top.get(i));
        }
        el.add(entity);
        if (!vers.isEmpty()) for (int i = 0; i < vers.size(); i++) {
            el.add(vers.get(i));
        }
        for (int i = 0; i < el.size(); i++) {
            el.get(i).teleport(loc);
        }
        Bukkit.getScheduler().runTask(MoeTP.plugin,() -> {
            for (int i = el.size() - 2; i >= 0; i--) {
                el.get(i + 1).addPassenger(el.get(i));
            }
        });
        if (Setting.DEBUG) entity.sendMessage(el.toString());
/*        if (entity.getPassengers() != null){
            List<Entity> toplist = new ArrayList<>();
            Entity end = RideManage.getEntityTopList(entity,toplist);
            if (toplist.size() == 1){
                end.teleport(loc);
                Bukkit.getScheduler().runTask(MoeTP.plugin,() -> {
                    entity.teleport(loc);
                    end.addPassenger(entity);
                });
                return true;
            } else {
                return false;
            }
        }
        if (v == null || v instanceof Player){
            entity.teleport(loc);
            return true;
        } else {
            entity.teleport(loc);
            v.teleport(loc);
            RideManage.Ride(entity,v);
            return RideTP(v,loc);
        }*/
        return true;
    }

    public static Entity getEntityTopList(Entity entity,List<Entity> list) {
        if (entity.isEmpty()) return entity;
        list.addAll(entity.getPassengers());
        return getEntityTopList(list.get(list.size() - 1),list);
    }

    public static Entity getEntityDownList(Entity entity,List<Entity> list) {
        if (entity.getVehicle() == null) return entity;
        list.add(entity.getVehicle());
        return getEntityDownList(entity.getVehicle(),list);
    }
}
