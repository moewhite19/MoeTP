package cn.whiteg.moetp.commands;

import cn.whiteg.mmocore.CommandInterface;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class speed extends CommandInterface {

    @Override
    public boolean onCommand(CommandSender sender,Command cmd,String label,String[] args) {
        if (!sender.hasPermission("whiteg.test")){
            sender.sendMessage("§b权限不足");
            return true;
        }
        if (args.length >= 3){
            Player player = null;
            if (args.length == 4)
                player = Bukkit.getPlayer(args[3]);
            else if (sender instanceof Player) player = (Player) sender;
            if (player == null){
                sender.sendMessage("找不到玩家");
                return false;
            }
            float speed = 0;
            try{
                speed = Float.valueOf(args[2]);
            }catch (NumberFormatException e){
                //e.printStackTrace()
                sender.sendMessage("无效数值");
                return true;
            }
            sender.sendMessage("设置" + player.getName() + "的" + args[1] + "速度为" + speed);
            if (args[1].equals("fly")){
                speed = speed / 10;
                if (checkSpeed(speed)) player.setFlySpeed(speed);
                else sender.sendMessage("超出有效范围");
            } else if (args[1].equals("walk")){
                speed = speed / 10;
                if (checkSpeed(speed)) player.setWalkSpeed(speed);
                else sender.sendMessage("超出有效范围");
            }
        } else {
            sender.sendMessage("参数有误");
        }
        return true;
    }

    boolean checkSpeed(float f) {
        return !(f > 1 || f < -1);
    }

    @Override
    public List<String> onTabComplete(CommandSender sender,Command cmd,String label,String[] args) {
        if (!sender.hasPermission("whiteg.test")) return null;
        if (args.length == 4){
            return PlayersList(args);
        } else if (args.length == 3){
            List<String> list = new ArrayList<>(4);
            list.addAll(Arrays.asList("1","5","10"));
            if (sender instanceof Player){
                Player player = (Player) sender;
                if (args[1].equals("walk")){
                    list.add(String.valueOf(player.getWalkSpeed() * 10));
                } else {
                    list.add(String.valueOf(player.getFlySpeed() * 10));
                }
            }
            return getMatches(args,list);
        } else if (args.length == 2){
            return getMatches(args,Arrays.asList("walk","fly"));
        }
        return null;
    }

}
