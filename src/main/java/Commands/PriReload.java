package Commands;

import me.pri.maven.PlayersRequireItems;
import org.bukkit.command.CommandSender;

public class PriReload{

        private PlayersRequireItems plugin;
        private final String[] args;

        public PriReload(CommandSender sender, String[] args, PlayersRequireItems plugin){
            this.plugin = plugin;
            this.args = args;
            execute(sender);
        }

        public void execute(CommandSender sender) {

        if(sender.hasPermission("pri.reload")){


            if(args.length == 1){
                /*manager.reloadConfig();*/
                sender.sendMessage("[§6P.R.I.§f]§7 Reload del config principale completato!");
            }else{
                plugin.eventManager.getEvent(args[1]).reloadEvent(sender);
                sender.sendMessage("[§6P.R.I.§f]§7 Reload config dell'evento " + args[1] + " completato!");
            }


        }
    }
}