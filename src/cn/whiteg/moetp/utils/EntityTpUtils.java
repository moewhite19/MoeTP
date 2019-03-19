package cn.whiteg.moetp.utils;

import cn.whiteg.mmocore.util.CoolDownUtil;
import cn.whiteg.mmocore.util.NMSUtils;
import cn.whiteg.moetp.MoeTP;
import cn.whiteg.moetp.Setting;
import cn.whiteg.moetp.api.DelayTp;
import com.google.common.collect.ImmutableList;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.vehicle.VehicleExitEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.spigotmc.event.entity.EntityDismountEvent;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class EntityTpUtils {
    public final static String tag = "§3传送";
    public static Field vehicleField;
    public static Field passengersField;
    public static Method endTpMethod;
    public static String noCdPlayer = "";
    public static String noBackPlayer = "";

    static {
        try{
            passengersField = NMSUtils.getFieldFormType(net.minecraft.world.entity.Entity.class,ImmutableList.class);
            passengersField.setAccessible(true);
            vehicleField = NMSUtils.getFieldFormType(net.minecraft.world.entity.Entity.class,net.minecraft.world.entity.Entity.class);
            vehicleField.setAccessible(true);
        }catch (NoSuchFieldException e){
            e.printStackTrace();
        }

        for (Method method : net.minecraft.world.entity.Entity.class.getMethods()) {
            if (method.getReturnType() != void.class) continue;
            final Class<?>[] types = method.getParameterTypes();
            if (types.length == 3){
                for (Class<?> type : types) {
                    if (!type.equals(double.class)) break;
                }
                //当三个参数都是double
                //在Entity.class 搜索关键字"UnmodifiableIterator"可找到方法
                endTpMethod = method;
                break;
            }
        }

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

        //区块预加载
        LocationUtils.worldPreloading(loc);

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
        var ne = NMSUtils.getNmsEntity(entity);
        Entity v = entity.getVehicle();
        if (v != null){
            if (v instanceof Vehicle vehicle && entity instanceof LivingEntity living){
                VehicleExitEvent ev = new VehicleExitEvent(vehicle,living);
                Bukkit.getServer().getPluginManager().callEvent(ev);
//                ev.callEvent();
            }
            EntityDismountEvent ev = new EntityDismountEvent(entity,v);
            Bukkit.getPluginManager().callEvent(ev);
//            ne.stopRiding(false);
            try{
                var nv = NMSUtils.getNmsEntity(v);
                //noinspection unchecked
                List<net.minecraft.world.entity.Entity> list = (List<net.minecraft.world.entity.Entity>) passengersField.get(nv);
                if (list.size() == 1 && list.get(0) == ne){
                    passengersField.set(nv,ImmutableList.of());
                } else {
                    passengersField.set(nv,list.stream().filter((enter) -> enter != ne).collect(ImmutableList.toImmutableList()));
                }
                vehicleField.set(ne,null);
            }catch (IllegalAccessException e){
                e.printStackTrace();
            }
//            entity.s = 60;
        }
    }

    public static void enderTeleportTo(Entity entity,Location loc) {
        if (loc.getWorld() != entity.getWorld()){
            throw new IllegalArgumentException("Cannot measure distance between " + entity.getWorld().getName() + " and " + loc.getWorld().getName());
        }

        //在Entity.class 搜索关键字"UnmodifiableIterator"可找到方法
        var ne = NMSUtils.getNmsEntity(entity);
        ne.a(loc.getX(),loc.getY(),loc.getZ());
//
//        try{
//            endTpMethod.invoke(ne,loc.getX(),loc.getY(),loc.getZ());
//        }catch (Exception e){
//            e.printStackTrace();
//        }
    }

    public static void enderTeleportTo(Entity entity,double x,double y,double z) {
        var ne = NMSUtils.getNmsEntity(entity);
        //在Entity.class 搜索关键字"UnmodifiableIterator"可找到方法
        try{
            endTpMethod.invoke(ne,x,y,z);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
