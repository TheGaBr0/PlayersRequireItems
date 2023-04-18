package FileHandlers;

import Events.Event;
import me.pri.maven.PlayersRequireItems;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class PlayersJoinedManager {
    private final String fileSeparator = System.getProperty("file.separator");
    private PlayersRequireItems plugin;

    public PlayersJoinedManager(PlayersRequireItems plugin){
        this.plugin = plugin;
    }

    public void resetPlayers(Event e) throws IOException {
        File joinedPlayers = new File(String.valueOf(plugin.getDataFolder() + fileSeparator + "Events"+ fileSeparator +
                e.getName() + fileSeparator + "playersjoined.yml"));

        FileConfiguration ymlJoinedPlayers = YamlConfiguration.loadConfiguration(joinedPlayers);

        ymlJoinedPlayers.set("AlreadyJoined", null);

        ymlJoinedPlayers.save(joinedPlayers);
    }

    public void addJoinedPlayer(Player p, Event e) throws IOException {

        File joinedPlayers = new File(String.valueOf(plugin.getDataFolder() + fileSeparator + "Events"+ fileSeparator +
                e.getName() + fileSeparator + "playersjoined.yml"));

        FileConfiguration ymlJoinedPlayers = YamlConfiguration.loadConfiguration(joinedPlayers);

        ArrayList<String> list = e.getJoinedPlayers();

        list.add(p.getUniqueId().toString());

        ymlJoinedPlayers.set("AlreadyJoined", list);

        ymlJoinedPlayers.save(joinedPlayers);

    }

    public boolean hasJoined(Player p, Event e){

        return e.getJoinedPlayers().contains(p.getUniqueId().toString());

    }

    public ArrayList<String> getJoinedPlayers(Event e){

        File joinedPlayers = new File(String.valueOf(plugin.getDataFolder() + fileSeparator + "Events"+ fileSeparator +
                e.getName() + fileSeparator + "playersjoined.yml"));

        FileConfiguration ymlJoinedPlayers = YamlConfiguration.loadConfiguration(joinedPlayers);

        return (ArrayList<String>) ymlJoinedPlayers.getStringList("AlreadyJoined");

    }
}
