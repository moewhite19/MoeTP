package cn.whiteg.moetp.commands;

import cn.whiteg.mmocore.common.HasCommandInterface;
import cn.whiteg.moetp.Setting;
import cn.whiteg.moetp.utils.WarpManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;


public class rmwarp extends HasCommandInterface {

    @Override
    public boolean executo(CommandSender sender,Command cmd,String label,String[] args) {
        if (args.length == 1){
            WarpManager.removeWarp(args[0]);
            sender.sendMessage("§b移除传送点成功");
        } else {
            sender.sendMessage("参数错误");
        }
        return true;
    }

    @Override
    public List<String> complete(CommandSender sender,Command cmd,String label,String[] args) {
        if (args.length == 1){
            List<String> warps = new ArrayList<>(Setting.warps.getKeys(false));
            return getMatches(args[0],warps);
        }
        return null;
    }

    @Override
    public boolean canUseCommand(CommandSender sender) {
        return sender.hasPermission("mmo.setwarp");
    }

    @Override
    public String getDescription() {
        return "移除传送点: <传送点>";
    }
}
