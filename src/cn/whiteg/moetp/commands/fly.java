package cn.whiteg.moetp.commands;

import cn.whiteg.mmocore.common.CommandInterface;
import cn.whiteg.moetp.utils.PlayerFlyManage;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class fly extends CommandInterface {

    @Override
    public boolean onCommand(CommandSender sender,Command cmd,String label,String[] args) {
        if (args.length == 0){
            if (sender instanceof Player){
                if (sender.hasPermission("mmo.fly.own")){
                    Player player = (Player) sender;
                    boolean isfly = player.getAllowFlight();
                    PlayerFlyManage.setAllowFlightFly(player,!isfly);
                    if (isfly){
                        sender.sendMessage("§b关闭飞行");
                    } else {
                        sender.sendMessage("§b飞行开启");
                    }
                } else {
                    sender.sendMessage("§b阁下没有权限或者当前世界禁止飞行");
                }
            }
        } else if (args.length == 1){
            if (sender.hasPermission("mmo.fly.other")){
                Player p = Bukkit.getPlayer(args[0]);
                if (p == null){
                    sender.sendMessage("§b找不到玩家");
                    return true;
                }
                boolean isfly = p.getAllowFlight();
                PlayerFlyManage.setAllowFlightFly(p,!isfly);
                if (isfly){
                    sender.sendMessage("§b为§r" + p.getDisplayName() + "§b关闭飞行");
                } else {
                    sender.sendMessage("§b为§r" + p.getDisplayName() + "§b开启飞行");
                }
            } else {
                sender.sendMessage("§b阁下没有权限");
            }
        } else if (args.length == 2){
            if (sender.hasPermission("mmo.fly.other")){
                Player p = Bukkit.getPlayer(args[0]);
                if (p == null){
                    sender.sendMessage("§b找不到玩家");
                    return true;
                }
                boolean setfly = args[1].equalsIgnoreCase("on");
                PlayerFlyManage.setAllowFlightFly(p,setfly);
                if (setfly){
                    sender.sendMessage("§b为§r" + p.getDisplayName() + "§b开启飞行");
                } else {
                    sender.sendMessage("§b为§r" + p.getDisplayName() + "§b关闭飞行");
                }
            } else {
                sender.sendMessage("§b阁下没有权限");
            }
        } else {
            sender.sendMessage("/fly 开关飞行");
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender,Command cmd,String label,String[] args) {
        if (args.length == 1){
            return getPlayersList(args,sender);
        } else if (args.length == 2){
            return Arrays.asList("on","off");
        }
        return null;
    }

    @Override
    public String getDescription() {
        return "切换飞行状态";
    }
}
