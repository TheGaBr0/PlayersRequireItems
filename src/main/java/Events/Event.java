package Events;

import FileHandlers.*;
import me.pri.maven.PlayersRequireItems;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Event {

    private final PlayersRequireItems plugin;
    private final String eventName;
    private boolean missingFiles = false;
    public final ConfigManager configManager;
    private boolean standby;
    private boolean running;
    private boolean areDatesPeriodic;

    private LocalDate activationDate;
    private LocalDate deactivationDate;
    private int fadeIn;
    private int stay;
    private int fadeOut;
    private int invFadeIn;
    private int invStay;
    private int invFadeOut;

    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    //Constructor
    public Event(String name, PlayersRequireItems plugin){

        this.plugin = plugin;

        eventName = name;
        running = false;
        areDatesPeriodic = false;

        if(!plugin.checker.eventExists(name)){
            plugin.builder.eventCreator(eventName);
        }

        configManager = new ConfigManager(plugin, this);
        standby = configManager.getStandby();

        updateValuesFromConfig(null);

    }

    //-----------------------------------------getValues------------------------------------------
    public String getName(){
        return eventName;
    }

    public boolean getIfMissingFiles(){
        return missingFiles;
    }

    public boolean isPeriodic(){
        return areDatesPeriodic;
    }

    public LocalDate getActivationDate(){ return activationDate; }

    public LocalDate getDeactivationDate(){ return deactivationDate; }

    public boolean isStandby(){
        return standby;
    }

    public boolean isRunning(){
        return running;
    }

    public boolean hasJoined(Player p){
        return plugin.playersJoinedManager.hasJoined(p, this);
    }

    public ArrayList<String> getJoinedPlayers(){
        return plugin.playersJoinedManager.getJoinedPlayers(this);
    }

    public List<ItemStack> getItemList(){
        return plugin.itemsManager.getItemList(this);
    }

    //OnSuccesfulGive

    public String getEventTitle(){ return configManager.getEventTitle(); }

    public Integer getEventFadeIn(){ return fadeIn; }

    public Integer getEventStay(){ return stay; }

    public Integer getEventFadeOut(){ return fadeOut; }

    public String getEventSubtitle(){ return configManager.getEventSubtitle(); }

    //OnUnSuccesfulGive

    public String getEventInvTitle(){ return configManager.getEventInvTitle(); }

    public Integer getEventInvFadeIn(){ return invFadeIn; }

    public Integer getEventInvStay(){ return invStay; }

    public Integer getEventInvFadeOut(){ return invFadeOut; }

    public String getEventInvSubtitle(){ return configManager.getEventInvSubtitle(); }

    //-----------------------------------------setValues------------------------------------------
    public void setActivationDate(LocalDate date) throws IOException {
        activationDate = date;
        configManager.setEventActivationDate(date);
    }

    public void setDectivationDate(LocalDate date) throws IOException {
        deactivationDate = date;
        configManager.setEventDeactivationDate(date);
    }

    public void setPeriodic(boolean periodic){ areDatesPeriodic = periodic; }

    public void setStandby(Boolean standby){
        this.standby = standby;
        try {
            configManager.setStandby(standby);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setMissingFiles(boolean missing){ missingFiles = missing; }

    public void setRunning(boolean running){ this.running = running; }

    //OnSuccesfulGive

    public void setEventTitle(String title){ configManager.setEventTitle(title); }

    public void setEventFadeIn(Integer fadeIn){
        this.fadeIn = fadeIn;
        configManager.setEventFadeIn(fadeIn); }

    public void setEventStay(Integer stay){
        this.stay = stay;
        configManager.setEventStay(stay); }

    public void setEventFadeOut(Integer fadeOut){
        this.fadeOut = fadeOut;
        configManager.setEventFadeOut(fadeOut);
    }

    public void setEventSubtitle(String subtitle){ configManager.setEventSubtitle(subtitle); }

    public void setTitleDefault(){
        fadeOut = configManager.EVENTTITLEFADEOUT_DEFAULT;
        configManager.setEventFadeOut(configManager.EVENTTITLEFADEOUT_DEFAULT);

        fadeIn = configManager.EVENTTITLEFADEOUT_DEFAULT;
        configManager.setEventFadeIn(configManager.EVENTTITLEFADEIN_DEFAULT);

        stay = configManager.EVENTTITLEFADEOUT_DEFAULT;
        configManager.setEventStay(configManager.EVENTTITLESTAY_DEFAULT);
    }

    //OnUnSuccesfulGive

    public void setEventInvTitle(String title){ configManager.setEventInvTitle(title); }

    public void setEventInvFadeIn(Integer fadeIn){
        invFadeIn = fadeIn;
        configManager.setEventInvFadeIn(fadeIn); }

    public void setEventInvStay(Integer stay){
        invStay = stay;
        configManager.setEventInvStay(stay); }

    public void setEventInvFadeOut(Integer fadeOut){
        invFadeOut = fadeOut;
        configManager.setEventInvFadeOut(fadeOut); }

    public void setEventInvSubtitle(String subtitle){ configManager.setEventInvSubtitle(subtitle); }

    public void setTitleInvDefault(){
        invFadeOut = configManager.EVENTTITLEFADEOUT_DEFAULT;
        configManager.setEventInvFadeOut(configManager.EVENTTITLEFADEOUT_DEFAULT);

        invFadeIn = configManager.EVENTTITLEFADEOUT_DEFAULT;
        configManager.setEventInvFadeIn(configManager.EVENTTITLEFADEIN_DEFAULT);

        invStay = configManager.EVENTTITLEFADEOUT_DEFAULT;
        configManager.setEventInvStay(configManager.EVENTTITLESTAY_DEFAULT);
    }

    //playersHandlers

    public void addJoinedPlayer(Player p){
        try {
            plugin.playersJoinedManager.addJoinedPlayer(p, this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void clearJoinedPlayers(){
        try {
            plugin.playersJoinedManager.resetPlayers(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //itemHandlers

    public void addItem(ItemStack itemToAdd){
        try {
            plugin.itemsManager.addItem(itemToAdd, this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void removeItem(ItemStack item){
        try {
            plugin.itemsManager.removeItem(item, this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //-----------------------------------------Other------------------------------------------

    //config
    public void reloadEvent(CommandSender sender){
        configManager.reloadConfig();
        updateValuesFromConfig(sender);
    }

    //Other
    public void kill(){
        plugin.eventManager.removeEvent(this);
        plugin.remover.eventRemover(this);
    }


    public void updateValuesFromConfig(@Nullable CommandSender sender){
        if(configManager.getEventActivationDate() != null && configManager.getEventDeactivationDate() != null){
            if(!configManager.isFirstDateLaterOrEqual(configManager.getEventActivationDate(), configManager.getEventDeactivationDate())){
                activationDate = LocalDate.parse(configManager.getEventActivationDate(), dateTimeFormatter);
                deactivationDate = LocalDate.parse(configManager.getEventDeactivationDate(), dateTimeFormatter);
            }else{
                if(sender!=null)
                    sender.sendMessage("[§6P.R.I.§f]§7 Nell'evento "+eventName+" la data di attivazione è uguale oppure oltre la data di disattivazione! I valori sono stati annullati.");
                activationDate = null;
                deactivationDate = null;
            }
        }else{
            if(sender!=null)
                sender.sendMessage("[§6P.R.I.§f]§7 Nell'evento "+eventName+" la data di attivazione o di disattivazione non è valida! I valori sono stati annullati.");
            activationDate = null;
            deactivationDate = null;
        }

        fadeIn = configManager.getEventFadeIn();
        fadeOut = configManager.getEventFadeOut();
        stay = configManager.getEventStay();

        invFadeIn = configManager.getEventInvFadeIn();
        invFadeOut = configManager.getEventInvFadeOut();
        invStay = configManager.getEventInvStay();

    }

}
