package cn.whiteg.moetp;

import cn.whiteg.mmocore.common.CommandInterface;
import cn.whiteg.mmocore.common.PluginBase;
import cn.whiteg.mmocore.util.PluginUtil;
import cn.whiteg.moetp.listener.*;
import cn.whiteg.moetp.utils.WorldBorderManager;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.util.Map;
import java.util.logging.Logger;

import static cn.whiteg.moetp.Setting.reload;


public class MoeTP extends PluginBase {
    public static Logger logger;
    public static MoeTP plugin;
    public CommandManage mainCommand;
    private CommandManage.SubCommand subCommander;
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
        PluginCommand pc = getCommand("moetp");
        if (pc != null){
            pc.setExecutor(mainCommand);
            pc.setTabCompleter(mainCommand);
        }
        regListener(new PlayerTP());
        regListener(new PlayerFarTP());
//        regEven(new rideTpListener());
        regListener(new SafeTpListener());
        if (Setting.joinSpawn) regListener(new PlayerSpawnListener());
        if (Setting.setRespawn) regListener(new PlayerReSpawnListener());
//        regEven(new PlayerReSpawnListener());
        if (Setting.AUTO_SETFLY) regListener(new PlayerAutoSetFlyListener());
        for (Map.Entry<String, CommandInterface> entry : mainCommand.commandMap.entrySet()) {
            String cmd = entry.getKey();
            PluginCommand c = PluginUtil.getPluginCommanc(this,cmd);
            if (c != null){
                c.setExecutor(subCommander);
                c.setTabCompleter(subCommander);
            }
        }
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
        unregListener();
        //注销注册玩家加入服务器事件
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
}
