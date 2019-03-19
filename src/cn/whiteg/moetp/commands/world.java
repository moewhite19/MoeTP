package cn.whiteg.moetp.commands;

import cn.whiteg.mmocore.common.HasCommandInterface;
import cn.whiteg.moetp.utils.EntityTpUtils;
import com.google.common.collect.ImmutableList;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class world extends HasCommandInterface {

    @Override
    public boolean executo(CommandSender sender,Command cmd,String label,String[] args) {
        if (args.length == 1){
            if (sender instanceof Player player){
                var world = Bukkit.getWorld(args[0]);
                if (world == null){
                    sender.sendMessage("找不到世界: " + args[0]);
                    return false;
                }
                EntityTpUtils.PlayerTP(player,world.getSpawnLocation());
            }
        } else {
            sender.sendMessage("无效参数");
            return false;
        }
        return true;
    }

    @Override
    public List<String> complete(CommandSender commandSender,Command command,String s,String[] args) {
        if (args.length <= 1){
            ImmutableList.Builder<String> builder = ImmutableList.builder();
            for (World world : Bukkit.getWorlds()) {
                builder.add(world.getName());
            }
            return getMatches(builder.build(),args);
        }
        return null;
    }

    @Override
    public boolean canUseCommand(CommandSender sender) {
        return sender.hasPermission("mmo.tpo");
    }

    @Override
    public String getDescription() {
        return "传送到指定世界";
    }
}
