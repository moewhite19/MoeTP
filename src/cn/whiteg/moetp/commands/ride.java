package cn.whiteg.moetp.commands;

import cn.whiteg.mmocore.common.HasCommandInterface;
import cn.whiteg.moetp.reqest.RideReqest;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class ride extends HasCommandInterface {

    @Override
    public boolean executo(CommandSender sender,Command cmd,String label,String[] args) {
        if (args.length == 1 && sender instanceof Player p1){
            Player p2 = Bukkit.getPlayer(args[0]);
            if (p2 == null){
                sender.sendMessage("§b找不到玩家");
                return true;
            }
            if (p1 == p2) return true;
            new RideReqest(p1).sendTo(p2);
        } else {
            sender.sendMessage("§a/ride <玩家id>§b请求骑乘");
        }
        return true;
    }

    @Override
    public List<String> complete(CommandSender sender,Command cmd,String label,String[] args) {
        if (args.length == 1){
            List<String> list = PlayersList(args);
            list.remove(sender.getName());
            return list;
        }
        return null;
    }

    @Override
    public boolean canUseCommand(CommandSender sender) {
        return sender.hasPermission("rpgarmour.ride");
    }

    @Override
    public String getDescription() {
        return "请求骑上玩家:§7 <玩家id>";
    }
}
