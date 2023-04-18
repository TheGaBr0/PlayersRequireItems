package Commands;

import Events.*;
import me.pri.maven.PlayersRequireItems;
import org.bukkit.command.CommandSender;

public class PriStandby {

    private final PlayersRequireItems plugin;
    private final Event e;
    private final CommandSender sender;
    private final String[] args;

    public PriStandby(PlayersRequireItems plugin, CommandSender sender, String[] args, Event e){
        this.plugin = plugin;
        this.sender = sender;
        this.e = e;
        this.args = args;
        execute();
    }

    public void execute() {

        if(args.length == 4){
            boolean standby = Boolean.parseBoolean(args[3]);

            if(e.isStandby() && standby)
                sender.sendMessage("[§6P.R.I.§f]§7 L'evento " +e.getName()+ " è già in stand-by!");
            else if(!e.isStandby() && !standby){
                sender.sendMessage("[§6P.R.I.§f]§7 L'evento " +e.getName()+ " è già non in stand-by!");
            }
            else{
                e.setStandby(standby);
                if(standby)
                    sender.sendMessage("[§6P.R.I.§f]§7 L'evento " +e.getName()+ " è ora in stand-by!");
                else
                    sender.sendMessage("[§6P.R.I.§f]§7 L'evento " +e.getName()+ " non è più in stand-by!");
            }

        }else{
            if(e.isStandby()) {
                e.setStandby(false);
                plugin.eventManager.loadTodayEvents();
                sender.sendMessage("[§6P.R.I.§f]§7 L'evento " +e.getName()+ " non è più in stand-by!");
            } else{
                e.setStandby(true);
                plugin.eventManager.loadTodayEvents();
                sender.sendMessage("[§6P.R.I.§f]§7 L'evento " +e.getName()+ " è ora in stand-by!");
            }
        }




    }
}