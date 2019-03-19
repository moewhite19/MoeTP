package cn.whiteg.moetp.commands;

import cn.whiteg.mmocore.CommandInterface;
import cn.whiteg.mmocore.enitity.MyMode;
import cn.whiteg.mmocore.enitity.MyZombie;
import cn.whiteg.moetp.MoeTP;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_13_R2.CraftWorld;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent;

import java.util.Arrays;
import java.util.List;

public class test extends CommandInterface {

    @Override
    public boolean onCommand(CommandSender sender,Command cmd,String label,String[] args) {
        if (!sender.hasPermission("whiteg.test")){
            sender.sendMessage("§b权限不足");
            return true;
        }
        if(sender instanceof Player){
            Player p = (Player) sender;
            test(p,args);
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender,Command cmd,String label,String[] args) {
        return null;
    }

    public void test(Player player,String[] args) {
        player.sendMessage("测试项目" + Arrays.toString(args));
        if (args[1].equals("0")){
            MyZombie mz = new MyZombie(player.getLocation());
            ((CraftWorld) player.getLocation().getWorld()).getHandle().addEntity(mz,CreatureSpawnEvent.SpawnReason.CUSTOM);
        } else if (args[1].equals("1")){
            MyMode mz = new MyMode(player.getLocation());
            ((CraftWorld) player.getLocation().getWorld()).getHandle().addEntity(mz,CreatureSpawnEvent.SpawnReason.CUSTOM);
        } else if (args[1].equals("2")){
            MoeTP.plugin.unregEven();
        }
        else {
            player.sendMessage("NULL");
        }
        /*
        EntityLiving oldEntity = ((CraftLivingEntity) sender).getHandle();
        sender.sendMessage("System "+oldEntity.getAttributeMap().a(args[2]).toString());
   */
    }
}
