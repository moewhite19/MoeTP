package cn.whiteg.moetp.utils;

import cn.whiteg.mmocore.util.YamlUtils;
import cn.whiteg.moetp.MoeTP;
import cn.whiteg.moetp.Setting;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;

import java.io.File;
import java.io.IOException;


public class WarpManager {
    public static Location spawn = null;

    public static Location getWarp(String name) {
        ConfigurationSection cs = Setting.warps.getConfigurationSection(name);
        if (cs == null){
            return null;
        }
        Location loc = YamlUtils.getLocation(cs);
        if (loc == null){
            return null;
        }
        return loc;
    }

    public static void setWarp(String name,Location loc) {
        ConfigurationSection cs = Setting.warps.getConfigurationSection(name);
        if (cs == null){
            cs = Setting.warps.createSection(name);
        }
        YamlUtils.setLocation(cs,loc);
        if (name.equals("spawn")){
            spawn = loc;
            loc.getWorld().setSpawnLocation(loc);
        }
        saveWarps();
    }

    public static Location getSpawn() {
        if (spawn == null){
            spawn = getWarp("spawn");
            if (spawn == null){
                spawn = Bukkit.getWorlds().get(0).getSpawnLocation();
            }
        }
        return spawn;
    }

    //储存warp传送点
    public static void saveWarps() {
        try{
            File file = new File(MoeTP.plugin.getDataFolder(),"warps.yml");
            if (!file.exists()) file.createNewFile();
            Setting.warps.save(file);
        }catch (IOException e){
            e.printStackTrace();
            MoeTP.logger.info("warp储存失败");
        }
    }

    public static void removeWarp(String name) {
        Setting.warps.set(name,null);
    }
}
