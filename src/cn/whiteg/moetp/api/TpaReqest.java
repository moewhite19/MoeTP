package cn.whiteg.moetp.api;

import cn.whiteg.mmocore.container.PlayerReqest;
import cn.whiteg.moetp.MoeTP;
import cn.whiteg.moetp.utils.EntityTpUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class TpaReqest extends PlayerReqest {
    public TpaReqest(Player player,Player player1) {
        super(player,player1,"tpa");
    }

    @Override
    public void onAccept() {
        Bukkit.getScheduler().runTaskLater(MoeTP.plugin,() -> {
            if (getSender().isOnline() && getPlayer().isOnline())
                EntityTpUtils.PlayerTP(getSender(),getPlayer().getLocation());
        },40);
        getPlayer().sendMessage(getPlayer().getDisplayName() + "§b已接受传送请求");
        getSender().sendTitle("§b将会为你准备传送","§f请站在原地不要走动",10,30,10);
    }
}
