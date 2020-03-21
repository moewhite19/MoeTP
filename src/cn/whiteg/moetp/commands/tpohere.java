package cn.whiteg.moetp.commands;

import cn.whiteg.mmocore.common.CommandInterface;
import cn.whiteg.moetp.utils.EntityTpUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class tpohere extends CommandInterface {

    @Override
    public boolean onCommand(CommandSender sender,Command cmd,String label,String[] args) {
        if (args.length == 2 && sender instanceof Player){
            if (sender.hasPermission("mmo.tpohere")){
                Player p1 = (Player) sender;
                Player p2 = Bukkit.getPlayer(args[1]);
                if (p2 == null){
                    sender.sendMessage("§b找不到玩家");
                    return true;
                }
                if (p1 == p2) return true;
                p1.sendMessage("§b传送至" + p2.getDisplayName());
                EntityTpUtils.PlayerOnceTp(p2,p1.getLocation());
                p2.sendMessage(p1.getDisplayName() + "§b正在传送");
            }
        } else {
            sender.sendMessage("§a/tpo <玩家id>§b传送至玩家");
        }
        return true;
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
