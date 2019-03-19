package cn.whiteg.moetp.commands;

import cn.whiteg.mmocore.CommandInterface;
import cn.whiteg.mmocore.MainCommand;
import cn.whiteg.mmocore.api.ConfirmContainer;
import cn.whiteg.mmocore.api.ConfirmManage;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class confirm extends CommandInterface {

    @Override
    public boolean onCommand(CommandSender sender,Command cmd,String label,String[] args) {
        if (sender instanceof Player){
            boolean r = false;
            if (args.length == 1){
                r = ConfirmManage.runTask((Player) sender);
            } else if (args.length == 2){
                r = ConfirmManage.runTask((Player) sender,args[1]);
            }
            if (!r){
                sender.sendMessage("§b没有待确认事件");
            }
            return true;
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender,Command cmd,String label,String[] args) {
        if (sender instanceof Player){
            if (args.length == 2){
                ConfirmContainer ce = ConfirmManage.confirmMap.get(sender.getName());
                if (ce == null) return null;
                return MainCommand.getMatches(args[1],new ArrayList<>(ce.getKeys()));
            }
        }
        return null;
    }
}
