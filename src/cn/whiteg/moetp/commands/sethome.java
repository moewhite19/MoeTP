package cn.whiteg.moetp.commands;

import cn.whiteg.mmocore.common.CommandInterface;
import cn.whiteg.mmocore.DataCon;
import cn.whiteg.mmocore.MMOCore;
import cn.whiteg.mmocore.util.YamlUtils;
import cn.whiteg.moetp.Setting;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class sethome extends CommandInterface {

    @Override
    public boolean onCommand(CommandSender sender,Command cmd,String label,String[] args) {
        if (!(sender instanceof Player)) return false;
        Player player = (Player) sender;
        if (!sender.hasPermission("mmo.sethome")) return false;
        if (args.length == 1){
            sethome(player,player.getLocation(),"home");
        } else if (args.length == 2){
            sethome(player,player.getLocation(),args[1]);
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
            if (homes == null){
                return null;
            }
            List<String> warps = new ArrayList<>(homes.getKeys(false));
            return getMatches(args[1],warps);
        }
        return null;
    }

    void sethome(Player player,Location location,String home) {
        if (home.contains(".")){
            player.sendMessage("§b名字含非法字符");
            return;
        }
        DataCon dc = MMOCore.getPlayerData(player);
        ConfigurationSection homes = dc.getConfig().getConfigurationSection("homes");
        if (homes == null){
            homes = dc.createSection("homes");
        }
        int homesize = homes.getKeys(false).size();
        int maxhome = dc.getConfig().getInt("Player.ExtraHome",0) + Setting.PlayerMaxHomes;
        ConfigurationSection cs = homes.getConfigurationSection(home);

        if (cs != null && homesize <= maxhome){
            YamlUtils.setLocation(cs,location);
            player.sendMessage("覆盖home " + home);
            dc.onSet();
        } else if (homesize < maxhome){
            cs = homes.createSection(home);
            YamlUtils.setLocation(cs,location);
            player.sendMessage("设置home " + home);
            dc.onSet();
        } else {
            player.sendMessage("§b你的home数量已达到上限");
        }

    }
}
