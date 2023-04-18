package FileHandlers;

import Events.Event;
import me.pri.maven.PlayersRequireItems;

import java.io.File;

public class FileRemover {

    private final PlayersRequireItems plugin;

    public FileRemover(PlayersRequireItems plugin){

        this.plugin = plugin;

    }

    public static void recursiveDelete(File file) {
        if (!file.exists())
            return;

        if (file.isDirectory()) {
            for (File f : file.listFiles()) {
                recursiveDelete(f);
            }
        }

        file.delete();
    }

    public void eventRemover(Event e){

        String fileSeparator = System.getProperty("file.separator");
        File file = new File(plugin.getDataFolder() + fileSeparator + "Events" + fileSeparator + e.getName());

        if(file.exists())
            recursiveDelete(file);

    }



}
