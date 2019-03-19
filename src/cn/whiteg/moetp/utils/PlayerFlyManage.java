package cn.whiteg.moetp.utils;

import cn.whiteg.mmocore.DataCon;
import cn.whiteg.mmocore.MMOCore;
import cn.whiteg.moetp.Setting;
import org.bukkit.entity.Player;

public class PlayerFlyManage {
    public static void setAllowFlightFly(Player player,boolean b) {
        DataCon dc = MMOCore.getPlayerData(player);
        double f = dc.getDouble("fly_speed");
        player.setAllowFlight(b);
        if (f == 0){
            f = Setting.FLY_SPEED;
        }
        try{
            player.setFlySpeed((float) f);
        }catch (IllegalArgumentException e){
            player.setFlySpeed(0.1F);
        }
    }
}
