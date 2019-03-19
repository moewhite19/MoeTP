package cn.whiteg.moetp;

import cn.whiteg.mmocore.util.FileMan;
import cn.whiteg.moetp.listener.PlayerFarTP;
import cn.whiteg.moetp.listener.PlayerTP;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.logging.Logger;

import static cn.whiteg.moetp.Setting.*;


public class MoeTP extends JavaPlugin {
    public CommandManage mainCommand;
    public static Logger logger;
    public static MoeTP plugin;
    public Map<String, Listener> listenerMap = new HashMap<>();
    private SubCommand subCommander;

    public void onLoad() {
        saveDefaultConfig();
    }

    public void onEnable() {
        plugin = this;
        logger = getLogger();
        logger.info("开始加载插件");
        reload();
        if (Setting.DEBUG) logger.info("§a调试模式已开启");
        mainCommand = new CommandManage();
        getCommand("moetp").setExecutor(mainCommand);
        subCommander = new SubCommand();
        regEven(new PlayerTP());
        regEven(new PlayerFarTP());
        Bukkit.getScheduler().runTask(this,() -> {
            for (String cmd : Setting.subCommands) {
                PluginCommand c = getCommand(cmd);
                if (c != null){
                    c.setExecutor(subCommander);
                } else {
                    logger.info(cmd + "指令没有添加到plugin.yml");
                }
            }
        });
        logger.info("全部加载完成");
    }

    public void onDisable() {
        unregEven();
        //注销注册玩家加入服务器事件
        listenerMap.clear();
        logger.info("插件已关闭");
    }

    public void onReload() {
        logger.info("--开始重载--");
        reload();
        logger.info("--重载完成--");
    }

    public void regEven(Listener listener) {
        regEven(listener.getClass().getName(),listener);

    }

    public void regEven(String key,Listener listener) {
        logger.info("注册事件:" + key);
        listenerMap.put(key,listener);
        Bukkit.getPluginManager().registerEvents(listener,plugin);

    }

    public void unregEven() {
        for (Map.Entry<String, Listener> entry : listenerMap.entrySet()) {
            unregEven(entry.getKey());
        }
    }

    public void unregEven(String Key) {
        Listener evens = listenerMap.get(Key);
        if (evens == null){
            return;
        }
        logger.info("注销: " + Key);
        try{
            Class c = evens.getClass();
            Method unreg = c.getDeclaredMethod("unreg");
            unreg.setAccessible(true);
            unreg.invoke(evens);
        }catch (SecurityException | IllegalArgumentException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e){
            e.printStackTrace();
        }
    }
}
