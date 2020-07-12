package cn.whiteg.moetp.api;

import cn.whiteg.moetp.MoeTP;
import cn.whiteg.moetp.utils.EntityTpUtils;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DelayTp extends BukkitRunnable {
    private static final Map<UUID, DelayTp> map = new HashMap<>();
    final private Player player;
    final private Location todLoc;
    final private Location startLoc;
    private final BossBar bossBar;
    int pace = 0; //进度
    double range = 0.3D; //范围
    private int preTime; //需要时间
    private CallBack callback; //回调函数
    private double toLocEffY = 0.5D; //目标位置特效第二层

    public DelayTp(Player player,Location loc,int deny) {
        this.player = player;
        this.todLoc = loc;
        this.startLoc = player.getLocation();
        this.preTime = deny;
        if (deny > 1){
            bossBar = Bukkit.createBossBar("§b传送准备",BarColor.WHITE,BarStyle.SOLID);
            bossBar.addPlayer(player);
            bossBar.setProgress(0);
            runTaskTimer(MoeTP.plugin,1,1);
        } else {
            EntityTpUtils.PlayerOnceTp(player,loc);
            bossBar = null;
        }
    }

    public static Map<UUID, DelayTp> getMap() {
        return map;
    }

    public static DelayTp PlayerTp(Player player,Location loc,int dec) {
        DelayTp o = map.get(player.getUniqueId());
        if (o != null) o.onClose();
        o = new DelayTp(player,loc,dec * 20);
        map.put(player.getUniqueId(),o);
        return o;
    }

    @Override
    public void run() {
        if (!player.isOnline()){
            cancel();
            return;
        }
        if (!check()){
            onClose();
            if (callback != null)
                callback.onClose();
            return;
        }
        if (pace >= preTime){
            onTeleport();
        } else {
            //前10tick准备动画
            if (pace < 10){
                //玩家位置准备动画
                range += 0.08D;
                for (int i = 0; i < 360; i += 45) {
                    double radians = Math.toRadians((pace * 4) + i);
                    double hig = pace * 0.2;
                    Location playEffectLocation = startLoc.clone().add(range * Math.cos(radians),hig,range * Math.sin(radians));
                    playEffectLocation.getWorld().spawnParticle(Particle.TOTEM,playEffectLocation,5,0,0,0,0.0D); //经验球
                }

                //目标位置准备动画
                for (int i = 0; i < 360; i += 45) {
                    double radians = Math.toRadians((pace * 4) + i);
                    double hig = pace * 0.1;
                    Location playEffectLocation = todLoc.clone().add(range * Math.cos(radians),hig,range * Math.sin(radians));
                    playEffectLocation.getWorld().spawnParticle(Particle.REDSTONE,playEffectLocation,5,0,0,0,0D,new Particle.DustOptions(Color.FUCHSIA,1)); //红石自定义
                }
            } else {
                //玩家位置动画
                for (int i = 0; i < 360; i += 45) {
                    double radians = Math.toRadians((pace * 4) + i);
                    Location playEffectLocation = startLoc.clone().add(range * Math.cos(radians),2D,range * Math.sin(radians));
                    playEffectLocation.getWorld().spawnParticle(Particle.TOTEM,playEffectLocation,5,0,0,0,0.0D); //经验球
                    //playEffectLocation.getWorld().spawnParticle(Particle.PORTAL,playEffectLocation,500,null);//末影珍珠效果
                    //playEffectLocation.getWorld().spawnParticle(Particle.REDSTONE,playEffectLocation,5,0,0,0,0D, new Particle.DustOptions(Color.ORANGE, 2)); //红石自定义
                }
                //玩家位置反向动画
//                for (int i = 45; i < 360; i += 90) {
//                    double radians = Math.toRadians(~(pace * 4) - i);
//                    Location playEffectLocation = startLoc.clone().add(range * Math.cos(radians),2D,range * Math.sin(radians));
//                    playEffectLocation.getWorld().spawnParticle(Particle.TOTEM,playEffectLocation,5,0,0,0,0.0D); //经验球
//                }


                //目标位置动画
                for (int i = 0; i <= 360; i += 90) {
                    double radians = Math.toRadians((pace * 4) + i);
                    Location playEffectLocation = todLoc.clone().add(range * Math.cos(radians),0.5D,range * Math.sin(radians));
                    playEffectLocation.getWorld().spawnParticle(Particle.REDSTONE,playEffectLocation,5,0,0,0,0D,new Particle.DustOptions(Color.FUCHSIA,2)); //红石自定义
                }
                //目标位置第二层
                if (pace > 13 && pace < 25) toLocEffY += 0.08D;
                for (int i = 45; i <= 360; i += 90) {
                    double radians = Math.toRadians((pace * 4) + i);
                    Location playEffectLocation = todLoc.clone().add(range * Math.cos(radians),toLocEffY,range * Math.sin(radians));
                    playEffectLocation.getWorld().spawnParticle(Particle.REDSTONE,playEffectLocation,5,0,0,0,0D,new Particle.DustOptions(Color.FUCHSIA,pace < 25 ? 1 : 2)); //红石自定义
                }
            }

            pace++;
            bossBar.setProgress(getProgress());
        }


    }

    public float getProgress() {
        return ((float) pace) / preTime;
    }

    public boolean check() {
        try{
            if (!player.isOnline() || player.getLocation().distance(startLoc) > 0.5){
                return false;
            }
        }catch (Exception ignored){
            return false;
        }
        return true;
    }

    public void onTeleport() {
        cancel();
        player.eject();
        player.closeInventory();
        final boolean status = player.teleport(todLoc);
        bossBar.removeAll();

        //传送完成后的的话
        Vector off = new Vector(0,0.5,0);
        if (status){
            startLoc.getWorld().spawnParticle(Particle.PORTAL,startLoc.clone().add(off),600,null);//末影珍珠效果
            todLoc.getWorld().spawnParticle(Particle.SPELL_WITCH,todLoc.clone().add(off),600,null);//女巫效果
        } else {
            startLoc.getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE,startLoc.clone().add(off),600,null);//附魔台效果
            todLoc.getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE,todLoc.clone().add(off),600,null);//附魔台效果
        }

        if (callback != null){
            callback.onTeleport(status);
        }
    }

    public void onClose() {
        cancel();
        player.sendMessage("§b传送已取消");
        bossBar.removeAll();
        Vector off = new Vector(0,0.5,0);
        startLoc.getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE,startLoc.clone().add(off),600,null);//附魔台效果
        todLoc.getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE,todLoc.clone().add(off),600,null);//附魔台效果
        if (callback != null){
            callback.onClose();
        }
    }

    public Player getPlayer() {
        return player;
    }

    public Location getTodLoc() {
        return todLoc;
    }

    public int getPreTime() {
        return preTime;
    }

    public void setCallback(CallBack callback) {
        this.callback = callback;
    }

    @Override
    public synchronized void cancel() throws IllegalStateException {
        map.remove(player.getUniqueId());
        super.cancel();
    }
}
