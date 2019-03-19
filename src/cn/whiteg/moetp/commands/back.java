package cn.whiteg.moetp.commands;

import cn.whiteg.mmocore.DataCon;
import cn.whiteg.mmocore.MMOCore;
import cn.whiteg.mmocore.common.HasCommandInterface;
import cn.whiteg.mmocore.util.YamlUtils;
import cn.whiteg.moetp.utils.EntityTpUtils;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class back extends HasCommandInterface {

    @Override
    public boolean executo(CommandSender sender,Command cmd,String label,String[] args) {
        if (!(sender instanceof Player player)){
            sender.sendMessage("这个指令只有玩家能使用");
            return true;
        }
        DataCon dc = MMOCore.getPlayerData(player);

        Location loc = YamlUtils.getLocation(dc.getConfig(),"Player.Back");
        if (loc == null){
            sender.sendMessage("无效位置");
            return true;
        }
        sender.sendMessage("§b正在回到上个位置");
        EntityTpUtils.PlayerTP(player,loc);
        return true;
    }

    @Override
    public List<String> complete(CommandSender sender,Command cmd,String label,String[] args) {
        return null;
    }

    @Override
    public String getDescription() {
        return "回到传送前的位置";
    }

    @Override
    public boolean canUseCommand(CommandSender sender) {
        return sender.hasPermission("mmo.back");
    }
}
