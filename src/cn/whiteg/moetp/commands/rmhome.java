package cn.whiteg.moetp.commands;

import cn.whiteg.mmocore.DataCon;
import cn.whiteg.mmocore.MMOCore;
import cn.whiteg.mmocore.common.HasCommandInterface;
import cn.whiteg.mmocore.util.YamlUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class rmhome extends HasCommandInterface {

    @Override
    public boolean executo(CommandSender sender,Command cmd,String label,String[] args) {
        if (!(sender instanceof Player)) return false;
        Player player = (Player) sender;
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
    public List<String> complete(CommandSender sender,Command cmd,String label,String[] args) {
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

    @Override
    public String getDescription() {
        return "删除Home: <Home>";
    }


    @Override
    public boolean canUseCommand(CommandSender sender) {
        return sender.hasPermission("mmo.rmhome");
    }
}
