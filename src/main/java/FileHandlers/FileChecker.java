package FileHandlers;

import me.pri.maven.PlayersRequireItems;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

public class FileChecker {

    private final PlayersRequireItems plugin;

    private final String fileSeparator = System.getProperty("file.separator");

    public FileChecker(PlayersRequireItems plugin){
        this.plugin = plugin;
    }

    public boolean eventExists(String name){

        return new File(plugin.getDataFolder() + fileSeparator + "Events" + fileSeparator + name).exists();
    }

    public int hasFilesMissing(String name){
        if(eventExists(name)){
            if(new File(plugin.getDataFolder() + fileSeparator + "Events" + fileSeparator + name + fileSeparator + name+"-config.yml").exists()){
                if(new File(plugin.getDataFolder() + fileSeparator + "Events" + fileSeparator + name + fileSeparator + "playersjoined.yml").exists()){
                    if(new File(plugin.getDataFolder() + fileSeparator + "Events" + fileSeparator + name + fileSeparator + "itemstogive.yml").exists()){
                        return 0;
                    }
                    return 1;
                }
                return 2;
            }
            return 3;
        }
        return 3;
    }

    public List<String> findFoldersInDirectory(File directory) {

        FileFilter directoryFileFilter = new FileFilter() {
            public boolean accept(File file) {
                return file.isDirectory();
            }
        };

        File[] directoryListAsFile = directory.listFiles(directoryFileFilter);
        List<String> foldersInDirectory = new ArrayList<String>(directoryListAsFile.length);
        for (File directoryAsFile : directoryListAsFile) {
            foldersInDirectory.add(directoryAsFile.getName());
        }

        return foldersInDirectory;
    }

    public List<String> getEventsName(){
        File eventsFolderPath = new File(plugin.getDataFolder()+fileSeparator+"Events");

        return findFoldersInDirectory(eventsFolderPath);
    }
}
