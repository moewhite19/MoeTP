package cn.whiteg.moetp.utils;

import cn.whiteg.moetp.MoeTP;
import cn.whiteg.moetp.Setting;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldBorder;
import org.bukkit.configuration.ConfigurationSection;

public class WorldBorderManager {
    public static void set() {
        if (Setting.worldborder == null) return;
        ConfigurationSection worlds = Setting.worldborder.getConfigurationSection("worlds");
        if (worlds != null){
            for (String key : worlds.getKeys(false)) {
                World world = Bukkit.getWorld(key);
                if (world == null){
                    MoeTP.logger.warning("无效世界配置: " + key);
                    continue;
                }
                ConfigurationSection sc = worlds.getConfigurationSection(key);
                if (sc == null){
                    MoeTP.logger.warning("无效世界配置: " + key);
                    continue;
                }
                MoeTP.logger.info("设置世界边界: " + key);
                WorldBorder wb = world.getWorldBorder();
                ConfigurationSection cen = sc.getConfigurationSection("Center");
                if (cen != null){
                    wb.setCenter(cen.getDouble("x"),cen.getDouble("z"));
                }
                if (sc.isSet("Size")){
                    wb.setSize(sc.getDouble("Size"));
                }
                if (sc.isSet("DamageBuffer")){
                    wb.setDamageBuffer(sc.getDouble("DamageBuffer"));
                }
                if (sc.isSet("DamageAmount")){
                    wb.setDamageAmount(sc.getDouble("DamageAmount"));
                }
                if (sc.isSet("WarningDistance")){
                    wb.setWarningDistance(sc.getInt("WarningDistance"));
                }
                if (sc.isSet("WarningTime")){
                    wb.setWarningTime(sc.getInt("WarningTime"));
                }
            }

        }
    }
}
