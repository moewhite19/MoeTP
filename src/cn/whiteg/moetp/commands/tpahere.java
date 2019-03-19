package cn.whiteg.moetp.commands;

import cn.whiteg.mmocore.common.CommandInterface;
import cn.whiteg.moetp.reqest.TpahereReqest;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class tpahere extends CommandInterface {

    @Override
    public boolean onCommand(CommandSender sender,Command cmd,String label,String[] args) {
        if (args.length == 1 && sender instanceof Player){
            if (sender.hasPermission("mmo.tpahere")){
                Player p1 = (Player) sender;
                Player p2 = Bukkit.getPlayer(args[0]);
                if (p2 == null){
                    sender.sendMessage("§b找不到玩家");
                    return true;
                }
                if (p1 == p2) return true;
                new TpahereReqest(p1).sendTo(p2);
//                final Location loc = ((Player) sender).getLocation();
//                String confirmID = "tpahere@" + sender.getName();
//                TextComponent a0 = new TextComponent(" §f" + p1.getDisplayName());
//                a0.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,new ComponentBuilder(p1.getDisplayName()).append("§r\n位置: " + LocationUtils.LocationUtils(p1.getLocation())).create()));
//                TextComponent a1 = new TextComponent(" §7请求阁下传送过去 §3§l>>§b§l点我接受传送§3§l<< ".replace("%sender%",p1.getDisplayName()).replace("&","§"));
//                a1.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,"/confirm " + confirmID));
//                a1.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,new ComponentBuilder("§3点我或者使用指令§a/c " + confirmID + "§3接受传送").color(ChatColor.BLUE).create()));
//                p2.spigot().sendMessage(a0 , a1);
//                Location loc = p1.getLocation();
//                if (ReqestManage.addEvent(p2.getName(),confirmID,() -> {
//                    if (!p1.isOnline()){
//                        p2.sendMessage("§b对方已下线");
//                        return;
//                    }
//                    EntityTpUtils.PlayerTP(p2 , loc, false);
////                    EntityTpUtils.PlayerTpNoCd(p2,loc);
//                    p1.sendMessage(p2.getDisplayName() + "§r§b已接受传送至阁下");
//                    p2.sendMessage("§b正在传送至" + p1.getDisplayName());
//                })){
//                    p2.playSound(p2.getLocation() ,Setting.REQUSET_SOUND,  1 ,1);
//                    p1.sendMessage("§b已发送传送请求至§f" + p2.getName());
//                }else {
//                    sender.sendMessage("已存在一个相同的请求");
//                }
            }
        } else {
            sender.sendMessage("§a/tpahere <玩家id>§b请求玩家传送至阁下");
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender,Command cmd,String label,String[] args) {
        if (args.length == 1){
            List<String> list = getPlayersList(args,sender);
            list.remove(sender.getName());
            return list;
        }
        return null;
    }

    @Override
    public String getDescription() {
        return "请求玩家传送过来:§7 <玩家id>";
    }
}
