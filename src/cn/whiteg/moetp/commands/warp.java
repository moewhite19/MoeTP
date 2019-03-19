package cn.whiteg.moetp.commands;

import cmiLib.RawMessage;
import cn.whiteg.mmocore.CommandInterface;
import cn.whiteg.moetp.Setting;
import cn.whiteg.mmocore.util.YamlUtils;
import cn.whiteg.moetp.utils.EntityTpUtils;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class warp extends CommandInterface {

    @Override
    public boolean onCommand(CommandSender sender,Command cmd,String label,String[] args) {
        if(!sender.hasPermission("mmo.warp")) return false;
        if (args.length == 1){
            Set<String> keys = Setting.warps.getKeys(false);
            sender.sendMessage("当前可用传送点有:");
            RawMessage rw = new RawMessage();
            for (String st : keys){
                rw.add( st + " "  , "点我传送至§9"+st , "warp " + st);
            }
            rw.show((Player) sender,true);
        } else if (args.length == 2){
            if (!(sender instanceof Player)) return false;
            ConfigurationSection cs = Setting.warps.getConfigurationSection(args[1]);
            if (cs == null){
                sender.sendMessage("§b传送点不存在");
                return true;
            }
            Location loc = YamlUtils.getLocation(cs);
            if (loc == null){
                sender.sendMessage("未知传送点");
                return true;
            }
            EntityTpUtils.PlayerTP((Player) sender,loc);
            sender.sendMessage("传送到" + cs.getString("displayname",args[1]));
            return true;
        }
        return false;
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
