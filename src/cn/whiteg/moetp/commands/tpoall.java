package cn.whiteg.moetp.commands;

import cn.whiteg.mmocore.common.CommandInterface;
import cn.whiteg.mmocore.common.HasCommandInterface;
import cn.whiteg.moetp.utils.EntityTpUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class tpoall extends HasCommandInterface {

    @Override
    public boolean executor(CommandSender sender,Command cmd,String label,String[] args) {
        if (args.length == 0 && sender instanceof Player){
            Player p1 = (Player) sender;
            p1.sendMessage("§b将所有人传送过来");
            Location loc = p1.getLocation();
            for (Player p2 : Bukkit.getOnlinePlayers()) {
                if (p1 == p2) continue;
                EntityTpUtils.PlayerOnceTp(p2,loc);
            }
        } else sender.sendMessage(getDescription());
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
        return "§a/tpoall §f将所有人传送过来";
    }

    @Override
    public boolean canUseCommand(CommandSender sender) {
        return sender.hasPermission("mmo.tpohere");
    }
}
