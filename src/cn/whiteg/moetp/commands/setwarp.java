package cn.whiteg.moetp.commands;

import cn.whiteg.mmocore.common.HasCommandInterface;
import cn.whiteg.moetp.Setting;
import cn.whiteg.moetp.utils.WarpManager;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;


public class setwarp extends HasCommandInterface {

    @Override
    public boolean executo(CommandSender sender,Command cmd,String label,String[] args) {
        if (sender instanceof Player){
            Player player = (Player) sender;
            Location loc = player.getLocation();
            if (args.length == 1){
                try{
                    WarpManager.setWarp(args[0],loc);
                    sender.sendMessage("§b设置传送点成功");
                }catch (IllegalArgumentException exception){
                    sender.sendMessage(exception.getMessage());
                }
            } else {
                sender.sendMessage("参数错误");
            }
            return true;
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender,Command cmd,String label,String[] args) {
        if (args.length == 1){
            List<String> warps = new ArrayList<>(Setting.warps.getKeys(false));
            return getMatches(args,warps);
        }
        return null;
    }

    @Override
    public boolean canUseCommand(CommandSender sender) {
        return sender.hasPermission("mmo.setwarp");
    }

    @Override
    public String getDescription() {
        return "设置Warp:§7 <warp>";
    }
}
