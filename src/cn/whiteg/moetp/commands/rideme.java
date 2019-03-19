package cn.whiteg.moetp.commands;

import cn.whiteg.mmocore.common.CommandInterface;
import cn.whiteg.moetp.reqest.RidemeReqest;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class rideme extends CommandInterface {

    @Override
    public boolean onCommand(CommandSender sender,Command cmd,String label,String[] args) {
        if (args.length == 1 && sender instanceof Player){
            if (sender.hasPermission("rpgarmour.rideme")){
                Player p1 = (Player) sender;
                Player p2 = Bukkit.getPlayer(args[0]);
                if (p2 == null){
                    sender.sendMessage("§b找不到玩家");
                    return true;
                }
                if (p1 == p2) return true;
                new RidemeReqest(p1).sendTo(p2);
            }
        } else {
            sender.sendMessage("§a/rideme <玩家id>§b请求骑乘");
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender,Command cmd,String label,String[] args) {
        if (args.length == 1){
            List<String> list = PlayersList(args);
            list.remove(sender.getName());
            return list;
        }
        return null;
    }

    @Override
    public String getDescription() {
        return "请求骑乘:§7 <玩家id>";
    }
}
