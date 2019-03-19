package cn.whiteg.moetp.commands;

import cn.whiteg.mmocore.CommandInterface;
import cn.whiteg.moetp.Setting;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

import static cn.whiteg.moetp.Setting.saveWarps;

public class rmwarp extends CommandInterface {

    @Override
    public boolean onCommand(CommandSender sender,Command cmd,String label,String[] args) {
        if (sender.hasPermission("mmo.setwarp")){
            if (args.length == 2){
                Setting.warps.set(args[1],null);
                saveWarps();
                sender.sendMessage("§b移除传送点成功");
            } else {
                sender.sendMessage("参数错误");
            }
        } else {
            sender.sendMessage("阁下没有权限");
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender,Command cmd,String label,String[] args) {
        if (args.length == 2){
            List<String> warps = new ArrayList<>(Setting.warps.getKeys(false));
            return getMatches(args[1] , warps);
        }
        return null;
    }
}
