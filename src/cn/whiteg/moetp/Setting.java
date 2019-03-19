package cn.whiteg.moetp;

import cn.whiteg.mmocore.DataCon;
import cn.whiteg.mmocore.sound.SingleSound;
import cn.whiteg.mmocore.sound.Sound;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class Setting {
    final private static int CONFIGVER = 11;
    public static boolean DEBUG;
    public static List<String> subCommands;
    public static FileConfiguration warps;
    public static FileConfiguration worldborder;
    public static int PlayerMaxHomes;
    public static boolean AUTO_SETFLY;
    public static Double FLY_SPEED;
    public static int tpcd;
    public static boolean joinSpawn = false;
    public static boolean setRespawn = false;
    public static double deductMoneyRate = 0.0001D;
    public static double rcoolDownRate = 2;
    public static int DelayTpTime = 0;
    public static boolean ignoreLoadedChunk = true;
    public static boolean outOfBorder = true;
    public static boolean outOfNether = true;
    public static boolean teleportAsync = false;
    public static boolean PlayParticle = false;
    public static Sound START_SOUND = Sound.EMPTY;
    public static Sound BREAK_SOUND = SingleSound.EMPTY;
    public static Sound END_SOUND = SingleSound.EMPTY;

    public static void reload() {

        File file = new File(MoeTP.plugin.getDataFolder(),"config.yml");
        final FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        //自动更新配置文件
        if (config.getInt("ver") < CONFIGVER){
            MoeTP.plugin.saveResource("config.yml",true);
            config.set("ver",CONFIGVER);
            final FileConfiguration newcon = YamlConfiguration.loadConfiguration(file);
            Set<String> keys = newcon.getKeys(true);
            for (String k : keys) {
                if (config.isSet(k)) continue;
                config.set(k,newcon.get(k));
                MoeTP.logger.info("新增配置节点: " + k);
            }
            try{
                config.save(file);
            }catch (IOException e){
                e.printStackTrace();
            }
        }
        DEBUG = config.getBoolean("debug");
        subCommands = Arrays.asList("back","spawn","setspawn","confirm","cancel","setwarp","warp","rmwarp","tpa","tpo","tpohere","tpahere","tpall","tpaall","home","sethome","rmhome","fly","speed","ohome","top");
        PlayerMaxHomes = config.getInt("Player.MaxHomes",5);
        AUTO_SETFLY = config.getBoolean("AutoSetFly",false);
        FLY_SPEED = config.getDouble("FlySpeed",0.05);
        tpcd = config.getInt("TpCd",5000);
        joinSpawn = config.getBoolean("joinSpawn",joinSpawn);
        setRespawn = config.getBoolean("setRespawn",setRespawn);
        deductMoneyRate = config.getDouble("deductMoneyRate",deductMoneyRate);
        rcoolDownRate = config.getDouble("rcoolDownRate",rcoolDownRate);
        ignoreLoadedChunk = config.getBoolean("ignoreLoadedChunk",ignoreLoadedChunk);
        outOfBorder = config.getBoolean("outOfBorder",outOfBorder);
        outOfNether = config.getBoolean("outOfNether",outOfNether);
        teleportAsync = config.getBoolean("teleportAsync",teleportAsync);

        ConfigurationSection cs = config.getConfigurationSection("TelportDelay");
        if (cs != null){
            DelayTpTime = cs.getInt("DelaySecond",DelayTpTime);
            PlayParticle = cs.getBoolean("PlayParticle",PlayParticle);
            START_SOUND = Sound.parseYml(cs.get("StartSound"));
            BREAK_SOUND = Sound.parseYml(cs.get("BreakSound"));
            END_SOUND = Sound.parseYml(cs.get("EndSound"));
        }

        //读取warp.yml
        file = new File(MoeTP.plugin.getDataFolder(),"warps.yml");
        if (file.exists()){
            warps = YamlConfiguration.loadConfiguration(file);
        } else {
            warps = new YamlConfiguration();
        }
        //读取WorldBorder.yml
        file = new File(MoeTP.plugin.getDataFolder(),"worldborder.yml");
        if (file.exists()){
            worldborder = YamlConfiguration.loadConfiguration(file);
        } else {
            worldborder = null;
            MoeTP.plugin.saveResource("worldborder.yml",false);
        }
    }

    public static int getPlayerMaxHomes(DataCon dc) {
        boolean b = dc.getConfig().getBoolean("Authenticate.Success",false);
        return b ? PlayerMaxHomes + 1 : PlayerMaxHomes;
    }
}