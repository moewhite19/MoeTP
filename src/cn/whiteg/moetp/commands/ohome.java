package cn.whiteg.moetp.commands;

import cn.whiteg.mmocore.common.CommandInterface;
import cn.whiteg.mmocore.DataCon;
import cn.whiteg.mmocore.MMOCore;
import cn.whiteg.mmocore.util.YamlUtils;
import cn.whiteg.moetp.utils.EntityTpUtils;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ohome extends CommandInterface {

    @Override
    public boolean onCommand(CommandSender sender,Command cmd,String label,String[] args) {
        if (!sender.hasPermission("mmo.otherhome")) return false;
        if (!(sender instanceof Player)) return false;
        Player player = (Player) sender;
        if (args.length == 2){
            DataCon dc = MMOCore.getPlayerData(args[1]);
            if (dc == null){
                sender.sendMessage("找不到玩家");
                return false;
            }
            ConfigurationSection homes = dc.getConfig().getConfigurationSection("homes");
            if (homes == null){
                sender.sendMessage(dc.getName() + "当前还没有home");
                return false;
            }
            Set<String> keys = homes.getKeys(false);
            if (keys.isEmpty()){
                sender.sendMessage(dc.getName() + "当前没有home");
                return false;
            }
            ComponentBuilder cb = new ComponentBuilder("当前可用home有:\n");
            for (String st : keys) {
                cb.append(st + " ").event(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND,"/ohome " + dc.getName() + ' ' + st)).event(new HoverEvent(HoverEvent.Action.SHOW_TEXT,new ComponentBuilder("点我传送至§9" + st).create()));
            }
            player.spigot().sendMessage(cb.create());
        } else if (args.length == 3){
            DataCon dc = MMOCore.getPlayerData(args[1]);
            if (dc == null){
                sender.sendMessage("找不到玩家");
                return false;
            }
            ConfigurationSection homes = dc.getConfig().getConfigurationSection("homes");
            if (homes == null){
                return false;
            }
            ConfigurationSection cs = homes.getConfigurationSection(args[2]);
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
            sender.sendMessage("传送到" + args[2]);
            return true;
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender,Command cmd,String label,String[] args) {
        if (args.length == 3){
            if (!sender.hasPermission("mmo.otherhome")) return null;
            if (!(sender instanceof Player)) return null;
            Player player = (Player) sender;
            DataCon dc = MMOCore.getPlayerData(args[1]);
            if (dc == null){
                sender.sendMessage("找不到玩家");
                return null;
            }
            ConfigurationSection homes = dc.getConfig().getConfigurationSection("homes");
            if (homes == null){
                return null;
            }
            List<String> warps = new ArrayList<>(homes.getKeys(false));
            return getMatches(args[2],warps);
        }
        return null;
    }
}
