package cn.whiteg.moetp.listener;

import cn.whiteg.mmocore.DataCon;
import cn.whiteg.mmocore.MMOCore;
import cn.whiteg.moetp.Setting;
import cn.whiteg.moetp.utils.EntityTpUtils;
import cn.whiteg.moetp.utils.WarpManager;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerSpawnListener implements Listener {
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player p = event.getPlayer();
        final DataCon pd = MMOCore.getPlayerData(p);
        if (pd.get("Player.quit_time") == null){
            chan(p);
            p.setBedSpawnLocation(WarpManager.getSpawn(),true);
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerQuit(PlayerQuitEvent event) {
        chan(event.getPlayer());
    }


    private void chan(Player player) {
        if (Setting.joinSpawn){
            final Location loc = WarpManager.getSpawn();
//            player.setBedSpawnLocation(loc);
            //EntityTpUtils.PlayerTP(player , loc);
            EntityTpUtils.PlayerOnceTp(player,loc);
            //player.teleport(loc , PlayerTeleportEvent.TeleportCause.ENDER_PEARL);
        }
    }

}
