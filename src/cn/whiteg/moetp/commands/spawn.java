package cn.whiteg.moetp.commands;

import cn.whiteg.mmocore.common.CommandInterface;
import cn.whiteg.moetp.utils.EntityTpUtils;
import cn.whiteg.moetp.utils.WarpManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class spawn extends CommandInterface {

    @Override
    public boolean onCommand(CommandSender sender,Command cmd,String label,String[] args) {
        if (args.length == 1){
            if (sender instanceof Player){
                if (!sender.hasPermission("mmo.warp")) return false;
                EntityTpUtils.PlayerTP((Player) sender,WarpManager.getSpawn());
                sender.sendMessage("§b传送到出生点");
                return true;
            }
        } else if (args.length == 2){
            if (sender.hasPermission("mmo.onFarTp.other")){
                final Player player = Bukkit.getPlayer(args[1]);
                if (player == null){
                    sender.sendMessage("§b找不到玩家");
                    return true;
                }
                final Location loc = WarpManager.getSpawn();
                if (loc == null){
                    sender.sendMessage("未知传送点");
                    return true;
                }
                EntityTpUtils.PlayerOnceTp(player,loc);
//                EntityTpUtils.PlayerTpNoCd(player,loc);
                sender.sendMessage("§b将 §f" + player.getName() + " §b传送到出生点");
            }
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

            return getMatches(args[1],ls);
        }
        return null;
    }
}
