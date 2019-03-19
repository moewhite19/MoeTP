package cn.whiteg.moetp.commands;

import cn.whiteg.mmocore.common.HasCommandInterface;
import cn.whiteg.moetp.utils.EntityTpUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class top extends HasCommandInterface {

    @Override
    public boolean executo(CommandSender sender,Command cmd,String label,String[] args) {
        if (args.length == 0){
            if (sender instanceof Player){
                Location loc = ((Player) sender).getLocation();
                loc.setY(loc.getWorld().getHighestBlockYAt(loc) + 1D);
                EntityTpUtils.PlayerOnceTp((Player) sender,loc);
                sender.sendMessage("§b传送到顶部");
                return true;
            }
        } else if (args.length == 1){
            if (sender.hasPermission("mmo.top.other")){
                final Player player = Bukkit.getPlayer(args[0]);
                if (player == null){
                    sender.sendMessage("§b找不到玩家");
                    return true;
                }
                Location loc = player.getLocation();
                loc.setY(loc.getWorld().getHighestBlockYAt(loc) + 1D);
                EntityTpUtils.PlayerOnceTp(player,loc);
                sender.sendMessage("§b将 §f" + player.getName() + " §b传送到顶部");
            } else sender.sendMessage("阁下权限不足");
        }
        return false;
    }

    @Override
    public List<String> complete(CommandSender sender,Command cmd,String label,String[] args) {
        if (args.length == 1){
            List<String> list = getPlayersList(args);
            list.remove(sender.getName());
            return list;
        }
        return null;
    }

    @Override
    public boolean canUseCommand(CommandSender sender) {
        return sender.hasPermission("mmo.top.self");
    }

    @Override
    public String getDescription() {
        return "传送到顶部方块";
    }
}
