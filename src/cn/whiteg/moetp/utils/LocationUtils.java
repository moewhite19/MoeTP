package cn.whiteg.moetp.utils;

import org.bukkit.Location;

public class LocationUtils {
    public static String LocationToString(Location location) {
        return new StringBuilder().append("§b世界: §f").append(location.getWorld().getName())
                .append(" §bX:§f").append(location.getBlockX())
                .append(" §bY:§f").append(location.getBlockY())
                .append(" §bZ:§f").append(location.getBlockZ())
                .toString();
    }

    //获取两个位置的距离，如果不在一个世界则从目标位置的传送点开始算
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

    public static void worldPreloading(Location location) {
        //区块预加载
//        CraftWorld cw = (CraftWorld) location.getWorld();
//        var nw = cw.getHandle();
//        nw.getChunkProvider().addTicket(TicketType.LOGIN,new ChunkCoordIntPair(location.getBlockX() >> 4,location.getBlockZ() >> 4),3,100L);
    }
}
