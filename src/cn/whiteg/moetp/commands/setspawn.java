package cn.whiteg.moetp.commands;

import cn.whiteg.mmocore.CommandInterface;
import cn.whiteg.moetp.Setting;
import cn.whiteg.mmocore.util.YamlUtils;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.List;

import static cn.whiteg.moetp.Setting.saveWarps;

public class setspawn extends CommandInterface {

    @Override
    public boolean onCommand(CommandSender sender,Command cmd,String label,String[] args) {
        if (sender instanceof Player){
            if (sender.hasPermission("mmo.setspawn")){
                Player player = (Player) sender;
                Setting.spawnLoc = player.getLocation();
                player.getWorld().setSpawnLocation(Setting.spawnLoc);
                Location loc = player.getLocation();
                ConfigurationSection cs = Setting.warps.createSection("spawn");
                YamlUtils.setLocation(cs,loc);
                saveWarps();
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
