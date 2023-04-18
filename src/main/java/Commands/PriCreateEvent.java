package Commands;

import Events.*;
import me.pri.maven.PlayersRequireItems;
import org.bukkit.command.CommandSender;


public class PriCreateEvent {

    private final PlayersRequireItems plugin;
    private final CommandSender sender;
    private final String[] args;

    public PriCreateEvent(CommandSender sender, String[] args, PlayersRequireItems plugin){
        this.plugin = plugin;
        this.sender = sender;
        this.args = args;


        execute();
    }

    public void execute() {

        if(!plugin.checker.eventExists(args[1])){

            Event e = new Event(args[1], plugin);

            plugin.eventManager.addEvent(e);

            sender.sendMessage("[§6P.R.I.§f]§7 Evento " + args[1] + " creato! Date di attivazione e disattivazione non settate!");

        }else{
            sender.sendMessage("[§6P.R.I.§f]§7 L'evento "+ args[1] +" è già stato creato!");
        }
    }

}
