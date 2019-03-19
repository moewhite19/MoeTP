package cn.whiteg.moetp.listener;

import cn.whiteg.mmocore.DataCon;
import cn.whiteg.mmocore.MMOCore;
import cn.whiteg.mmocore.util.CoolDownUtil;
import cn.whiteg.mmocore.util.YamlUtils;
import cn.whiteg.moeInfo.nms.ActionBar;
import cn.whiteg.moetp.Event.PlayerFarTpEvent;
import cn.whiteg.moetp.MoeTP;
import cn.whiteg.moetp.Setting;
import cn.whiteg.moetp.utils.EntityTpUtils;
import cn.whiteg.moetp.utils.LocationUtils;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public class PlayerFarTP implements Listener {

    public static boolean setBackLoc(Player p) {
        if (EntityTpUtils.noBackPlayer.equals(p.getName())){
            EntityTpUtils.noBackPlayer = "";
            return false;
        }
        Location loc = p.getLocation();
        int minHeight = p.getWorld().getMinHeight();
        if (loc.getY() < minHeight){
            var block = p.getWorld().getHighestBlockAt(loc);
            loc = block.getLocation();
            if (loc.getY() <= minHeight){
                p.sendMessage(" §bBack位置无效");
                return false;
            }
        }
        DataCon dc = MMOCore.getPlayerData(p);
        YamlUtils.setLocation(dc.getConfig(),"Player.Back",loc);
        dc.onSet();
        return true;
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onFarTp(PlayerFarTpEvent event) {
        if (event.getCause() != PlayerTeleportEvent.TeleportCause.PLUGIN) return;
        if (hasDec(event.getTo(),event.getFrom())) return;
        final Player p = event.getPlayer();
        final boolean isNoCd = p.getName().equals(EntityTpUtils.noCdPlayer);
        if (isNoCd || (Setting.ignoreLoadedChunk && event.getTo().getWorld().isChunkLoaded(event.getTo().getBlockX() >> 4,event.getTo().getBlockZ() >> 4))){
            EntityTpUtils.noCdPlayer = "";
        } else {
            final double distance = LocationUtils.LocationDistance(event.getFrom(),event.getTo());
            if (EntityTpUtils.isTeleportCoolDown(p)){
                ActionBar.sendActionBar(p,"§3§l> §b阁下传送还在冷却中,下次充能完成还需要§f" + CoolDownUtil.getCds(p.getName(),EntityTpUtils.getCoolDownTag()) + "§b秒 §3§l<");
                event.setCancelled(true);
                return;
            }
            if (Setting.deductMoneyRate > 0){
                EconomyResponse status = MoeTP.plugin.getEconomy().withdrawPlayer(event.getPlayer(),distance * Setting.deductMoneyRate);
                if (status.type == EconomyResponse.ResponseType.SUCCESS){
                    p.sendMessage("§b已消费§f" + status.amount + MoeTP.plugin.getEconomy().currencyNamePlural() + "§b用于传送");
                } else {
                    p.sendMessage("§b无法传送到目标，原因: §f" + status.errorMessage);
                    event.setCancelled(true);
                    return;
                }
            }
            if (Setting.rcoolDownRate > 0)
                CoolDownUtil.setCd(p.getName(),EntityTpUtils.getCoolDownTag(),(int) (distance * Setting.rcoolDownRate));
        }
        setBackLoc(p);

        //p.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION , 20,1));
    }

    @EventHandler(ignoreCancelled = true)
    public void onDed(PlayerDeathEvent event) {
        Player p = event.getEntity();
        if (!setBackLoc(p)) return;
        TextComponent a1 = new TextComponent(" §3* §b§n§l点我返回死亡位置§3 *");
        a1.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,"/back"));
        a1.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,new Text("§b返回死亡位置")));
        event.getEntity().spigot().sendMessage(a1);
    }

    public boolean hasDec(Location l1,Location l2) {
        final double x1 = l1.getX();
        final double z1 = l1.getZ();
        final double x2 = l2.getX();
        final double z2 = l2.getZ();
        final double dec = Math.abs(x1 - x2) + Math.abs(z1 - z2);
        return dec < 32;
    }

    public void unreg() {
        PlayerFarTpEvent.getHandlerList().unregister(this);
        PlayerDeathEvent.getHandlerList().unregister(this);
    }
}
