package cn.whiteg.moetp.commands;

import cn.whiteg.mmocore.common.HasCommandInterface;
import cn.whiteg.mmocore.container.PlayerReqest;
import cn.whiteg.moetp.reqest.TpaReqest;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class tpa extends HasCommandInterface {

    @Override
    public boolean executo(CommandSender sender,Command cmd,String label,String[] args) {
        if (args.length == 1 && sender instanceof Player){
            final Player send = (Player) sender;
            final Player p = Bukkit.getPlayer(args[0]);
            if (p == null){
                sender.sendMessage("§b找不到玩家");
                return true;
            }
            if (send == p) return true;
            final PlayerReqest reqest = new TpaReqest(send);
            reqest.sendTo(p);
        } else {
            sender.sendMessage("§a/tpa <玩家id>§b请求传送至玩家");
        }
        return true;
    }

    @Override
    public List<String> complete(CommandSender sender,Command cmd,String label,String[] args) {
        if (args.length == 1){
            List<String> list = getPlayersList(args,sender);
            list.remove(sender.getName());
            return list;
        }
        return null;
    }

    @Override
    public boolean canUseCommand(CommandSender sender) {
        return sender.hasPermission("mmo.tpa");
    }

    @Override
    public String getDescription() {
        return "请求传送";
    }
}
