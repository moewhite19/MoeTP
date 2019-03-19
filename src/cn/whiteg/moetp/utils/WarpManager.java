package cn.whiteg.moetp.utils;

import cn.whiteg.mmocore.util.YamlUtils;
import cn.whiteg.moetp.MoeTP;
import cn.whiteg.moetp.Setting;
import cn.whiteg.moetp.support.MapMarkerHandler;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;

import java.io.File;
import java.io.IOException;
import java.util.Set;


public class WarpManager {
    public static Location spawn = null;

    static {
    }

    //将Warp同步到卫星地图插件
    public static void syncMarker(MapMarkerHandler.MyMarkerSet markerSet) {
        for (String warp : getWarps()) {
            var loc = getWarp(warp);
            if (loc != null) markerSet.createMarker(warp,warp,loc);
        }
    }

    public static Location getWarp(String name) {
        ConfigurationSection cs = Setting.warps.getConfigurationSection(name);
        if (cs == null){
            return null;
        }
        Location loc = YamlUtils.getLocation(cs);
        if (loc == null){
            return null;
        }
        return loc.clone();
    }

    public static void setWarp(String name,Location loc) {
        validKey(name);
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

        var markerSet = MoeTP.plugin.getMarkerSet();
        if (markerSet != null){
            markerSet.createMarker(name,name,loc);
        }

    }

    public static Location getSpawn() {
        if (spawn == null){
            spawn = getWarp("spawn");
            if (spawn == null){
                return Bukkit.getWorlds().get(0).getSpawnLocation();
            }
        }
        return spawn.clone();
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

    public static Set<String> getWarps() {
        return Setting.warps.getKeys(false);
    }

    public static void removeWarp(String name) {
        Setting.warps.set(name,null);
        saveWarps();
        var markerSet = MoeTP.plugin.getMarkerSet();
        if (markerSet != null){
            var mark = markerSet.getMarker(name);
            if (mark != null) mark.delete();
        }
    }

    //字符串是否可以用来当配置名
    public static void validKey(String key) {
        if (key == null || key.isEmpty() || key.contains(".") || key.contains("\\") || key.contains("/")){
            throw new IllegalArgumentException("invalid key");
        }
    }
}
