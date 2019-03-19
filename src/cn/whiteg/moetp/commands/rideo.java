package cn.whiteg.moetp.commands;

import cn.whiteg.mmocore.common.HasCommandInterface;
import cn.whiteg.moetp.utils.RideManage;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class rideo extends HasCommandInterface {


    @Override
    public boolean executo(CommandSender sender,Command cmd,String label,String[] args) {
        if (sender instanceof Player){
            if (args.length == 1){
                Player p = (Player) sender;
                Player o = Bukkit.getPlayer(args[0]);
                if (RideManage.Ride(p,o)){
                    sender.sendMessage("已骑上去");
                } else {
                    sender.sendMessage("未知原因没有骑上去");
                }
            }
            if (args.length == 2){
                Player p1 = Bukkit.getPlayer(args[0]);
                Player p2 = Bukkit.getPlayer(args[1]);
                if (p1 == null || p2 == null){
                    sender.sendMessage("§b玩家不存在");
                }
                if (RideManage.Ride(p1,p2)){
                    sender.sendMessage("已骑上去");
                } else {
                    sender.sendMessage("未知原因没有骑上去");
                }
            }
        }
        return false;
    }

    @Override
    public boolean canUseCommand(CommandSender sender) {
        return sender.hasPermission("whiteg.test");
    }

    @Override
    public String getDescription() {
        return "强制骑上玩家";
    }
}
