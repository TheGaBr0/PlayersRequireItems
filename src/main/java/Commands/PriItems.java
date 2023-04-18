package Commands;

import Events.*;
import GUIs.*;
import me.pri.maven.PlayersRequireItems;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PriItems {

    private final PlayersRequireItems plugin;
    private final Event e;
    private final CommandSender sender;

    public PriItems(CommandSender sender, Event e, PlayersRequireItems plugin){
        this.plugin = plugin;
        this.sender = sender;
        this.e = e;
        plugin.updateTodayEvents();
        execute();
    }

    public void execute() {

        EventItemsGui itemsGui = new EventItemsGui(plugin, e, null);
        itemsGui.openInventory((Player) sender);

    }
}