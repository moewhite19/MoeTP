package cn.whiteg.moetp.utils;

import cn.whiteg.moetp.MoeTP;
import cn.whiteg.moetp.Setting;
import cn.whiteg.moetp.api.DelayTp;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.vehicle.VehicleExitEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.spigotmc.event.entity.EntityDismountEvent;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

public class EntityTpUtils {
    public final static Field vehicleField;
    public static Map<String, Long> tpTime = new WeakHashMap<>();
    public static String noCdPlayer = "";
    public static String noBackPlayer = "";

    static {
        Field f = null;
        try{
            f = net.minecraft.server.v1_15_R1.Entity.class.getDeclaredField("vehicle");
            f.setAccessible(true);
        }catch (NoSuchFieldException e){
            e.printStackTrace();
        }
        vehicleField = f;
    }

    public static void PlayerTpNoCd(Player p,Location loc) {
        noCdPlayer = p.getName().intern();
        p.teleport(loc);

    }

    public static boolean PlayerTP(Player p,Location loc) {
        return PlayerTP(p,loc,true,true);
    }

    public static boolean PlayerTP(Player p,Location loc,boolean checkCd) {
        return PlayerTP(p,loc,checkCd,true);
    }

    public static boolean PlayerTP(Player p,Location loc,boolean checkCd,boolean saveBack) {
        if (checkCd){
            if (iscd(p)){
                p.sendMessage("§b传送还在冷却中");
                return false;
            }
        } else {
            noCdPlayer = p.getName().intern();
        }
        if (!saveBack){
            noBackPlayer = p.getName().intern();
        }

        //if (!p.isEmpty() || p.getVehicle() != null) return RideTP(p,loc);
        if (Setting.DelayTpTime > 0){
            DelayTp.PlayerTp(p,loc,Setting.DelayTpTime);
        } else {
            p.eject();
            return PlayerOnceTp(p,loc);
        }
/*
        final byte[] con = {1}; BukkitRunnable br = new BukkitRunnable() {
            @Override
            public void run() {
                if (con[0] == 1){
                    Chunk chunk = loc.getWorld().getChunkAt(loc);
                    if (!chunk.isLoaded()){
                        chunk.load();
                    }
                } else {
                    byte r = con[0];
                    p.sendMessage("正在加载区块 " + r);
                    byte sta = 0;
                    Chunk c = loc.getChunk();
                    while (sta <= 3) {
                        if (sta == 0){
                            int x = c.getX() - r;
                            int z = c.getZ() + r;
                            while (x < c.getX() + r) {
                                loadChunk(loc.getWorld(),x,z);
                                x++;
                            }
                        } else if (sta == 1){
                            int x = c.getX() - r;
                            int z = c.getZ() - r;
                            while (x < c.getX() + r) {
                                loadChunk(loc.getWorld(),x,z);
                                x++;
                            }
                        } else if (sta == 2){
                            int x = c.getX() - r;
                            int z = c.getZ() - r;
                            while (z < c.getZ() + r) {
                                loadChunk(loc.getWorld(),x,z);
                                z++;
                            }
                        } else if (sta == 3){
                            int x = c.getX() + r;
                            int z = c.getZ() - r;
                            while (z < c.getZ() + r) {
                                loadChunk(loc.getWorld(),x,z);
                                z++;
                            }
                        } else {
                            break;
                        }
                        sta++;
                    }
                }
                if (con[0] > 5){
                    Chunk chunk = loc.getChunk();
                    if (chunk.isLoaded()){
                        p.sendMessage("区块已预加载");
                    }
                    p.teleport(loc,PlayerTeleportEvent.TeleportCause.ENDER_PEARL);
                    cancel();
                }
                con[0]++;
            }
        };
        br.runTaskTimer(MoeTP.plugin,10,10);*/
        return true;
    }

    public static boolean PlayerOnceTp(Player player,Location loc) {
        player.eject();
        player.closeInventory();
        player.setFallDistance(0F);
        forgeStopRide(player);
        return player.teleport(loc,PlayerTeleportEvent.TeleportCause.PLUGIN);
    }

    public static boolean loadChunk(World world,int x,int z) {
        Chunk c = world.getChunkAt(x,z);
        return c.load(true);

    }

    public static void updatacd(Player p) {
        tpTime.put(new String(p.getName()),System.currentTimeMillis());
    }

    public static boolean iscd(Player p) {
        long t = tpTime.getOrDefault(p.getName(),0L);
        if (t == 0L) return false;
        return System.currentTimeMillis() - t < Setting.tpcd;
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
        BukkitTask br = new BukkitRunnable() {
            int i = 0;

            @Override
            public void run() {
                if (i < el.size()){
                    el.get(i).teleport(loc);
                    i++;
                } else {

                    for (int i = el.size() - 2; i >= 0; i--) {
                        el.get(i + 1).addPassenger(el.get(i));
                    }
                    cancel();
                }
            }
        }.runTaskTimer(MoeTP.plugin,2,2);
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

    public static void forgeStopRide(Entity entity) {
        net.minecraft.server.v1_15_R1.Entity ne = ((CraftEntity) entity).getHandle();
        net.minecraft.server.v1_15_R1.Entity nv = ne.getVehicle();
        if (nv != null){
            Entity v = entity.getVehicle();
            if (v instanceof Vehicle && entity instanceof LivingEntity){
                VehicleExitEvent ev = new VehicleExitEvent((Vehicle) v,(LivingEntity) entity);
                ev.callEvent();
            }
            EntityDismountEvent ev = new EntityDismountEvent(entity,v);
            ev.callEvent();
            ne.getVehicle().passengers.remove(ne);
            try{
                vehicleField.set(ne,null);
            }catch (IllegalAccessException e){
                e.printStackTrace();
            }
        }
    }

    public static void enderTeleportTo(Entity entity,Location loc) {
        if (loc.getWorld() != entity.getWorld()){
            throw new IllegalArgumentException("Cannot measure distance between " + entity.getWorld().getName() + " and " + loc.getWorld().getName());
        }
        net.minecraft.server.v1_15_R1.Entity ne = ((CraftEntity) entity).getHandle();
        ne.enderTeleportTo(loc.getX(),loc.getY(),loc.getZ());
    }

    public static void enderTeleportTo(Entity entity,double x,double y,double z) {
        net.minecraft.server.v1_15_R1.Entity ne = ((CraftEntity) entity).getHandle();
        ne.enderTeleportTo(x,y,z);
    }
}
