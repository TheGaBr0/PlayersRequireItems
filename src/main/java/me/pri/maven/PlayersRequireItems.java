package me.pri.maven;

import Commands.*;
import GUIs.*;
import Events.*;
import FileHandlers.*;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class PlayersRequireItems extends JavaPlugin{

    public ItemsManager itemsManager;
    public PlayersJoinedManager playersJoinedManager;
    public FileRemover remover;
    public FileBuilder builder;
    public EventsManager eventManager;
    public FileChecker checker;

    @Override
    public void onEnable() {

        itemsManager = new ItemsManager(this);
        playersJoinedManager = new PlayersJoinedManager(this);
        remover = new FileRemover(this);
        builder = new FileBuilder(this);
        eventManager = new EventsManager(this);
        checker = new FileChecker(this);

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

        getLogger().info("has been enabled!");

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

