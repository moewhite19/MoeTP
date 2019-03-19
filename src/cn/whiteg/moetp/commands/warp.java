package cn.whiteg.moetp.commands;

import cn.whiteg.mmocore.common.HasCommandInterface;
import cn.whiteg.moetp.Setting;
import cn.whiteg.moetp.utils.EntityTpUtils;
import cn.whiteg.moetp.utils.WarpManager;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class warp extends HasCommandInterface {

    @Override
    public boolean executo(CommandSender sender,Command cmd,String label,String[] args) {
        if (args.length == 0){
            ComponentBuilder cb = new ComponentBuilder("当前可用warp有:\n");
            for (String st : WarpManager.getWarps()) {
                cb.append(st + " ").event(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND,"/warp " + st)).event(new HoverEvent(HoverEvent.Action.SHOW_TEXT,new Text("点我传送至§9" + st)));
            }
            sender.spigot().sendMessage(cb.create());
        } else if (args.length == 1){
            if (!(sender instanceof Player)) return false;
            Location loc = WarpManager.getWarp(args[0]);
            if (loc == null){
                sender.sendMessage("未知传送点");
                return true;
            }
            EntityTpUtils.PlayerTP((Player) sender,loc);
            sender.sendMessage("传送到" + args[0]);
            return true;
        } else if (args.length == 2){
            if (!sender.hasPermission("mmo.tpo")) return false;
            Player player = Bukkit.getPlayer(args[1]);
            if (player == null) return false;
            Location loc = WarpManager.getWarp(args[0]);
            if (loc == null){
                sender.sendMessage("未知传送点");
                return true;
            }
            EntityTpUtils.PlayerOnceTp(player,loc);
            sender.sendMessage(sender + "§b将你传送到§f" + args[0]);
            return true;
        }
        return false;
    }

    @Override
    public List<String> complete(CommandSender sender,Command cmd,String label,String[] args) {
        if (args.length == 1){
            List<String> warps = new ArrayList<>(Setting.warps.getKeys(false));
            return getMatches(args,warps);
        }
        return null;
    }

    @Override
    public boolean canUseCommand(CommandSender sender) {
        return sender.hasPermission("mmo.warp");
    }

    @Override
    public String getDescription() {
        return "传送到Warp:§7 [warp]";
    }
}
