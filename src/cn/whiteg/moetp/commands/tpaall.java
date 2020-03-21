package cn.whiteg.moetp.commands;

import cn.whiteg.mmocore.common.CommandInterface;
import cn.whiteg.moetp.api.TpahereReqest;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class tpaall extends CommandInterface {

    @Override
    public boolean onCommand(CommandSender sender,Command cmd,String label,String[] args) {
        if (args.length == 1 && sender instanceof Player){
            if (sender.hasPermission("mmo.tpaall")){
                Player p1 = (Player) sender;
                sender.sendMessage("§b给所有玩家发送传送请求");
                for (Player p2 : Bukkit.getOnlinePlayers()) {
                    if (p1 == p2) continue;
                    new TpahereReqest(p2,p1).send();
                }
            } else {
                sender.sendMessage("§b阁下没有权限使用这个指令");
            }
        } else {
            sender.sendMessage("§a/tpaall§b请求所有玩家传送至阁下");
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender,Command cmd,String label,String[] args) {
        if (args.length == 2){
            List<String> ls = new ArrayList<>();
            for (Player p : Bukkit.getOnlinePlayers()) {
                ls.add(p.getName());
            }
            ls.remove(sender.getName());
            return getMatches(args[1],ls);
        }
        return null;
    }
}
