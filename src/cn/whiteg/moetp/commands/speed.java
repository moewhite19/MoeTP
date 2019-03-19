package cn.whiteg.moetp.commands;

import cn.whiteg.mmocore.common.HasCommandInterface;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class speed extends HasCommandInterface {

    @Override
    public boolean executo(CommandSender sender,Command cmd,String label,String[] args) {
        if (args.length >= 2){
            Player player = null;
            if (args.length == 3)
                player = Bukkit.getPlayer(args[2]);
            else if (sender instanceof Player) player = (Player) sender;
            if (player == null){
                sender.sendMessage("找不到玩家");
                return false;
            }
            float speed = 0;
            try{
                speed = Float.parseFloat(args[1]);
            }catch (NumberFormatException e){
                //e.printStackTrace()
                sender.sendMessage("无效数值");
                return true;
            }
            sender.sendMessage("设置" + player.getName() + "的" + args[0] + "速度为" + speed);
            if (args[0].equals("fly")){
                speed = speed / 10;
                if (checkSpeed(speed)) player.setFlySpeed(speed);
                else sender.sendMessage("超出有效范围");
            } else if (args[0].equals("walk")){
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
    public List<String> complete(CommandSender sender,Command cmd,String label,String[] args) {
        if (args.length == 3){
            return getPlayersList(args);
        } else if (args.length == 2){
            List<String> list = new ArrayList<>(4);
            list.addAll(Arrays.asList("1","5","10"));
            if (sender instanceof Player){
                Player player = (Player) sender;
                if (args[0].equals("walk")){
                    list.add(String.valueOf(player.getWalkSpeed() * 10));
                } else {
                    list.add(String.valueOf(player.getFlySpeed() * 10));
                }
            }
            return getMatches(args,list);
        } else if (args.length == 1){
            return getMatches(args,Arrays.asList("walk","fly"));
        }
        return null;
    }

    @Override
    public boolean canUseCommand(CommandSender sender) {
        return sender.hasPermission("whiteg.test");
    }

    @Override
    public String getDescription() {
        return "设置速度";
    }
}
