package cn.whiteg.moetp.support;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.Plugin;
import org.dynmap.bukkit.DynmapPlugin;
import org.dynmap.markers.MarkerAPI;

import java.util.Objects;


public class DynmapMarkerHandler implements MapMarkerHandler {
    MarkerAPI markerAPI;

    public DynmapMarkerHandler(Plugin plugin) {
        Objects.requireNonNull(plugin,"dynmap is null");
        markerAPI = ((DynmapPlugin) plugin).getMarkerAPI();
    }

    public MarkerAPI getMarkerAPI() {
        return markerAPI;
    }

    @Override
    public MyMarkerSet getMarkerSet(String name) {
        var set = markerAPI.getMarkerSet(name);
        if (set != null) return new DynmapMarkerSet(set);
        return null;
    }

    @Override
    public MyMarkerSet createMarkerSet(String id,String name) {
        var set = markerAPI.getMarkerSet(id);
        if (set == null) set = markerAPI.createMarkerSet(id,name,null,false);
        set.setDefaultMarkerIcon(markerAPI.getMarkerIcon("door")); //设置图标
        return new DynmapMarkerSet(set);
    }

    public class DynmapMarkerSet implements MyMarkerSet {
        final org.dynmap.markers.MarkerSet markerSet;

        public DynmapMarkerSet(org.dynmap.markers.MarkerSet markerSet) {
            this.markerSet = markerSet;
        }

        @Override
        public MyMarker getMarker(String id) {
            var marker = markerSet.findMarker(id);
            if (marker != null) return new DynMarker(marker);
            return null;
        }

        @Override
        public MyMarker createMarker(String id,String name,Location location) {
            var set = markerSet.createMarker(id,id,location.getWorld().getName(),location.getX(),location.getY(),location.getZ(),markerSet.getDefaultMarkerIcon(),false);
            return new DynMarker(set);
        }

        @Override
        public String getId() {
            return markerSet.getMarkerSetID();
        }

        @Override
        public String getName() {
            return markerSet.getMarkerSetLabel();
        }

        @Override
        public void delete() {
            markerSet.deleteMarkerSet();
        }
    }

    public class DynMarker implements MyMarker {

        final org.dynmap.markers.Marker marker;

        public DynMarker(org.dynmap.markers.Marker marker) {
            this.marker = marker;
        }

        @Override
        public String getName() {
            return getId();
        }

        @Override
        public String getId() {
            return marker.getMarkerID();
        }

        @Override
        public Location getLocation() {
            return new Location(Bukkit.getWorld(marker.getWorld()),marker.getX(),marker.getY(),marker.getZ());
        }

        @Override
        public void delete() {
            marker.deleteMarker();
        }
    }
}
