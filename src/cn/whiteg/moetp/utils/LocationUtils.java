package cn.whiteg.moetp.utils;

import org.bukkit.Location;

public class LocationUtils {
    public static String LocationToString(Location location) {
        StringBuilder sb = new StringBuilder();
        sb.append("§b世界: §f");
        sb.append(location.getWorld().getName());
        sb.append(" §bX:§f" + location.getBlockX());
        sb.append(" §bY:§f" + location.getBlockY());
        sb.append(" §bZ:§f" + location.getBlockZ());
        return sb.toString();
    }

    public static double LocationDistance(Location startLoc,Location endLoc) {
        try{
            return startLoc.distance(endLoc);
        }catch (IllegalArgumentException e){
            return endLoc.distance(endLoc.getWorld().getSpawnLocation());
        }catch (Exception e){
            e.printStackTrace();
            return 0;
        }
    }
}
