package Events;

import GUIs.*;
import me.pri.maven.PlayersRequireItems;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;

public class EventsManager {

    private final PlayersRequireItems plugin;
    private static final ArrayList<Event> events = new ArrayList<>();
    private static final ArrayList<Event> events_today = new ArrayList<>();
    private LocalDate today = LocalDate.now();

    public EventsManager(PlayersRequireItems plugin){
        this.plugin = plugin;
    }

    public Event getEvent(String name){

        for(Event e : events){
            if(e.getName().equals(name)){
                return e;
            }
        }
        return null;
    }

    public Event getEventAtIndex(int index){

        return events.get(index);

    }

    public ArrayList<Event> getTodayEvents(){
        loadTodayEvents();
        return events_today;
    }

    public void addEvent(Event e){
        if(!events.contains(e)){
            events.add(e);
            loadTodayEvents();
        }
    }

    public void removeEvent(Event e){
        if(e != null){
            events.remove(e);
            runningEventsKiller(e);
        }
    }

    public void startEventsGui(Player p){

        AllEventsGui eventsGui = new AllEventsGui(plugin);
        eventsGui.openInventory(p);

    }

    public int getNumberOfEvents(){
        checkForMissingFiles();
        return events.size();
    }

    public void checkForMissingFiles(){

        for(Event e : events){
            e.setMissingFiles(plugin.checker.hasFilesMissing(e.getName()) != 0);
        }

    }

    public boolean isWithinRange(LocalDate currentDate, LocalDate activation, LocalDate deactivation){

        return ((currentDate.equals(activation) || currentDate.isAfter(activation)) && currentDate.isBefore(deactivation));
    }

    public void updateToday(){

        if(!today.equals(LocalDate.now())){
            if(today.getYear() != LocalDate.now().getYear()){
                updateYears();
            }
        }

        this.today = LocalDate.now();
    }

    public boolean runningEventsChecker(Event e){

        return isWithinRange(today, e.getActivationDate(), e.getDeactivationDate());

    }

    public void runningEventsKiller(Event e){
        events_today.remove(e);
        e.clearJoinedPlayers();
    }

    public void runningEventsAdder(Event e){
        if(!events_today.contains(e))
            events_today.add(e);
    }

    public void loadTodayEvents(){

        updateToday();

        for(Event e : events){
            if(e.getActivationDate() == null || e.getDeactivationDate() == null || e.isStandby()){
                e.setRunning(false);
                continue;
            }
            
            if(runningEventsChecker(e)){
                runningEventsAdder(e);
                e.setRunning(true);
            }else{
                runningEventsKiller(e);
                e.setRunning(false);
            }
        }

    }

    public void loadEvents(){

        String fileSeparator = System.getProperty("file.separator");

        File eventsFolderPath = new File(plugin.getDataFolder()+fileSeparator+"Events");

        String[] eventsFolders = eventsFolderPath.list();

        if(eventsFolders == null)
            return;

        for(String eventFolder : eventsFolders){

            if(plugin.checker.eventExists(eventFolder)){
                Event e = new Event(eventFolder, plugin);
                e.setMissingFiles(plugin.checker.hasFilesMissing(eventFolder) != 0);
                addEvent(e);
            }
        }
    }

    public void updateYears(){
        LocalDate activation;
        LocalDate deactivation;

        for(Event e : events){
            if(e.isPeriodic()){
                activation = e.getActivationDate();
                deactivation = e.getDeactivationDate();

                try {
                    e.setActivationDate(activation.plusYears(1));
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }

                try {
                    e.setDectivationDate(deactivation.plusYears(1));
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        }
    }
}
