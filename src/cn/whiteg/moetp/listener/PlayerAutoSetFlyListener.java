package cn.whiteg.moetp.listener;

import cn.whiteg.moetp.Event.PlayerFarTpEvent;
import cn.whiteg.moetp.MoeTP;
import cn.whiteg.moetp.Setting;
import cn.whiteg.moetp.utils.PlayerFlyManage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

public class PlayerAutoSetFlyListener implements Listener {
    @EventHandler
    public void onTP(PlayerFarTpEvent event) {
        check(event.getPlayer());
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        check(event.getPlayer());
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        check(event.getPlayer());
    }

    public void check(Player player) {
        Bukkit.getScheduler().runTaskLater(MoeTP.plugin,() -> {
            PlayerFlyManage.setAllowFlightFly(player,player.hasPermission("mmo.fly.own"));
        },20);
    }

    public void unreg() {

    }
}
