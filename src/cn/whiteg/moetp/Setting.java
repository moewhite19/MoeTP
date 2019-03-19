package cn.whiteg.moetp;

import cn.whiteg.mmocore.util.YamlUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class Setting {
    public static boolean DEBUG;
    public static List<String> subCommands;
    public static FileConfiguration config;
    public static Location spawnLoc;
    public static FileConfiguration warps;
    public static int PlayerMaxHomes;

    public static void reload() {
        File file = new File(MoeTP.plugin.getDataFolder(),"config.yml");
        config = YamlConfiguration.loadConfiguration(file);
        DEBUG = config.getBoolean("debug");
        subCommands = Arrays.asList("back","spawn","setspawn","confirm","cancel","setwarp","warp","rmwarp","tpa","tpo","tpohere","tpahere","tpall","tpaall","home","sethome","rmhome");
        PlayerMaxHomes = config.getInt("Player.MaxHomes",5);
        //读取warp.yml
        file = new File(MoeTP.plugin.getDataFolder(),"warps.yml");
        if (file.exists()){
            warps = YamlConfiguration.loadConfiguration(file);
        } else {
            warps = new YamlConfiguration();
        }
        //获取spawn传送点
        ConfigurationSection cs = Setting.warps.getConfigurationSection("spawn");
        if (cs == null){
            spawnLoc = Bukkit.getWorlds().get(0).getSpawnLocation();
        } else {
            spawnLoc = YamlUtils.getLocation(cs);
        }
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
}
