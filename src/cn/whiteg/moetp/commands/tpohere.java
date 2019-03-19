package cn.whiteg.moetp.commands;

import cn.whiteg.mmocore.common.HasCommandInterface;
import cn.whiteg.moetp.utils.EntityTpUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class tpohere extends HasCommandInterface {

    @Override
    public boolean executo(CommandSender sender,Command cmd,String label,String[] args) {
        if (args.length == 1 && sender instanceof Player){
            Player p1 = (Player) sender;
            Player p2 = Bukkit.getPlayer(args[0]);
            if (p2 == null){
                sender.sendMessage("§b找不到玩家");
                return true;
            }
            if (p1 == p2) return true;
            p1.sendMessage("§b传送至" + p2.getDisplayName());
            EntityTpUtils.PlayerOnceTp(p2,p1.getLocation());
            p2.sendMessage(p1.getDisplayName() + "§b正在传送");
        } else {
            sender.sendMessage(getDescription());
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender,Command cmd,String label,String[] args) {
        if (args.length == 1){
            List<String> list = getPlayersList(args,sender);
            list.remove(sender.getName());
            return list;
        }
        return null;
    }

    @Override
    public String getDescription() {
        return "将玩家传送过来";
    }

    @Override
    public boolean canUseCommand(CommandSender sender) {
        return sender.hasPermission("mmo.tpohere");
    }
}
