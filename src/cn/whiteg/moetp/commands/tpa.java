package cn.whiteg.moetp.commands;

import cn.whiteg.mmocore.CommandInterface;
import cn.whiteg.mmocore.api.ConfirmManage;
import cn.whiteg.moetp.CommandManage;
import cn.whiteg.moetp.utils.EntityTpUtils;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class tpa extends CommandInterface {

    @Override
    public boolean onCommand(CommandSender sender,Command cmd,String label,String[] args) {
        if (args.length == 2 && sender instanceof Player){
            if (sender.hasPermission("mmo.tpa")){
                Player p1 = (Player) sender;
                Player p2 = Bukkit.getPlayer(args[1]);
                if (p2 == null){
                    sender.sendMessage("§b找不到玩家");
                    return true;
                }
                if (p1 == p2) return true;
                String confirmID = "tpa@" + sender.getName();
                p1.sendMessage("§b已发送传送请求至" + p2.getDisplayName());
                TextComponent a1 = new TextComponent(" §f%sender% §7请求传送到阁下这里来 §3§l>>§b§l点我接受传送§3§l<< ".replace("%sender%",p1.getDisplayName()).replace("&","§"));
                a1.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,"/confirm " + confirmID));
                a1.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,new ComponentBuilder("§3点我或者使用指令§a/c " + confirmID + "§3接受传送").color(ChatColor.BLUE).create()));
                p2.spigot().sendMessage(a1);
                ConfirmManage.addEvent(p2,confirmID,() -> {
                    EntityTpUtils.PlayerTP(p1,p2.getLocation());
                    p1.sendMessage(p2.getDisplayName() + "§r§b已接受传送");
                    p2.sendMessage(p1.getDisplayName() + "§b正在传送");
                });
            }
        }else {
            sender.sendMessage("§a/tpa <玩家id>§b请求传送至玩家");
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender,Command cmd,String label,String[] args) {
        if (args.length == 2){
            return CommandManage.PlayersList(args[1]);
        }
        return null;
    }
}
