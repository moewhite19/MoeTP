package cn.whiteg.moetp.commands;

import cn.whiteg.mmocore.CommandInterface;
import cn.whiteg.mmocore.api.ConfirmManage;
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

public class tpaall extends CommandInterface {

    @Override
    public boolean onCommand(CommandSender sender,Command cmd,String label,String[] args) {
        if (args.length == 1 && sender instanceof Player){
            if (sender.hasPermission("mmo.tpaall")){
                Player p1 = (Player) sender;
                sender.sendMessage("§b给所有玩家发送传送请求");
                for (Player p2 : Bukkit.getOnlinePlayers()) {
                    if (p1 == p2) continue;
                    String confirmID = "tpahere@" + sender.getName();
                    TextComponent a1 = new TextComponent(" §f%sender% §7给所有人发送传送过去的请求 §3§l>>§b§l点我接受传送§3§l<< ".replace("%sender%",p1.getDisplayName()).replace("&","§"));
                    a1.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,"/confirm " + confirmID));
                    a1.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,new ComponentBuilder("§3点我或者使用指令§a/c " + confirmID + "§3接受传送").color(ChatColor.BLUE).create()));
                    p2.spigot().sendMessage(a1);
                    ConfirmManage.addEvent(p2,confirmID,() -> {
                        EntityTpUtils.PlayerTP(p2,p1.getLocation());
                        p1.sendMessage(p2.getDisplayName() + "§r§b已接受传送至阁下");
                        p2.sendMessage("§b正在传送至" + p1.getDisplayName());
                    });
                }
            }else {

                sender.sendMessage("§b阁下没有权限使用这个指令");
            }
        }else {
            sender.sendMessage("§a/tpaall§b请求所有玩家传送至阁下");
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender,Command cmd,String label,String[] args) {
        if (args.length == 2){
            List<String> ls = new ArrayList<>();
            for (Player p : Bukkit.getOnlinePlayers()) {
                ls.add(p.getName());
            }
            ls.remove(sender.getName());
            return getMatches(args[1],ls);
        }
        return null;
    }
}
