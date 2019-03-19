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

public class tpahere extends CommandInterface {

    @Override
    public boolean onCommand(CommandSender sender,Command cmd,String label,String[] args) {
        if (args.length == 2 && sender instanceof Player){
            if (sender.hasPermission("mmo.tpahere")){
                Player p1 = (Player) sender;
                Player p2 = Bukkit.getPlayer(args[1]);
                if (p2 == null){
                    sender.sendMessage("§b找不到玩家");
                    return true;
                }
                if (p1 == p2) return true;
                String confirmID = "tpahere@" + sender.getName();
                TextComponent a1 = new TextComponent(" §f%sender% §7请求阁下传送过去 §3§l>>§b§l点我接受传送§3§l<< ".replace("%sender%",p1.getDisplayName()).replace("&","§"));
                a1.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,"/confirm " + confirmID));
                a1.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,new ComponentBuilder("§3点我或者使用指令§a/c " + confirmID + "§3接受传送").color(ChatColor.BLUE).create()));
                p2.spigot().sendMessage(a1);
                ConfirmManage.addEvent(p2,confirmID,() -> {
                    EntityTpUtils.PlayerTP(p2,p1.getLocation());
                    p1.sendMessage(p2.getDisplayName() + "§r§b已接受传送至阁下");
                    p2.sendMessage( "§b正在传送至"+p1.getDisplayName());
                });
            }
        }else {
            sender.sendMessage("§a/tpahere <玩家id>§b请求玩家传送至阁下");
        }
        return true;
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
