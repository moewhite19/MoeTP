package cn.whiteg.moetp.commands;

import cn.whiteg.mmocore.DataCon;
import cn.whiteg.mmocore.CommandInterface;
import cn.whiteg.mmocore.MMOCore;
import cn.whiteg.mmocore.util.YamlUtils;
import cn.whiteg.moetp.utils.EntityTpUtils;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;

public class back extends CommandInterface {

    @Override
    public boolean onCommand(CommandSender sender,Command cmd,String label,String[] args) {
        if (!(sender instanceof Player)){
            sender.sendMessage("这个指令只有玩家能使用");
            return true;
        }
        if (!sender.hasPermission("mmo.back")){
            sender.sendMessage("§b阁下没有权限使用这个指令");
            return true;
        }
        Player player = (Player) sender;
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
    public List<String> onTabComplete(CommandSender sender,Command cmd,String label,String[] args) {
        return null;
    }
}
