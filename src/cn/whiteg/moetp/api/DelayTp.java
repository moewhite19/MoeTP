package cn.whiteg.moetp.api;

import cn.whiteg.mmocore.sound.Sound;
import cn.whiteg.mmocore.sound.SoundPlayer;
import cn.whiteg.moetp.MoeTP;
import cn.whiteg.moetp.Setting;
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
    final private Location toLoc;
    final private Location startLoc;
    private final BossBar bossBar;
    int pace = 0; //进度
    double radius = 0.3D; //范围
    private int preTime; //需要时间
    private CallBack callback; //回调函数
    private double toLocEffY = 0.5D; //目标位置特效第二层
    private int period = 2; //步进间隔,越小越精细，同时越消耗资源

    DelayTp(Player player,Location loc,int deny) {
        this.player = player;
        this.toLoc = loc;
        this.startLoc = player.getLocation();
        this.preTime = deny + 20;
        if (deny > 1){
            bossBar = Bukkit.createBossBar("§b传送准备",BarColor.WHITE,BarStyle.SOLID);
            bossBar.addPlayer(player);
            bossBar.setProgress(0);
            runTaskTimer(MoeTP.plugin,0,period);
        } else {
            EntityTpUtils.PlayerOnceTp(player,loc);
            bossBar = null;
        }
        playSound(Setting.START_SOUND);
    }

    public static Map<UUID, DelayTp> getMap() {
        return map;
    }

    public static DelayTp PlayerTp(Player player,Location loc,int dec) {
        UUID uuid = player.getUniqueId();
        DelayTp o = map.get(uuid);
        if (o != null) o.onBreak();
        o = new DelayTp(player,loc,dec * 20);
        map.put(uuid,o);
        return o;
    }

    @Override
    public void run() {
        if (!player.isOnline()){
            cancel();
            return;
        }
        if (!check()){
            onBreak();
            if (callback != null)
                callback.onBreak();
            return;
        }
        if (preTime <= pace){
            onTeleport();
            return;
        }

        //int rest = preTime - pace; //剩余Tick

        if (callback != null) callback.tick();

        if (Setting.PlayParticle){
            //颜色
            int rgbi = (int) (0xFF * getProgress());
            Color color;
            color = Color.fromRGB(0xFF,0xFF - rgbi,rgbi); //从黄到紫色
            //前20tick逐步扩大半径
            if (pace < 20){
                radius += 0.03D * period;
            }
            //玩家当前位置
            //第一层
            double size = 0.11D * getProgress(); //粒子范围
            int number = (int) (5 + (5F * getProgress())); //粒子数量
            if (pace < 10){
                //开始从低到高
                double hig = pace * 0.1;
                for (int i = 0; i < 360; i += 45) {
                    double radians = Math.toRadians((pace * 2 * period) + i);
                    Location loc = startLoc.clone().add(radius * Math.cos(radians),hig,radius * Math.sin(radians));
                    loc.getWorld().spawnParticle(Particle.REDSTONE,loc,number,0,0,0,0D,new Particle.DustOptions(color,1)); //红石自定义
                }
            } else {
                for (int i = 0; i < 360; i += 90) {
                    double radians = Math.toRadians((pace * 2 * period) + i);
                    Location loc = startLoc.clone().add(radius * Math.cos(radians),0.5D,radius * Math.sin(radians));
                    loc.getWorld().spawnParticle(Particle.REDSTONE,loc,number,size,size,size,0D,new Particle.DustOptions(color,1)); //红石自定义
                }
            }
            //第二层
            if (pace < 20){
                //开始从低到高
                double hig = pace * 0.1;
                for (int i = 45; i < 360; i += 90) {
                    double radians = Math.toRadians(~(pace * 2 * period) + i);
                    Location loc = startLoc.clone().add(radius * Math.cos(radians),hig,radius * Math.sin(radians));
                    loc.getWorld().spawnParticle(Particle.REDSTONE,loc,number,0,0,0,0D,new Particle.DustOptions(color,1)); //红石自定义
                }
            } else {
                for (int i = 45; i < 360; i += 90) {
                    double radians = Math.toRadians(~(pace * 2 * period) + i);
                    Location playEffectLocation = startLoc.clone().add(radius * Math.cos(radians),toLocEffY,radius * Math.sin(radians));
                    playEffectLocation.getWorld().spawnParticle(Particle.REDSTONE,playEffectLocation,number,size,size,size,0D,new Particle.DustOptions(color,1)); //红石自定义
                }
            }

            color = Color.fromRGB(rgbi,0xFF - rgbi,0xFF); //从蓝到紫
            //目的地位置动画
            if (pace < 10){
                //目标位置准备动画
                double hig = pace * 0.1;
                for (int i = 0; i < 360; i += 45) {
                    double radians = Math.toRadians((pace * 3 * period) + i);
                    Location loc = toLoc.clone().add(radius * Math.cos(radians),hig,radius * Math.sin(radians));
                    loc.getWorld().spawnParticle(Particle.REDSTONE,loc,number,size,size,size,0D,new Particle.DustOptions(color,1)); //红石自定义
                }
            } else {
                for (int i = 0; i <= 360; i += 90) {
                    double radians = Math.toRadians((pace * 3 * period) + i);
                    Location loc = toLoc.clone().add(radius * Math.cos(radians),0.5D,radius * Math.sin(radians));
                    loc.getWorld().spawnParticle(Particle.REDSTONE,loc,number,size,size,size,0D,new Particle.DustOptions(color,1)); //红石自定义
                }
                //目标位置第二层
                if (pace > 13 && pace < 25) toLocEffY += 0.08D * period;
                //size = pace < 25 ? 0 : 0.1;
                for (int i = 45; i <= 360; i += 90) {
                    double radians = Math.toRadians((pace * 3 * period) + i);
                    Location playEffectLocation = toLoc.clone().add(radius * Math.cos(radians),toLocEffY,radius * Math.sin(radians));
                    playEffectLocation.getWorld().spawnParticle(Particle.REDSTONE,playEffectLocation,5,size,size,size,0D,new Particle.DustOptions(color,1)); //红石自定义
                }
            }
        }

        pace += period;
        bossBar.setProgress(getProgress());
    }

    //获取浮点数进度0 - 1;
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
        final boolean status = EntityTpUtils.PlayerOnceTp(player,toLoc);
        bossBar.removeAll();

        //传送完成后的粒子
        Vector off = new Vector(0,0.5,0);
        if (status){
            startLoc.getWorld().spawnParticle(Particle.SPELL_WITCH,startLoc.add(off),600);//女巫效果
            toLoc.getWorld().spawnParticle(Particle.PORTAL,toLoc.add(off),800);//末影珍珠效果
            playSound(Setting.END_SOUND);
            stopSound(Setting.START_SOUND);
            //解决玩家传送到目的地后看不到自己的传送特效
            player.spawnParticle(Particle.PORTAL,toLoc,800);
            if (!(Setting.END_SOUND instanceof SoundPlayer)) Setting.END_SOUND.playTo(player,toLoc);
        } else {
            startLoc.getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE,startLoc.add(off),600);//附魔台效果
            toLoc.getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE,toLoc.add(off),600);//附魔台效果
        }

        if (callback != null){
            callback.onTeleport(status);
        }
    }

    public void onBreak() {
        cancel();
        player.sendActionBar("§b传送已取消");
        EntityTpUtils.setTeleportCoolDown(player,5);
        bossBar.removeAll();
        Vector off = new Vector(0,0.5,0);
        startLoc.getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE,startLoc.clone().add(off),600,null);//附魔台效果
        toLoc.getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE,toLoc.clone().add(off),600,null);//附魔台效果
        if (callback != null){
            callback.onBreak();
        }
        stopSound(Setting.START_SOUND);
        playSound(Setting.BREAK_SOUND);
    }

    public Player getPlayer() {
        return player;
    }

    public Location getToLoc() {
        return toLoc;
    }

    public int getPreTime() {
        return preTime;
    }

    public void setCallback(CallBack callback) {
        this.callback = callback;
    }

    public void playSound(Sound sound) {
        sound.playTo(startLoc);
        sound.playTo(toLoc);
    }

    public void stopSound(Sound sound) {
        sound.stopTo(startLoc);
        sound.stopTo(toLoc);
    }

    @Override
    public synchronized void cancel() {
        map.remove(player.getUniqueId());
        if (callback != null){
            callback.onCancel();
        }
        super.cancel();
    }
}
