package cn.whiteg.moetp;

import cn.whiteg.mmocore.common.CommandManage;
import cn.whiteg.mmocore.common.PluginBase;
import cn.whiteg.moetp.Event.PlayerFarTpEvent;
import cn.whiteg.moetp.listener.*;
import cn.whiteg.moetp.support.BlueMapMarkerHandler;
import cn.whiteg.moetp.support.DynmapMarkerHandler;
import cn.whiteg.moetp.support.MapMarkerHandler;
import cn.whiteg.moetp.utils.WarpManager;
import cn.whiteg.moetp.utils.WorldBorderManager;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.util.logging.Logger;

import static cn.whiteg.moetp.Setting.reload;


public class MoeTP extends PluginBase implements Listener {
    public static Logger logger;
    public static MoeTP plugin;
    public CommandManage mainCommand;
    private boolean moeInfo;
    private Economy economy;
    private MapMarkerHandler.MyMarkerSet markerSet;

    public MoeTP() {
        plugin = this;
    }

    public void onLoad() {
        saveDefaultConfig();
        logger = getLogger();
        reload();
    }

    public void onEnable() {
        logger.info("开始加载插件");
        if (Setting.DEBUG){
            logger.info("§a调试模式已开启");
        }
        mainCommand = new CommandManage(this);
        mainCommand.setExecutor();
        regListener(new PlayerFarTpEvent.listener());
        regListener(new PlayerFarTP());
//        regListener(new RideTpListener());
        if (Setting.outOfBorder || Setting.outOfNether) regListener(new SafeTpListener());
        if (Setting.joinSpawn) regListener(new PlayerSpawnListener());
        if (Setting.setRespawn) regListener(new PlayerReSpawnListener());
        if (Setting.AUTO_SETFLY) regListener(new PlayerAutoSetFlyListener());
        WorldBorderManager.set();
        if (Bukkit.getPluginManager().getPlugin("Vault") != null){
            RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(Economy.class);
            if (economyProvider != null){
                this.economy = economyProvider.getProvider();
            }
        }

        Plugin plugin;
        if ((plugin = Bukkit.getPluginManager().getPlugin("dynmap")) != null){
            try{
                markerSet = new DynmapMarkerHandler(plugin).createMarkerSet("warps","传送点");
                logger.info("supported " + plugin.getDescription().getFullName());
            }catch (Throwable e){
                e.printStackTrace();
                logger.warning("not support " + plugin.getDescription().getFullName());
            }
        } else if ((plugin = Bukkit.getPluginManager().getPlugin("BlueMap")) != null){
            try{
                markerSet = new BlueMapMarkerHandler(plugin).createMarkerSet("warps","传送点");
                logger.info("supported " + plugin.getDescription().getFullName());
            }catch (Throwable e){
                e.printStackTrace();
                logger.warning("not support " + plugin.getDescription().getFullName());
            }
        }

        if (markerSet != null){
            WarpManager.syncMarker(markerSet);
            regListener(this); //支持插件监听器，在支持的插件卸载后取消支持
        }

        moeInfo = Bukkit.getPluginManager().getPlugin("MoeInfo") != null;
        logger.info("全部加载完成");

    }

    public void onDisable() {
        unregListener();
        //注销注册玩家加入服务器事件
        logger.info("插件已关闭");
    }

    @EventHandler(ignoreCancelled = true)
    public void disablePlugin(PluginDisableEvent event) {
        var name = event.getPlugin().getName();
        if (name.equals("dynmap") || name.equals("BlueMap")){
            markerSet = null;
        }
    }

    public Economy getEconomy() {
        return economy;
    }

    public void onReload() {
        logger.info("--开始重载--");
        Setting.reload();
        WorldBorderManager.set();
        logger.info("--重载完成--");
    }

    public boolean hasMoeInfo() {
        return moeInfo;
    }


    public MapMarkerHandler.MyMarkerSet getMarkerSet() {
        return markerSet;
    }
}
