package cn.whiteg.moetp.commands;

import cn.whiteg.mmocore.common.HasCommandInterface;
import cn.whiteg.moetp.utils.EntityTpUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class tpo extends HasCommandInterface {

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
            p1.sendMessage("§b传送" + p2.getDisplayName());
            EntityTpUtils.PlayerOnceTp(p1,p2.getLocation());
            p2.sendMessage("§b正在传送至" + p1.getDisplayName());
        } else if (args.length == 2){
            Player p1 = Bukkit.getPlayer(args[0]);
            Player p2 = Bukkit.getPlayer(args[1]);
            if (p1 == null || p2 == null){
                sender.sendMessage("§b找不到玩家");
                return true;
            }
            if (p1 == p2) return true;
            sender.sendMessage("§b将 §r" + p1.getDisplayName() + "§b传送至 " + p2.getDisplayName());
            p1.sendMessage("§b传送§r" + p2.getDisplayName());
            EntityTpUtils.PlayerOnceTp(p1,p2.getLocation());
            p2.sendMessage("§b正在传送至§r" + p1.getDisplayName());
        } else {
            sender.sendMessage("§a/tpo <玩家id>§b传送至玩家");
        }
        return true;
    }

    @Override
    public boolean canUseCommand(CommandSender sender) {
        return sender.hasPermission("mmo.tpo");
    }

    @Override
    public String getDescription() {
        return "强制传送";
    }
}
