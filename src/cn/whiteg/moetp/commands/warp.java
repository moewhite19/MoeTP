package cn.whiteg.moetp.commands;

import cn.whiteg.mmocore.common.CommandInterface;
import cn.whiteg.moetp.Setting;
import cn.whiteg.moetp.utils.EntityTpUtils;
import cn.whiteg.moetp.utils.WarpManager;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class warp extends CommandInterface {

    @Override
    public boolean onCommand(CommandSender sender,Command cmd,String label,String[] args) {
        if (!sender.hasPermission("mmo.warp")) return false;
        if (args.length == 1){
            Set<String> keys = Setting.warps.getKeys(false);
//            sender.sendMessage("当前可用传送点有:");
            ComponentBuilder cb = new ComponentBuilder("当前可用warp有:\n");
            for (String st : keys) {
                cb.append(st + " ").event(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND,"/warp " + st)).event(new HoverEvent(HoverEvent.Action.SHOW_TEXT,new ComponentBuilder("点我传送至§9" + st).create()));
            }
            sender.spigot().sendMessage(cb.create());
        } else if (args.length == 2){
            if (!(sender instanceof Player)) return false;
            Location loc = WarpManager.getWarp(args[1]);
            if (loc == null){
                sender.sendMessage("未知传送点");
                return true;
            }
            EntityTpUtils.PlayerTP((Player) sender,loc);
            sender.sendMessage("传送到" + args[1]);
            return true;
        } else if (args.length == 4){
            if (!sender.hasPermission("mmo.tpo")) return false;
            Player player = Bukkit.getPlayer(args[3]);
            if (player == null) return false;
            Location loc = WarpManager.getWarp(args[1]);
            if (loc == null){
                sender.sendMessage("未知传送点");
                return true;
            }
            EntityTpUtils.PlayerOnceTp(player,loc);
            sender.sendMessage("传送到" + args[1]);
            return true;
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender,Command cmd,String label,String[] args) {
        if (args.length == 2){
            List<String> warps = new ArrayList<>(Setting.warps.getKeys(false));
            return getMatches(args[1],warps);
        }
        return null;
    }
}
