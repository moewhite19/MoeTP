package cn.whiteg.moetp.commands;

import cn.whiteg.mmocore.common.CommandInterface;
import cn.whiteg.moetp.Setting;
import cn.whiteg.moetp.utils.WarpManager;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;


public class setwarp extends CommandInterface {

    @Override
    public boolean onCommand(CommandSender sender,Command cmd,String label,String[] args) {
        if (sender instanceof Player){
            if (sender.hasPermission("mmo.setwarp")){
                Player player = (Player) sender;
                Location loc = player.getLocation();
                if (args.length == 2){
                    if (args[1].contains(".")){
                        player.sendMessage("§b名字含非法字符");
                        return true;
                    }
                    WarpManager.setWarp(args[1],loc);
                    sender.sendMessage("§b设置传送点成功");
                } else {
                    sender.sendMessage("参数错误");
                }
            } else {
                sender.sendMessage("阁下没有权限");
            }
            return true;
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender,Command cmd,String label,String[] args) {
        if (args.length == 2){
            List<String> warps = new ArrayList<>(Setting.warps.getKeys(false));
            return getMatches(args[1],warps);
        }
        return null;
    }
}
