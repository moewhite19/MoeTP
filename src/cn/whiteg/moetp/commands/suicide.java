package cn.whiteg.moetp.commands;

import cn.whiteg.mmocore.CommandInterface;
import cn.whiteg.mmocore.api.ConfirmManage;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class suicide extends CommandInterface {

    @Override
    public boolean onCommand(CommandSender sender,Command cmd,String label,String[] args) {
        if(sender instanceof Player){
            Player player = (Player) sender;
            if(sender.hasPermission("mmo.suicide")){
                ConfirmManage.addEvent(player, "suicide" , ()->{
                    player.chat("再见 这个世界");
                    player.setHealth(0);
                });
                sender.sendMessage("输入指令/config suicide确认");
            }else{
                sender.sendMessage("阁下没有权限");
            }
            return true;
        }
        return false;
    }
    @Override
    public List<String> onTabComplete(CommandSender sender,Command cmd,String label,String[] args) {
        return null;
    }
}
