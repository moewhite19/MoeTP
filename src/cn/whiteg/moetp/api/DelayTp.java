package cn.whiteg.moetp.api;

import cn.whiteg.moetp.MoeTP;
import cn.whiteg.moetp.Setting;
import cn.whiteg.moetp.utils.EntityTpUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DelayTp {
    static private Map<UUID, DelayTp> map = new HashMap<>();

    final private Player player;
    final private Location tloc;
    final private Location sloc;
    final private BukkitTask task;
    private final BossBar bossBar;
    private int num;
    private CallBack callback;

    public DelayTp(Player player,Location loc,int deny) {
        this.player = player;
        this.tloc = loc;
        this.sloc = player.getLocation();
        this.num = deny;
        if (deny >= 1){
            bossBar = Bukkit.createBossBar("§b传送准备",BarColor.WHITE,BarStyle.SOLID);
            bossBar.addPlayer(player);
            bossBar.setProgress(0);
            task = new BukkitRunnable() {
                /**
                 * When an object implementing interface <code>Runnable</code> is used
                 * to create a thread, starting the thread causes the object's
                 * <code>run</code> method to be called in that separately executing
                 * thread.
                 * <p>
                 * The general contract of the method <code>run</code> is that it may
                 * take any action whatsoever.
                 *
                 * @see Thread#run()
                 */
                @Override
                public void run() {
                    if (!player.isOnline()){
                        cancel();
                        return;
                    }
                    if (!check()){
                        close();
                        if (callback != null)
                            callback.onClose();
                        return;
                    }
                    num--;
                    if (num <= 0){
                        onTP();
                    } else {
                        bossBar.setProgress(getProgress());
                    }
                }
            }.runTaskTimer(MoeTP.plugin,20,20);
        } else {
            EntityTpUtils.PlayerOnceTp(player,loc);
            task = null;
            bossBar = null;
        }
    }

    public static Map<UUID, DelayTp> getMap() {
        return map;
    }

    public static DelayTp PlayerTp(Player player,Location loc,int dec) {
        DelayTp o = map.get(player.getUniqueId());
        if (o != null) o.close();
        o = new DelayTp(player,loc,dec);
        map.put(player.getUniqueId(),o);
        return o;
    }

    public float getProgress() {
        return 1F - ((float) num) / Setting.DelayTpTime;
    }

    public boolean check() {
        try{
            if (!player.isOnline() || player.getLocation().distance(sloc) > 0.5){
                return false;
            }
        }catch (Exception ignored){
            return false;
        }
        return true;
    }

    public void onTP() {
        player.eject();
        player.closeInventory();
        final boolean status = player.teleport(tloc);
        if (task != null){
            task.cancel();
            bossBar.removeAll();
        }
        map.remove(player.getUniqueId());
        if (callback != null){
            callback.onDone(status);
        }
    }

    public void close() {
        player.sendMessage("§b传送已取消");
        map.remove(player.getUniqueId());
        if (task != null){
            task.cancel();
            bossBar.removeAll();
        }
        if (callback != null){
            callback.onClose();
        }
    }

    public Player getPlayer() {
        return player;
    }

    public Location getTloc() {
        return tloc;
    }

    public int getNum() {
        return num;
    }

    public void setCallback(CallBack callback) {
        this.callback = callback;
    }

    public abstract class CallBack {
        public void onDone(boolean status) {

        }

        public void onClose() {

        }
    }
}
