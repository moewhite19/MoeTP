package cn.whiteg.moetp.commands;

import cn.whiteg.mmocore.CommandInterface;
import cn.whiteg.mmocore.DataCon;
import cn.whiteg.mmocore.MMOCore;
import cn.whiteg.moetp.Setting;
import cn.whiteg.mmocore.util.YamlUtils;
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
            sethome(player,player.getLocation() ,"home");
        } else if (args.length == 2){
            sethome(player , player.getLocation() , args[1]);
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
    void sethome(Player player , Location location , String home){
        if(home.contains(".")){
            player.sendMessage("§b名字含非法字符");
            return;
        }
        DataCon dc = MMOCore.getPlayerData(player);
        ConfigurationSection homes = dc.getConfig().getConfigurationSection("homes");
        if (homes == null){
            homes = dc.createSection("homes");
        }
        int homesize = homes.getKeys(false).size();
        int m = dc.getConfig().getInt("MaxHomes" , Setting.PlayerMaxHomes);
        if(homesize >= m){
            player.sendMessage("§b你的home数量已达到上限");
            if(!player.hasPermission("whiteg.test")){
                return;
            }
        }
        ConfigurationSection cs = homes.createSection(home);
        YamlUtils.setLocation(cs,location);
        player.sendMessage("设置home " + home);
    }
}
