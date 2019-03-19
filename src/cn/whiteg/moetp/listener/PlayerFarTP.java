package cn.whiteg.moetp.listener;

import cn.whiteg.mmocore.DataCon;
import cn.whiteg.mmocore.MMOCore;
import cn.whiteg.mmocore.listener.SafeNumEven;
import cn.whiteg.mmocore.util.YamlUtils;
import cn.whiteg.moetp.Event.PlayerFarTpEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.List;

public class PlayerFarTP implements Listener {
    @EventHandler(ignoreCancelled = true)
    public void tp(PlayerFarTpEvent event) {
        DataCon dc = MMOCore.getPlayerData(event.getPlayer());
        YamlUtils.setLocation(dc.getConfig(),"Player.Back",event.getFrom());
        SafeNumEven.onEven(event.getPlayer(),80);

    }
    public void unreg(){
        PlayerFarTpEvent.getHandlerList().unregister(this);
    }
}
