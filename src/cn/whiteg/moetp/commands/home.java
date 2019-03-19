package cn.whiteg.moetp.commands;

import cmiLib.RawMessage;
import cn.whiteg.mmocore.CommandInterface;
import cn.whiteg.mmocore.DataCon;
import cn.whiteg.mmocore.MMOCore;
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

public class home extends CommandInterface {

    @Override
    public boolean onCommand(CommandSender sender,Command cmd,String label,String[] args) {
        if (!(sender instanceof Player)) return false;
        Player player = (Player) sender;
        if (args.length == 1){
            DataCon dc = MMOCore.getPlayerData(player);
            ConfigurationSection homes = dc.getConfig().getConfigurationSection("homes");
            if(homes == null) {
                return false;
            }
            Set<String> keys = homes.getKeys(false);
            sender.sendMessage("当前可用home有:");
            RawMessage rw = new RawMessage();
            for (String st : keys){
                rw.add(st + " "  , "点我传送至§9"+st , "home " + st);
            }
            rw.show(player ,true);
        } else if (args.length == 2){
            DataCon dc = MMOCore.getPlayerData(player);
            ConfigurationSection homes = dc.getConfig().getConfigurationSection("homes");
            if(homes == null) {
                return false;
            }
            if (!sender.hasPermission("mmo.home")) return false;
            ConfigurationSection cs = homes.getConfigurationSection(args[1]);
            if (cs == null){
                sender.sendMessage("§b传送点不存在");
                return true;
            }
            Location loc = YamlUtils.getLocation(cs);
            if (loc == null){
                sender.sendMessage("找不到home");
                return true;
            }
            EntityTpUtils.PlayerTP(player,loc);
            sender.sendMessage("传送到" + args[1]);
            return true;
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender,Command cmd,String label,String[] args) {
        if (args.length == 2){
            if (!(sender instanceof Player)) return null;
            Player player = (Player) sender;
            DataCon dc = MMOCore.getPlayerData(player);
            ConfigurationSection homes = dc.getConfig().getConfigurationSection("homes");
            if(homes == null) {
                return null;
            }
            List<String> warps = new ArrayList<>(homes.getKeys(false));
            return getMatches(args[1],warps);
        }
        return null;
    }
}
