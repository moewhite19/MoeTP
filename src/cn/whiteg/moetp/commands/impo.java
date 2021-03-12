package cn.whiteg.moetp.commands;

import cn.whiteg.mmocore.common.CommandInterface;
import cn.whiteg.mmocore.common.HasCommandInterface;
import cn.whiteg.moetp.MoeTP;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class impo extends HasCommandInterface {

    public impo() {
        arr.add("esshoms");
        arr.add("esswarp");
    }

    List<String> arr = new ArrayList<>();

    @Override
    public boolean executor(CommandSender sender,Command cmd,String label,String[] args) {
        if (args.length == 1){
            if (args[0].equals("esshomes")){
                File dir = new File(MoeTP.plugin.getDataFolder(),"Player");
                for (File file : Objects.requireNonNull(dir.listFiles())) {
                    MoeTP.logger.info("导入" + file.getName());
                    File ef = new File("plugins" + File.separator + "Essentials" + File.separator + "userdata" + File.separator + file.getName());
                    YamlConfiguration con = YamlConfiguration.loadConfiguration(file);
                    YamlConfiguration ec = YamlConfiguration.loadConfiguration(ef);
                    ConfigurationSection homes = ec.getConfigurationSection("homes");
                    if (homes == null){
                        MoeTP.logger.info("跳过" + file.getName());
                        continue;
                    }
                    con.set("homes",homes);
                    try{
                        con.save(file);
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                }
            } else if (args[0].equals("esswarp")){

            } else {
                sender.sendMessage("未知选项");
            }

        } else {
            sender.sendMessage("参数错误");
        }
        return false;
    }

    @Override
    public List<String> completer(CommandSender sender,Command cmd,String label,String[] args) {
        return arr;
    }

    @Override
    public boolean canUseCommand(CommandSender sender) {
        return sender.hasPermission("whiteg.test");
    }
}
