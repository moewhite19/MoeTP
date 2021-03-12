package cn.whiteg.moetp.commands;

import cn.whiteg.mmocore.DataCon;
import cn.whiteg.mmocore.MMOCore;
import cn.whiteg.mmocore.common.CommandInterface;
import cn.whiteg.mmocore.util.YamlUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class rmhome extends CommandInterface {

    @Override
    public boolean onCommand(CommandSender sender,Command cmd,String label,String[] args) {
        if (!(sender instanceof Player)) return false;
        Player player = (Player) sender;
        if (!sender.hasPermission("mmo.rmhome")) return false;
        if (args.length == 0){
            DataCon dc = MMOCore.getPlayerData(player);
            ConfigurationSection homes = dc.getConfig().getConfigurationSection("homes");
            if (homes == null) return false;
            YamlUtils.setLocation(homes.createSection("home"),player.getLocation());
        } else if (args.length == 1){
            DataCon dc = MMOCore.getPlayerData(player);
            ConfigurationSection homes = dc.getConfig().getConfigurationSection("homes");
            homes.set(args[0],null);
            sender.sendMessage("删除home " + args[0]);
            dc.onSet();
            return true;
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender,Command cmd,String label,String[] args) {
        if (args.length == 1){
            if (!(sender instanceof Player)) return null;
            Player player = (Player) sender;
            DataCon dc = MMOCore.getPlayerData(player);
            ConfigurationSection homes = dc.getConfig().getConfigurationSection("homes");
            if (homes == null) return null;
            List<String> list = new ArrayList<>(homes.getKeys(false));
            return getMatches(args,list);
        }
        return null;
    }
}
