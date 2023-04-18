package me.pri.maven;

import Commands.*;
import GUIs.*;
import Events.*;
import FileHandlers.*;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class PlayersRequireItems extends JavaPlugin{

    public ItemsManager itemsManager = new ItemsManager(this);
    public PlayersJoinedManager playersJoinedManager = new PlayersJoinedManager(this);
    public FileRemover remover = new FileRemover(this);
    public FileBuilder builder = new FileBuilder(this);
    public EventsManager eventManager = new EventsManager(this);
    public FileChecker checker = new FileChecker(this);

    @Override
    public void onEnable() {
        getLogger().info("has been enabled!");

        initialize();

        getServer().getPluginManager().registerEvents(new JoinEventManager(this), this);
        getServer().getPluginManager().registerEvents(new EventItemsGui(this), this);
        getServer().getPluginManager().registerEvents(new ConfirmationGui(this), this);
        getServer().getPluginManager().registerEvents(new AllEventsGui(this), this);
        getServer().getPluginManager().registerEvents(new EventSettingsGui(this), this);
        getServer().getPluginManager().registerEvents(new EventMessagesGui(this), this);
        getServer().getPluginManager().registerEvents(new EventMessagesGui_Inv(this), this);

        new PriManager(this);

        getCommand("playersrequireitems").setExecutor(new PriManager(this));


    }


    @Override
    public void onDisable(){
        getLogger().info("has been disabled!");
    }

    public void updateTodayEvents(){
        eventManager.loadTodayEvents();
    }

    private void initialize(){

        //events
        eventManager.loadEvents();

        //files

        String fileSeparator = System.getProperty("file.separator");

        File folder = new File(String.valueOf(this.getDataFolder()+fileSeparator+"Events"));
        File config = new File(this.getDataFolder(), "config.yml");

        if(!folder.exists())
            folder.mkdirs();

        if(!config.exists())
            saveResource("config.yml", false);


    }


}

