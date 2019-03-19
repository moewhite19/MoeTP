package cn.whiteg.moetp.reqest;

import cn.whiteg.mmocore.container.PlayerReqest;
import cn.whiteg.moetp.MoeTP;
import cn.whiteg.moetp.utils.EntityTpUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TpaReqest extends PlayerReqest {
    public TpaReqest(Player player) {
        super(player,"tpa");
    }

    @Override
    public void onAccept(CommandSender s) {
        if (s instanceof Player){
            Player p = ((Player) s);
            Bukkit.getScheduler().runTaskLater(MoeTP.plugin,() -> {
                if (getSender().isOnline() && p.isOnline())
                    EntityTpUtils.PlayerTP(getSender(),p.getLocation());
            },40);
            p.sendMessage(p.getDisplayName() + "§b已接受传送请求");
            getSender().sendTitle("§b将会为你准备传送","§f请站在原地不要走动",10,30,10);
        }
    }
}
