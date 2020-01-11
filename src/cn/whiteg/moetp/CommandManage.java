package cn.whiteg.moetp;

import cn.whiteg.mmocore.CommandInterface;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;

public class CommandManage extends CommandInterface {
    public Map<String, CommandInterface> CommandMap = new HashMap();
    public List<String> AllCmd;

    public CommandManage() {
        AllCmd = Arrays.asList("reload","back","spawn","setspawn","warp","setwarp","rmwarp","tpa","tpo","tpoall","tpohere","tpahere","tpall","tpaall","home","sethome","rmhome","fly","speed","ohome","top");
        for (int i = 0; i < AllCmd.size(); i++) {
            try{
                Class c = Class.forName("cn.whiteg.moetp.commands." + AllCmd.get(i));
                regCommand(AllCmd.get(i),(CommandInterface) c.newInstance());
            }catch (ClassNotFoundException | InstantiationException | IllegalAccessException e){
                e.printStackTrace();
            }
        }
    }

    public static List<String> getMatches(String[] args,List<String> list) {
        return getMatches(args[args.length - 1],list);
    }

    public static List<String> getMatches(List<String> list,String[] args) {
        return getMatches(args[args.length - 1],list);
    }

    public static List<String> getMatches(String value,List<String> list) {
        List<String> result = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            String str = list.get(i).intern().toLowerCase();
            if (str.startsWith(value.toLowerCase())){
                result.add(list.get(i));
            }
        }
        return result;
    }

    public static List<String> getMatches(List<String> list,String value) {
        return getMatches(list,value);
    }

    public static List<String> PlayersList(String arg) {
        Collection<? extends Player> ps = Bukkit.getOnlinePlayers();
        List<String> players = new ArrayList<>(ps.size());
        for (Player p : ps) players.add(p.getName());
        return getMatches(arg,players);
    }

    public static List<String> PlayersList(String[] arg) {
        return PlayersList(arg[arg.length - 1]);
    }

    @Override
    public boolean onCommand(CommandSender sender,Command cmd,String label,String[] args) {
        if (args.length == 0){
            sender.sendMessage("§2[§bMoeTP§2]");
            return true;
        }
        CommandInterface c = CommandMap.get(args[0]);
        if (c != null){
            return c.onCommand(sender,cmd,label,args);
        } else {
            sender.sendMessage("无效指令");
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender,Command cmd,String label,String[] args) {
        if (args.length > 1){
            List ls = null;
            if (CommandMap.containsKey(args[0])) ls = CommandMap.get(args[0]).onTabComplete(sender,cmd,label,args);
            if (ls != null){
                return getMatches(args[args.length - 1],ls);
            }
        }
        for (int i = 0; i < args.length; i++) {
            args[i] = args[i].toLowerCase();
        }
        if (args.length == 1){
            return getMatches(args[0],AllCmd);
        }
        return null;
    }

    public void regCommand(String var1,CommandInterface cmd) {
        CommandMap.put(var1,cmd);
    }

}
