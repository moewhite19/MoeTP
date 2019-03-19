package cn.whiteg.moetp.utils;

import cn.whiteg.mmocore.DataCon;
import cn.whiteg.mmocore.util.YamlUtils;
import cn.whiteg.moetp.Setting;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.Set;

public class PlayerHomeManage {
    public static final String homePath = "homes";

    public static Location getPlayerHome(DataCon dc,String key) throws InvalidHomeException {
        ConfigurationSection homes = dc.getConfig().getConfigurationSection(homePath);
        if (homes != null){
            ConfigurationSection data = homes.getConfigurationSection(key);
            if (data != null){
                String worldName = data.getString("world","-");
                World world = Bukkit.getWorld(worldName);
                if (world == null){
                    throw new InvalidHomeException("§b找不到世界:§f " + worldName);
                } else {
                    return new Location(world,data.getDouble("x"),data.getDouble("y"),data.getDouble("z"),(float) data.getDouble("yaw",0.0D),(float) data.getDouble("pitch",0.0D));
                }
            } else {
                throw new InvalidHomeException("§b无效数据类型");
            }
        }
        throw new NotHomeException(key);
    }

    @Nullable
    public static Set<String> getHomes(DataCon dc) {
        ConfigurationSection homes = dc.getConfig().getConfigurationSection(homePath);
        if (homes != null){
            return homes.getKeys(false);
        }
        //noinspection unchecked
        return Collections.EMPTY_SET;
    }

    public static void showHome(DataCon dc,Player showTo) {
        Set<String> homes = PlayerHomeManage.getHomes(dc);
        if (homes == null || homes.isEmpty()){
            showTo.sendMessage((isSelf(showTo,dc) ? "§b阁下" : dc.getName()) + "§b当前还没有home");
        } else {
            ComponentBuilder cb = new ComponentBuilder((isSelf(showTo,dc) ? "§b阁下" : dc.getName()) + "§b当前可用home有§3(可使用鼠标点击传送):\n");
            for (String home : homes) {
                cb.append(home + " ").event(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND,(isSelf(showTo,dc) ? "/home " : ("/ohome " + dc.getName() + " ")) + home)).event(new HoverEvent(HoverEvent.Action.SHOW_TEXT,new Text("点我传送至§9" + home)));
            }
            showTo.spigot().sendMessage(cb.create());
        }
    }

    /**
     * 设置Home
     *
     * @param dc     玩家数据
     * @param name   名字
     * @param loc    位置
     * @param sender 请求者
     * @throws InvalidHomeException 出现问题
     */
    public static void setHome(DataCon dc,String name,Location loc,CommandSender sender) throws InvalidHomeException {
        validKey(name);
        if (name.contains(".") || name == null || name.isEmpty()){
            throw new InvalidHomeException("§b名字含无效字符");
        }
        ConfigurationSection homes = dc.getConfig().getConfigurationSection("homes");
        if (homes == null){
            homes = dc.createSection("homes");
        }
        int homesize = homes.getKeys(false).size();
        int maxhome = getPlayerMaxHome(dc);
        ConfigurationSection cs = homes.getConfigurationSection(name);
        if (cs != null && homesize <= maxhome){
            YamlUtils.setLocation(cs,loc);
            dc.onSet();
            if (sender != null) sender.sendMessage("§b已覆盖Home §f" + name);
        } else if (homesize < maxhome){
            cs = homes.createSection(name);
            YamlUtils.setLocation(cs,loc);
            dc.onSet();
            if (sender != null) sender.sendMessage("§b已创建Home §f" + name);
        } else {
            throw new InvalidHomeException("§bhome数量已达到上限§f" + maxhome);
        }
    }

    public static int getPlayerMaxHome(DataCon dc) {
        return dc.getConfig().getInt("Player.ExtraHome",0) + Setting.PlayerMaxHomes;
    }

    //是否为自己
    static boolean isSelf(CommandSender sender,DataCon dc) {
        if (sender instanceof Player){
            return sender.getName().equals(dc.getName());
        }
        return false;
    }

    //字符串是否可以用来当配置名
    public static void validKey(String key) throws InvalidHomeException {
        if (key == null || key.isEmpty() || key.contains(".") || key.contains("\\") || key.contains("/")){
            throw new InvalidHomeException("名称包含无效字符串");
        }
    }

    public static class InvalidHomeException extends Exception {
        InvalidHomeException(String msg) {
            super(msg);
        }
    }

    public static class NotHomeException extends InvalidHomeException {
        NotHomeException(String key) {
            super("§b找不到Home" + key);
        }
    }
}
