package cn.whiteg.moetp.commands;

import cn.whiteg.mmocore.CommandInterface;
import cn.whiteg.moetp.Setting;
import cn.whiteg.moetp.utils.EntityTpUtils;
import org.bukkit.Bukkit;
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
                if (sender.hasPermission("mmo.spawn")){
                    EntityTpUtils.PlayerTP((Player) sender,Setting.spawnLoc);
                    sender.sendMessage("§b回到出生点");
                } else {
                    sender.sendMessage("阁下没有权限");
                }
                return true;
            }
        } else if (args.length == 2){
            if (sender.hasPermission("mmo.tp.o")){
                Player player = Bukkit.getPlayer(args[1]);
                if (player == null){
                    sender.sendMessage("§b找不到玩家");
                }
                player.teleport(Setting.spawnLoc);
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
