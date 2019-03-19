package cn.whiteg.moetp.support;

import de.bluecolored.bluemap.api.BlueMapAPI;
import de.bluecolored.bluemap.api.marker.Marker;
import de.bluecolored.bluemap.api.marker.MarkerAPI;
import de.bluecolored.bluemap.api.marker.MarkerSet;
import org.bukkit.Location;
import org.bukkit.plugin.Plugin;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;


public class BlueMapMarkerHandler implements MapMarkerHandler {
    de.bluecolored.bluemap.api.marker.MarkerAPI markerAPI;

    public BlueMapMarkerHandler(Plugin plugin) {
        Objects.requireNonNull(plugin,"BlueMap is null");
        try{
            markerAPI = BlueMapAPI.getInstance().get().getMarkerAPI();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public MarkerAPI getMarkerAPI() {
        return markerAPI;
    }

    @Override
    public MyMarkerSet getMarkerSet(String name) {
        var set = markerAPI.getMarkerSet(name);
        if (set.isPresent()) return new BlueMarkerSet(set.get());
        return null;
    }

    @Override
    public MyMarkerSet createMarkerSet(String id,String name) {
        markerAPI.removeMarkerSet(id);

        var set = Optional.of(markerAPI.createMarkerSet(id));
//        set.setDefaultMarkerIcon(markerAPI.getMarkerIcon("door")); //设置图标
        return new BlueMarkerSet(set.get());
    }

    public class BlueMarkerSet implements MyMarkerSet {
        final MarkerSet markerSet;

        public BlueMarkerSet(MarkerSet markerSet) {
            this.markerSet = markerSet;
        }

        @Override
        public BlueMarker getMarker(String id) {
            var m = markerSet.getMarker(id);
            if (m.isPresent()) return new BlueMarker(m.get(),this);
            return null;
        }

        @Override
        public MyMarker createMarker(String id,String name,Location location) {
            var map = BlueMapAPI.getInstance().get().getMap(location.getWorld().getName());
            if (map.isEmpty()) return null;
            var marker = markerSet.createPOIMarker(id,map.get(),location.getX(),location.getY(),location.getZ());
            return new BlueMarker(marker,this);
        }

        @Override
        public String getId() {
            return markerSet.getId();
        }

        @Override
        public String getName() {
            return markerSet.getId();
        }

        @Override
        public void delete() {
            markerAPI.removeMarkerSet(markerSet);
        }
    }

    public class BlueMarker implements MyMarker {

        final Marker marker;
        private final BlueMarkerSet set;

        public BlueMarker(Marker marker,BlueMarkerSet set) {
            this.marker = marker;
            this.set = set;
        }

        @Override
        public String getName() {
            return getId();
        }

        @Override
        public String getId() {
            return marker.getId();
        }

        @Override
        public Location getLocation() {
            var p = marker.getPosition();
            return new Location(null,p.getX(),p.getY(),p.getZ());
        }

        @Override
        public void delete() {
            set.markerSet.removeMarker(marker);
        }
    }
}
