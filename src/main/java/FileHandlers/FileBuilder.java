package FileHandlers;

import me.pri.maven.PlayersRequireItems;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import java.io.*;
import java.nio.file.Files;
import java.util.Objects;

public class FileBuilder {
    private final PlayersRequireItems plugin;
    private final String fileSeparator = System.getProperty("file.separator");
    private File eventConfig;
    private File playersJoined;
    private File itemsToGive;

    public FileBuilder(PlayersRequireItems plugin){
         this.plugin = plugin;
    }

    public void eventCreator(String name){

        File folder = new File(String.valueOf(plugin.getDataFolder()+ fileSeparator + "Events" + fileSeparator + name));

        folder.mkdirs();

        eventConfig = new File(String.valueOf(plugin.getDataFolder() + fileSeparator + "Events" + fileSeparator +
                name + fileSeparator + name+"-config.yml"));
        eventConfigInitializer();

        playersJoined = new File(String.valueOf(plugin.getDataFolder() + fileSeparator + "Events"+ fileSeparator +
                name + fileSeparator + "playersjoined.yml"));
        playersJoinedInitializer();

        itemsToGive = new File(String.valueOf(plugin.getDataFolder() + fileSeparator + "Events"+ fileSeparator +
                name + fileSeparator + "itemstogive.yml"));
        itemsToGiveInitializer();

    }

    private void copy(InputStream in, File file) {
        try {
            OutputStream out = new FileOutputStream(file);
            byte[] buf = new byte[1024];
            int len;
            while((len=in.read(buf))>0){
                out.write(buf,0,len);
            }
            out.close();
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void eventConfigInitializer(){

        copy(plugin.getResource("eventconfig.yml"), eventConfig);

    }

    private void playersJoinedInitializer(){
        FileConfiguration ymlPlayersJoined = YamlConfiguration.loadConfiguration(playersJoined);

        ymlPlayersJoined.createSection("AlreadyJoined");

        try {
            ymlPlayersJoined.save(playersJoined);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void itemsToGiveInitializer(){
        FileConfiguration ymlItemsToGive = YamlConfiguration.loadConfiguration(itemsToGive);

        ymlItemsToGive.createSection("Items");

        try {
            ymlItemsToGive.save(itemsToGive);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
