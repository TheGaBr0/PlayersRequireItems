package Commands;

import Events.*;
import me.pri.maven.PlayersRequireItems;
import org.bukkit.command.CommandSender;

public class PriRemoveEvent {

    private final PlayersRequireItems plugin;
    private final CommandSender sender;
    private final Event e;

    public PriRemoveEvent(PlayersRequireItems plugin, CommandSender sender, Event e){
        this.plugin = plugin;
        this.sender = sender;
        this.e = e;
        execute();
    }

    public void execute(){

        e.kill();

        sender.sendMessage("[§6P.R.I.§f]§7 L'evento " + e.getName() + " è stato eliminato!");

    }

}
