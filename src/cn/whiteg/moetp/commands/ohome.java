package cn.whiteg.moetp.commands;

import cn.whiteg.mmocore.DataCon;
import cn.whiteg.mmocore.MMOCore;
import cn.whiteg.mmocore.common.HasCommandInterface;
import cn.whiteg.moeInfo.api.WhoisMessageProvider;
import cn.whiteg.moeInfo.commands.whois;
import cn.whiteg.moetp.MoeTP;
import cn.whiteg.moetp.utils.EntityTpUtils;
import cn.whiteg.moetp.utils.PlayerHomeManage;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ohome extends HasCommandInterface {
    public ohome(MoeTP plugin) {
        Bukkit.getScheduler().runTask(plugin,() -> {
            if (plugin.hasMoeInfo()){
                whois.regMessager(new HomesWhoisMessageProvider(plugin));
            }
        });
    }

    @Override
    public boolean executo(CommandSender sender,Command cmd,String label,String[] args) {
        if (!(sender instanceof Player)) return false;
        Player player = (Player) sender;
        if (args.length == 1){
            DataCon dc = MMOCore.getPlayerData(args[0]);
            if (dc == null){
                sender.sendMessage(" §b找不到玩家");
                return false;
            }
            PlayerHomeManage.showHome(dc,player);
            return true;
        } else if (args.length == 2){
            DataCon dc = MMOCore.getPlayerData(args[0]);
            if (dc == null){
                sender.sendMessage(" §b找不到玩家");
                return false;
            }
            String name = args[1];
            try{
                Location home = PlayerHomeManage.getPlayerHome(dc,name);
                EntityTpUtils.PlayerTP(player,home);
                sender.sendMessage(" §b传送到§f" + name);
            }catch (PlayerHomeManage.InvalidHomeException e){
                sender.sendMessage(e.getMessage());
            }
        }
        return false;
    }

    @Override
    public List<String> complete(CommandSender sender,Command cmd,String label,String[] args) {
        if (args.length == 1){
            return getMatches(MMOCore.getLatelyPlayerList(),args);
        } else if (args.length == 2){
            if (!(sender instanceof Player)) return null;
            DataCon dc = MMOCore.getPlayerData(args[0]);
            if (dc == null){
                sender.sendMessage(" §b找不到玩家");
                return null;
            }
            ConfigurationSection homes = dc.getConfig().getConfigurationSection("homes");
            if (homes == null){
                return null;
            }
            List<String> list = new ArrayList<>(homes.getKeys(false));
            return getMatches(args,list);
        }
        return null;
    }

    @Override
    public boolean canUseCommand(CommandSender sender) {
        return sender.hasPermission("mmo.otherhome");
    }

    @Override
    public String getDescription() {
        return "显示或者传送到其他玩家的home";
    }

    public class HomesWhoisMessageProvider extends WhoisMessageProvider {
        public HomesWhoisMessageProvider(JavaPlugin plugin) {
            super(plugin);
        }

        @Override
        public String getMsg(CommandSender sender,DataCon dc) {
            if (canUseCommand(sender)){
                Set<String> homes = PlayerHomeManage.getHomes(dc);
                if (homes == null || homes.isEmpty()){
                    return " §b当前没有Home";
                }
                ComponentBuilder cb = new ComponentBuilder("§bHome:");
                for (String home : homes) {
                    cb.append(home + " ").event(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND,(sender.getName().equals(dc.getName()) ? "/home " : ("/ohome " + dc.getName() + " ")) + home)).event(new HoverEvent(HoverEvent.Action.SHOW_TEXT,new Text("点我传送至§9" + home)));
                }
                sender.spigot().sendMessage(cb.create());
            }
            return null;
        }
    }
}
