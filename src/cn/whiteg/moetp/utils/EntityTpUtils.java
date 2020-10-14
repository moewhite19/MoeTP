package cn.whiteg.moetp.utils;

import cn.whiteg.mmocore.util.CoolDownUtil;
import cn.whiteg.moetp.MoeTP;
import cn.whiteg.moetp.Setting;
import cn.whiteg.moetp.api.DelayTp;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_16_R2.entity.CraftEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.vehicle.VehicleExitEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.spigotmc.event.entity.EntityDismountEvent;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class EntityTpUtils {
    public final static Field vehicleField;
    public final static String tag = "§3传送";
    public static String noCdPlayer = "";
    public static String noBackPlayer = "";

    static {
        Field f = null;
        try{
            f = net.minecraft.server.v1_16_R2.Entity.class.getDeclaredField("vehicle");
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
        //检查cd
        if (checkCd){
            if (isTeleportCoolDown(p)){
                p.sendMessage("§b传送还在冷却中");
                return false;
            }
        } else {
            noCdPlayer = p.getName().intern();
        }
        //储存back
        if (!saveBack){
            noBackPlayer = p.getName().intern();
        }

        //如果玩家拥有权限直接传送，无需等待
        if (Setting.DelayTpTime > 0 && !p.hasPermission("mmo.tpdelay")){
            DelayTp.PlayerTp(p,loc,Setting.DelayTpTime);
        } else {
            return PlayerOnceTp(p,loc);
        }
        return true;
    }

    //立即传送玩家
    public static boolean PlayerOnceTp(Player player,Location loc) {
        player.eject();
        player.closeInventory();
        player.setFallDistance(0F);
        forgeStopRide(player);
//        if (Setting.teleportAsync){ //Paper方法,异步tp
//            player.teleportAsync(loc,PlayerTeleportEvent.TeleportCause.PLUGIN);
//            return true;
//        }
        return player.teleport(loc.clone(),PlayerTeleportEvent.TeleportCause.PLUGIN);
    }

    public static boolean loadChunk(World world,int x,int z) {
        Chunk c = world.getChunkAt(x,z);
        return c.load(true);

    }

    //检查传送是否在cd
    public static boolean isTeleportCoolDown(Player p) {
        if (p.hasPermission("mmo.tpnocd")) return false;
        return !CoolDownUtil.hasCd(p.getName(),tag);
    }

    //设置传送cd
    public static void setTeleportCoolDown(Player p,int s) {
        CoolDownUtil.setCds(p.getName(),tag,s);
    }

    public static String getCoolDownTag() {
        return tag;
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
        if (!vers.isEmpty()) el.addAll(vers);
        for (Entity value : el) {
            value.teleport(loc);
        }
        new BukkitRunnable() {
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
        net.minecraft.server.v1_16_R2.Entity ne = ((CraftEntity) entity).getHandle();
        net.minecraft.server.v1_16_R2.Entity nv = ne.getVehicle();
        if (nv != null){
            Entity v = entity.getVehicle();
            if (v instanceof Vehicle && entity instanceof LivingEntity){
                VehicleExitEvent ev = new VehicleExitEvent((Vehicle) v,(LivingEntity) entity);
            }
            EntityDismountEvent ev = new EntityDismountEvent(entity,v);
            Bukkit.getPluginManager().callEvent(ev);
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
        net.minecraft.server.v1_16_R2.Entity ne = ((CraftEntity) entity).getHandle();
        ne.enderTeleportTo(loc.getX(),loc.getY(),loc.getZ());
    }

    public static void enderTeleportTo(Entity entity,double x,double y,double z) {
        net.minecraft.server.v1_16_R2.Entity ne = ((CraftEntity) entity).getHandle();
        ne.enderTeleportTo(x,y,z);
    }
}
