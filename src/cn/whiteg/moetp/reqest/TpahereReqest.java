package cn.whiteg.moetp.reqest;

import cn.whiteg.mmocore.container.PlayerReqest;
import cn.whiteg.moetp.MoeTP;
import cn.whiteg.moetp.utils.EntityTpUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TpahereReqest extends PlayerReqest {
    private final Location loc;

    public TpahereReqest(Player player) {
        super(player,"tpahere");
        loc = getSender().getLocation();
    }

    @Override
    public void onAccept(CommandSender s) {
        if (s instanceof Player){
            Player player = (Player) s;
            Bukkit.getScheduler().runTaskLater(MoeTP.plugin,() -> {
                EntityTpUtils.PlayerTP(player,loc);
            },20);
            getSender().sendMessage(player.getDisplayName() + "§b已接受传送至阁下");
            player.sendMessage("§b正在传送");
        }
    }


}
