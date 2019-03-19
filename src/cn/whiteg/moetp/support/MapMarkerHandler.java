package cn.whiteg.moetp.support;

import org.bukkit.Location;

public interface MapMarkerHandler {

    MyMarkerSet getMarkerSet(String id);

    MyMarkerSet createMarkerSet(String id,String name);

    interface MyMarkerSet {
        MyMarker getMarker(String id);

        MyMarker createMarker(String id,String name,Location location);

        String getId();

        String getName();

        void delete();
    }

    interface MyMarker {
        String getName();

        String getId();

        Location getLocation();

        void delete();
    }
}
