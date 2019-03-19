package cn.whiteg.moetp.commands;

import cn.whiteg.mmocore.DataCon;
import cn.whiteg.mmocore.MMOCore;
import cn.whiteg.mmocore.common.HasCommandInterface;
import cn.whiteg.moetp.utils.PlayerHomeManage;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class sethome extends HasCommandInterface {

    @Override
    public boolean executo(CommandSender sender,Command cmd,String label,String[] args) {
        if (!(sender instanceof Player player)) return false;
        DataCon dc = MMOCore.getPlayerData(player);
        try{
            if (args.length == 0){
                PlayerHomeManage.setHome(dc,"home",player.getLocation(),player);
            } else if (args.length == 1){
                String name = args[0];
                PlayerHomeManage.setHome(dc,name,player.getLocation(),player);
            }
        }catch (PlayerHomeManage.InvalidHomeException e){
            sender.sendMessage(e.getMessage());
            return false;
        }
        return true;
    }

    @Override
    public List<String> complete(CommandSender sender,Command cmd,String label,String[] args) {
        if (args.length == 1){
            if (!(sender instanceof Player player)) return null;
            DataCon dc = MMOCore.getPlayerData(player);
            return getMatches(args,new ArrayList<>(PlayerHomeManage.getHomes(dc)));
        }
        return null;
    }

    @Override
    public boolean canUseCommand(CommandSender sender) {
        return sender.hasPermission("mmo.sethome");
    }

    @Override
    public String getDescription() {
        return "设置Home:§7 <Home>";
    }
}
