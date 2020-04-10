package cn.whiteg.moetp.api;

import cn.whiteg.mmocore.container.PlayerReqest;
import cn.whiteg.moetp.MoeTP;
import cn.whiteg.moetp.utils.EntityTpUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class TpahereReqest extends PlayerReqest {
    private final Location loc;

    public TpahereReqest(Player player,Player player1) {
        super(player,player1,"tpahere");
        loc = getSender().getLocation();
    }

    @Override
    public void onAccept() {
        Bukkit.getScheduler().runTaskLater(MoeTP.plugin,() -> {
            EntityTpUtils.PlayerTP(getPlayer(),loc);
        },20);
        getSender().sendMessage(getPlayer().getDisplayName() + "§b已接受传送至阁下");
        getPlayer().sendMessage("§b正在传送");
    }


}
