package cn.whiteg.moetp.commands;

import cn.whiteg.mmocore.DataCon;
import cn.whiteg.mmocore.MMOCore;
import cn.whiteg.mmocore.common.HasCommandInterface;
import cn.whiteg.moetp.utils.EntityTpUtils;
import cn.whiteg.moetp.utils.PlayerHomeManage;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class home extends HasCommandInterface {
    @Override
    public boolean executo(CommandSender sender,Command cmd,String label,String[] args) {
        if (!(sender instanceof Player)) return false;
        Player player = (Player) sender;
        if (args.length == 0){
            PlayerHomeManage.showHome(MMOCore.getPlayerData(player),player);
        } else if (args.length == 1){
            DataCon dc = MMOCore.getPlayerData(player);
            try{
                Location home = PlayerHomeManage.getPlayerHome(dc,args[0]);
                EntityTpUtils.PlayerTP(player,home);
                sender.sendMessage(" §b传送到 §f" + args[0]);
            }catch (PlayerHomeManage.InvalidHomeException e){
                sender.sendMessage(e.getMessage());
                return false;
            }
        }
        return true;
    }

    @Override
    public List<String> complete(CommandSender sender,Command cmd,String label,String[] args) {
        if (args.length == 1){
            if (!(sender instanceof Player)) return null;
            Player player = (Player) sender;
            DataCon dc = MMOCore.getPlayerData(player);
            return getMatches(new ArrayList<>(PlayerHomeManage.getHomes(dc)),args);
        }
        return null;
    }

    @Override
    public boolean canUseCommand(CommandSender sender) {
        return sender.hasPermission("mmo.home");
    }

    @Override
    public String getDescription() {
        return "传送到Home: [Home]";
    }
}
