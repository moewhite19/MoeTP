package cn.whiteg.moetp.commands;

import cn.whiteg.mmocore.common.CommandInterface;
import cn.whiteg.mmocore.container.PlayerReqest;
import cn.whiteg.moetp.CommandManage;
import cn.whiteg.moetp.api.TpaReqest;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class tpa extends CommandInterface {

    @Override
    public boolean onCommand(CommandSender sender,Command cmd,String label,String[] args) {
        if (args.length == 2 && sender instanceof Player){
            if (sender.hasPermission("mmo.tpa")){
                final Player send = (Player) sender;
                final Player p = Bukkit.getPlayer(args[1]);
                if (p == null){
                    sender.sendMessage("§b找不到玩家");
                    return true;
                }
                if (send == p) return true;
                final PlayerReqest reqest = new TpaReqest(send);
                reqest.sendTo(p);
            }
        } else {
            sender.sendMessage("§a/tpa <玩家id>§b请求传送至玩家");
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender,Command cmd,String label,String[] args) {
        if (args.length == 2){
            return CommandManage.PlayersList(args[1]);
        }
        return null;
    }
}
