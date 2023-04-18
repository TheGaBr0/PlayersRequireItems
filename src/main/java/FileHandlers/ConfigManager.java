package FileHandlers;

import Events.Event;
import me.pri.maven.PlayersRequireItems;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class ConfigManager {

    private final PlayersRequireItems plugin;
    private final String fileSeparator = System.getProperty("file.separator");
    private final Event e;
    private File eventConfig;
    private FileConfiguration eventConfigYml;
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    //default
    private final String EVENTTITLE_DEFAULT = "§fEcco il tuo §6regalo§f!";
    private final String EVENTSUBTITLE_DEFAULT = "§fBy §eMy§aVanilla";
    public final Integer EVENTTITLEFADEIN_DEFAULT = 10;
    public final Integer EVENTTITLEFADEOUT_DEFAULT = 20;
    public final Integer EVENTTITLESTAY_DEFAULT = 70;

    private final Integer EVENTTITLEFADEIN_MAX = 100;
    private final Integer EVENTTITLEFADEOUT_MAX = 100;
    private final Integer EVENTTITLESTAY_MAX = 200;

    private final Integer EVENTTITLEFADEIN_MIN= 0;
    private final Integer EVENTTITLEFADEOUT_MIN = 0;
    private final Integer EVENTTITLESTAY_MIN = 0;

    public ConfigManager(PlayersRequireItems plugin, Event e){

        this.plugin = plugin;
        this.e = e;
        eventConfig = getEventConfig(e);
        eventConfigYml = YamlConfiguration.loadConfiguration(eventConfig);
    }

    public File getEventConfig(Event e){

        File[] eventsFolders = new File(plugin.getDataFolder()+fileSeparator+"Events").listFiles();

        if(eventsFolders == null)
            return null;

        for(File file : eventsFolders){

            if(file.getName().equals(e.getName())){

                return new File(plugin.getDataFolder() + fileSeparator + "Events" + fileSeparator +
                        file.getName() + fileSeparator + file.getName()+"-config.yml");
            }
        }

        return null;
    }

    public void saveConfig(){
        try {
            eventConfigYml.save(eventConfig);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    //-----------------------------------------getValues------------------------------------------

    public String getMainConfigVersion() {
        File mainConfigFile = new File(String.valueOf(plugin.getDataFolder()), "config.yml");

        return eventConfigYml.getString("Version");}

    public String getEventActivationDate(){
        if(isValid(eventConfigYml.getString("Dates.ActivationDate"))){
            return eventConfigYml.getString("Dates.ActivationDate");
        }
        return null;
    }

    public String getEventDeactivationDate(){
        if(isValid(eventConfigYml.getString("Dates.DeactivationDate"))){
            return eventConfigYml.getString("Dates.DeactivationDate");
        }
        return null;
    }

    public boolean getStandby(){
        return eventConfigYml.getBoolean("Stand-by");
    }

    //OnSuccesfulGive

    public String getEventTitle(){

        if(eventConfigYml.getString("TitleOnSuccessfulGive.TitleUponFirstAccess") == null){
            setEventTitle(EVENTTITLE_DEFAULT);
        }

        return eventConfigYml.getString("TitleOnSuccessfulGive.TitleUponFirstAccess");
    }

    public Integer getEventFadeIn(){

        if(!isInteger(eventConfigYml.getString("TitleOnSuccessfulGive.FadeIn")))
            setEventFadeIn(EVENTTITLEFADEIN_DEFAULT);
        else{
            if(eventConfigYml.getInt("TitleOnSuccessfulGive.FadeIn") <= EVENTTITLEFADEIN_MIN){
                setEventFadeIn(1);
            }
            if(eventConfigYml.getInt("TitleOnSuccessfulGive.FadeIn") >= EVENTTITLEFADEIN_MAX){
                setEventFadeIn(99);
            }
        }

        return eventConfigYml.getInt("TitleOnSuccessfulGive.FadeIn");
    }

    public Integer getEventStay(){

        if(!isInteger(eventConfigYml.getString("TitleOnSuccessfulGive.Stay")))
            setEventStay(EVENTTITLESTAY_DEFAULT);
        else{
            if(eventConfigYml.getInt("TitleOnSuccessfulGive.Stay") <= EVENTTITLESTAY_MIN){
                setEventStay(1);
            }
            if(eventConfigYml.getInt("TitleOnSuccessfulGive.Stay") >= EVENTTITLESTAY_MAX){
                setEventStay(199);
            }
        }

        return eventConfigYml.getInt("TitleOnSuccessfulGive.Stay");
    }

    public Integer getEventFadeOut(){

        if(!isInteger(eventConfigYml.getString("TitleOnSuccessfulGive.FadeOut")))
            setEventFadeOut(EVENTTITLEFADEOUT_DEFAULT);
        else{
            if(eventConfigYml.getInt("TitleOnSuccessfulGive.FadeOut") <= EVENTTITLEFADEOUT_MIN){
                setEventFadeOut(1);
            }
            if(eventConfigYml.getInt("TitleOnSuccessfulGive.FadeOut") >= EVENTTITLEFADEOUT_MAX){
                setEventFadeOut(99);
            }
        }

        return eventConfigYml.getInt("TitleOnSuccessfulGive.FadeOut");
    }

    public String getEventSubtitle(){

        if(eventConfigYml.getString("TitleOnSuccessfulGive.SubtitleUponFirstAccess") == null){
            setEventSubtitle(EVENTSUBTITLE_DEFAULT);
        }

        return eventConfigYml.getString("TitleOnSuccessfulGive.SubtitleUponFirstAccess");
    }

    //OnUnsuccesfulGive


    public String getEventInvTitle(){

        if(eventConfigYml.getString("TitleOnUnsuccessfulGive.TitleUponFirstAccess") == null){
            setEventInvTitle(EVENTTITLE_DEFAULT);
        }

        return eventConfigYml.getString("TitleOnUnsuccessfulGive.TitleUponFirstAccess");
    }

    public Integer getEventInvFadeIn(){

        if(!isInteger(eventConfigYml.getString("TitleOnUnsuccessfulGive.FadeIn")))
            setEventInvFadeIn(EVENTTITLEFADEIN_DEFAULT);
        else{
            if(eventConfigYml.getInt("TitleOnUnsuccessfulGive.FadeIn") <= EVENTTITLEFADEIN_MIN){
                setEventInvFadeIn(1);
            }
            if(eventConfigYml.getInt("TitleOnUnsuccessfulGive.FadeIn") >= EVENTTITLEFADEIN_MAX){
                setEventInvFadeIn(99);
            }
        }

        return eventConfigYml.getInt("TitleOnUnsuccessfulGive.FadeIn");
    }

    public Integer getEventInvStay(){

        if(!isInteger(eventConfigYml.getString("TitleOnUnsuccessfulGive.Stay")))
            setEventInvStay(EVENTTITLESTAY_DEFAULT);
        else{
            if(eventConfigYml.getInt("TitleOnUnsuccessfulGive.Stay") <= EVENTTITLESTAY_MIN){
                setEventInvStay(1);
            }
            if(eventConfigYml.getInt("TitleOnUnsuccessfulGive.Stay") >= EVENTTITLESTAY_MAX){
                setEventInvStay(199);
            }
        }

        return eventConfigYml.getInt("TitleOnUnsuccessfulGive.Stay");
    }

    public Integer getEventInvFadeOut(){

        if(!isInteger(eventConfigYml.getString("TitleOnUnsuccessfulGive.FadeOut")))
            setEventInvFadeOut(EVENTTITLEFADEOUT_DEFAULT);
        else{
            if(eventConfigYml.getInt("TitleOnUnsuccessfulGive.FadeOut") <= EVENTTITLEFADEOUT_MIN){
                setEventInvFadeOut(1);
            }
            if(eventConfigYml.getInt("TitleOnUnsuccessfulGive.FadeOut") >= EVENTTITLEFADEOUT_MAX){
                setEventInvFadeOut(99);
            }
        }

        return eventConfigYml.getInt("TitleOnUnsuccessfulGive.FadeOut");
    }

    public String getEventInvSubtitle(){

        if(eventConfigYml.getString("TitleOnUnsuccessfulGive.SubtitleUponFirstAccess") == null){
            setEventInvSubtitle(EVENTSUBTITLE_DEFAULT);
        }

        return eventConfigYml.getString("TitleOnUnsuccessfulGive.SubtitleUponFirstAccess");
    }

    //-----------------------------------------setValues------------------------------------------

    public void setStandby(boolean standby) throws IOException {

        eventConfigYml.set("Stand-by", standby);

        saveConfig();
    }

    public void setEventActivationDate(LocalDate activationDate){

        String formattedString = activationDate.format(dateTimeFormatter);

        eventConfigYml.set("Dates.ActivationDate", formattedString);

        saveConfig();
    }

    public void setEventDeactivationDate(LocalDate deactivationDate){

        String formattedString = deactivationDate.format(dateTimeFormatter);

        eventConfigYml.set("Dates.DeactivationDate", formattedString);

        saveConfig();
    }

    //OnSuccesfulGive

    public void setEventTitle(String title){
        eventConfigYml.set("TitleOnSuccessfulGive.TitleUponFirstAccess", title);

        saveConfig();
    }

    public void setEventFadeIn(Integer fadeIn){
        eventConfigYml.set("TitleOnSuccessfulGive.FadeIn", fadeIn);

        saveConfig();
    }

    public void setEventStay(Integer stay){
        eventConfigYml.set("TitleOnSuccessfulGive.Stay", stay);

        saveConfig();
    }

    public void setEventFadeOut(Integer fadeOut){
        eventConfigYml.set("TitleOnSuccessfulGive.FadeOut", fadeOut);

        saveConfig();
    }

    public void setEventSubtitle(String subtitle){
        eventConfigYml.set("TitleOnSuccessfulGive.SubtitleUponFirstAccess", subtitle);

        saveConfig();
    }

    //OnUnsuccessfulGive

    public void setEventInvTitle(String title){
        eventConfigYml.set("TitleOnUnsuccessfulGive.TitleUponFirstAccess", title);

        saveConfig();
    }

    public void setEventInvFadeIn(Integer fadeIn){
        eventConfigYml.set("TitleOnUnsuccessfulGive.FadeIn", fadeIn);

        saveConfig();
    }

    public void setEventInvStay(Integer stay){
        eventConfigYml.set("TitleOnUnsuccessfulGive.Stay", stay);

        saveConfig();
    }

    public void setEventInvFadeOut(Integer fadeOut){
        eventConfigYml.set("TitleOnUnsuccessfulGive.FadeOut", fadeOut);

        saveConfig();
    }

    public void setEventInvSubtitle(String subtitle){
        eventConfigYml.set("TitleOnUnsuccessfulGive.SubtitleUponFirstAccess", subtitle);

        saveConfig();
    }

    //-----------------------------------------Checkers---------------------------------------

    public boolean isFirstDateLaterOrEqual(String activationDate, String deactivationDate){

        LocalDate activationLocalDate = LocalDate.parse(activationDate, dateTimeFormatter);
        LocalDate deactivationLocalDate = LocalDate.parse(deactivationDate, dateTimeFormatter);

        if(activationLocalDate.isAfter(deactivationLocalDate))
            return true;
        else{
            return activationLocalDate.isEqual(deactivationLocalDate);
        }


    }

    private boolean isValid(String date) {

        if(date == null)
            return false;

        LocalDate parsedDate = null;

        try {
            parsedDate = LocalDate.parse(date, dateTimeFormatter);
        } catch (DateTimeParseException e) {
            return false;
        }
        return true;
    }

    private boolean isInteger(String s){
        try
        {
            Integer.parseInt(s);
            return true;
        } catch (NumberFormatException ex)
        {
            return false;
        }
    }

    //-----------------------------------------Other------------------------------------------

    public void reloadConfig(){
        eventConfig = getEventConfig(e);
        eventConfigYml = YamlConfiguration.loadConfiguration(eventConfig);

        try {
            eventConfigYml.save(eventConfig);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    /*public void updateConfig(File configFile){

        String fileSeparator = System.getProperty("file.separator");
        File folder = new File(String.valueOf(String.valueOf(Objects.requireNonNull(Bukkit.getPluginManager().getPlugin("PlayersRequireItems")).getDataFolder())+fileSeparator+"Old Configs"));
        File oldConfigFile = new File(folder, "oldconfig("+getMainConfigVersion()+").yml");

        folder.mkdir();
        mainConfigFile.renameTo(oldConfigFile);

        Bukkit.getPluginManager().getPlugin("PlayersRequireItems").saveDefaultConfig();


    }*/




}
