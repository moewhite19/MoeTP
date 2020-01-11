package cn.whiteg.moetp.commands;

import cn.whiteg.mmocore.CommandInterface;
import cn.whiteg.moetp.utils.WarpManager;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class setspawn extends CommandInterface {

    @Override
    public boolean onCommand(CommandSender sender,Command cmd,String label,String[] args) {
        if (sender instanceof Player){
            if (sender.hasPermission("mmo.setspawn")){
                Player player = (Player) sender;
                Location loc = player.getLocation();
                WarpManager.setWarp("spawn",loc);
                sender.sendMessage("§b已设置出生点");
            } else {
                sender.sendMessage("阁下没有权限");
            }
            return true;
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender,Command cmd,String label,String[] args) {
        return null;
    }
}
