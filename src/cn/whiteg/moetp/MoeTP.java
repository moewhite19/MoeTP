package cn.whiteg.moetp;

import cn.whiteg.mmocore.util.PluginUtil;
import cn.whiteg.moetp.listener.*;
import cn.whiteg.moetp.utils.WorldBorderManager;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.event.Listener;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import static cn.whiteg.moetp.Setting.reload;


public class MoeTP extends JavaPlugin {
    public static Logger logger;
    public static MoeTP plugin;
    public CommandManage mainCommand;
    public Map<String, Listener> listenerMap = new HashMap<>();
    private SubCommand subCommander;
    private Economy economy;

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
//            World world = Bukkit.getWorlds().get(0);
//            WorldBorder wb = world.getWorldBorder();
//            wb.setSize(500);
//            wb.setWarningDistance(50);
//            wb.setDamageBuffer(40);
//            wb.setWarningTime(30);
//            wb.setCenter(world.getSpawnLocation());
        }
        mainCommand = new CommandManage();
        getCommand("moetp").setExecutor(mainCommand);
        subCommander = new SubCommand();
        regEven(new PlayerTP());
        regEven(new PlayerFarTP());
//        regEven(new rideTpListener());
        regEven(new SafeTpListener());
        if (Setting.joinSpawn) regEven(new PlayerSpawnListener());
        if (Setting.setRespawn) regEven(new PlayerReSpawnListener());
//        regEven(new PlayerReSpawnListener());
        if (Setting.AUTO_SETFLY) regEven(new PlayerAutoSetFlyListener());
        Bukkit.getScheduler().runTask(this,() -> {
/*            for (String cmd : Setting.subCommands) {
                try{
                    //注册指令
                    Field commandMap = getServer().getClass().getDeclaredField("commandMap");
                    commandMap.setAccessible(true);
                    CommandMap map = (CommandMap) commandMap.get(getServer());
                    map.register(cmd,getCommand(cmd));
                    logger.info(cmd + "指令添加");
                }catch (NoSuchFieldException | IllegalAccessException e){
                    e.printStackTrace();
                }
            }*/
            for (String cmd : Setting.subCommands) {
                PluginCommand c = PluginUtil.getPluginCommanc(this,cmd);
                if (c != null){

/*                    Plugin e = getServer().getPluginManager().getPlugin("Essentials");
                    PluginCommand ec = e.getServer().getPluginCommand(cmd);
                    if(ec!=null) e.getServer().getPluginCommand(cmd)*/
                    c.setExecutor(subCommander);
                    c.setTabCompleter(subCommander);
                } else {
                    logger.info(cmd + "指令没有添加到plugin.yml");
                }
            }
        });
        WorldBorderManager.set();
        if (Bukkit.getPluginManager().getPlugin("Vault") != null){
            RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(Economy.class);
            if (economyProvider != null){
                this.economy = economyProvider.getProvider();
            }
        }
        logger.info("全部加载完成");
    }

    public void onDisable() {
        unregEven();
        //注销注册玩家加入服务器事件
        listenerMap.clear();
        logger.info("插件已关闭");
    }

    public Economy getEconomy() {
        return economy;
    }

    public void onReload() {
        logger.info("--开始重载--");
        reload();
        WorldBorderManager.set();
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
            logger.info("没有注销事件" + Key + ":" + e.getMessage());
        }
    }
}
