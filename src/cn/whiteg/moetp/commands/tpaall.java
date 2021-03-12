package cn.whiteg.moetp.commands;

import cn.whiteg.mmocore.common.HasCommandInterface;
import cn.whiteg.moetp.api.TpahereReqest;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class tpaall extends HasCommandInterface {

    @Override
    public boolean executo(CommandSender sender,Command cmd,String label,String[] args) {
        if (args.length == 0 && sender instanceof Player){
            Player p1 = (Player) sender;
            sender.sendMessage(" §b给所有玩家发送传送请求");
            new TpahereReqest(p1).sendAll();
            return true;
        } else {
            sender.sendMessage(getDescription());
        }
        return false;
    }

    @Override
    public List<String> complete(CommandSender sender,Command cmd,String label,String[] args) {
        if (args.length == 1){
            List<String> ls = new ArrayList<>();
            for (Player p : Bukkit.getOnlinePlayers()) {
                ls.add(p.getName());
            }
            ls.remove(sender.getName());
            return getMatches(args,ls);
        }
        return null;
    }

    @Override
    public boolean canUseCommand(CommandSender sender) {
        return sender.hasPermission("mmo.tpaall");
    }

    @Override
    public String getDescription() {
        return " §a/tpaall§b请求所有玩家传送至阁下";
    }
}
