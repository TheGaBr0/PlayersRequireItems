package Commands;

import Events.*;
import GUIs.*;
import me.pri.maven.PlayersRequireItems;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class PriManager implements TabExecutor {

    private static final String[] EVENTCOMMANDS = {"create", "items", "delete", "standby", "dates", "titles"};
    private static final String[] COMMANDS = {"event", "help", "reload"};
    private static final String[] BINARY = {"true", "false"};
    private static final String[] DATES = {"activationDate:", "deactivationDate:"};
    private static final String[] TITLES = {"normal", "warning"};

    private Event currentEvent;
    private final PlayersRequireItems plugin;

    public PriManager(PlayersRequireItems plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (cmd.getName().equalsIgnoreCase("playersrequireitems")) {
            if (sender.hasPermission("pri")) {
                if (args.length == 0) {
                    sender.sendMessage("[§6P.R.I.§f]§7 Se hai bisogno di una lista dei comandi digita /pri §5help!");
                    return true;
                } else {
                    if (args.length == 1) {
                        switch (args[0]) {
                            case "help":
                                new PriHelp(sender, args, plugin);
                                break;
                            case "reload":
                                new PriReload(sender, args, plugin);
                                break;
                            default:
                                sender.sendMessage("[§6P.R.I.§f]§7 Comando non riconosciuto, prova con /pri §5help!");
                        }
                    } else {
                        if(args[0].equals("help"))
                            new PriHelp(sender, args, plugin);
                        else {
                            if (args[0].equals("reload"))
                                new PriReload(sender, args, plugin);
                            else {
                                currentEvent = plugin.eventManager.getEvent(args[1]);
                                if (args.length >= 3 && !args[1].equals("list")) {
                                    switch (args[2]) {
                                        case "items":
                                            if (currentEvent != null) {
                                                if (sender instanceof Player)
                                                    new PriItems(sender, currentEvent, plugin);
                                                else
                                                    sender.sendMessage("[§6P.R.I.§f]§7 Devi essere un player per eseguire questo comando!");
                                            } else
                                                sender.sendMessage("[§6P.R.I.§f]§7 L'evento " + args[1] + " non esiste!");
                                            break;

                                        case "delete":
                                            if (currentEvent != null)
                                                new PriRemoveEvent(plugin, sender, currentEvent);
                                            else
                                                sender.sendMessage("[§6P.R.I.§f]§7 L'evento " + args[1] + " non esiste!");
                                            break;

                                        case "create":
                                            if (args.length > 3)
                                                new PriDatesCreate(plugin, sender, args);
                                            else
                                                new PriCreateEvent(sender, args, plugin);
                                            break;

                                        case "standby":
                                            if (currentEvent != null)
                                                new PriStandby(plugin, sender, args, currentEvent);
                                            else
                                                sender.sendMessage("[§6P.R.I.§f]§7 L'evento " + args[1] + " non esiste!");
                                            break;
                                        case "dates":
                                            if (currentEvent != null)
                                                new PriDatesUpdate(plugin, sender, args, currentEvent);
                                            else
                                                sender.sendMessage("[§6P.R.I.§f]§7 L'evento " + args[1] + " non esiste!");
                                            break;
                                        case "titles":
                                            if (currentEvent != null)
                                                new PriTitles(plugin, sender, args, currentEvent);
                                            else
                                                sender.sendMessage("[§6P.R.I.§f]§7 L'evento " + args[1] + " non esiste!");
                                            break;
                                        default:
                                            sender.sendMessage("[§6P.R.I.§f]§7 Comando non riconosciuto, prova con /pri §5help!");
                                    }
                                } else {
                                    if (args[1].equals("list")) {
                                        if (sender instanceof Player) {
                                            plugin.updateTodayEvents();
                                            plugin.eventManager.startEventsGui((Player) sender);
                                        } else
                                            sender.sendMessage("[§6P.R.I.§f]§7 Devi essere un player per eseguire questo comando!");
                                    } else {
                                        if (currentEvent != null) {
                                            if (sender instanceof Player) {
                                                EventSettingsGui settingsGui = new EventSettingsGui(plugin, currentEvent, null);
                                                plugin.updateTodayEvents();
                                                settingsGui.openInventory((Player) sender);
                                            } else
                                                sender.sendMessage("[§6P.R.I.§f]§7 Devi essere un player per eseguire questo comando!");
                                        } else {
                                            sender.sendMessage("[§6P.R.I.§f]§7 L'evento " + args[1] + " non esiste!");
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return true;
    }

    private List<String> getActivationDatesList(){
        LocalDate startDate = LocalDate.parse("01-01-"+LocalDate.now().getYear(), DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        LocalDate endDate = LocalDate.parse("01-01-"+(LocalDate.now().getYear()+1), DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        List<String> dates = new ArrayList<String>();

        List<LocalDate> listOfDates = startDate.datesUntil(endDate).collect(Collectors.toList());

        for(LocalDate date : listOfDates){
            dates.add("activationDate:"+date.format(DateTimeFormatter.ofPattern("dd-MM")));
        }

        return dates;

    }

    private List<String> getDeactivationDatesList(){
        LocalDate startDate = LocalDate.parse("01-01-"+LocalDate.now().getYear(), DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        LocalDate endDate = LocalDate.parse("01-01-"+(LocalDate.now().getYear()+1), DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        List<String> dates = new ArrayList<String>();

        List<LocalDate> listOfDates = startDate.datesUntil(endDate).collect(Collectors.toList());

        for(LocalDate date : listOfDates){
            dates.add("deactivationDate:"+date.format(DateTimeFormatter.ofPattern("dd-MM")));
        }

        return dates;

    }

    private List<String> updateTitleInputs(List<String> argsAsList, List<String> list){

        Iterator<String> it = list.iterator();

        while(it.hasNext()){ //finchè c'è qualcosa nell'array list
            String s = it.next();
            for (String value : argsAsList) { //scorro gli args
                if (value.contains(s)) { //se un arg contiene un valore di list
                    if(value.startsWith(s)){//se l'arg inizia con il valore di list (per evitare problema title-subtitle)
                        it.remove();//lo rimuovo
                        break;
                    }

                }
            }
        }

        return list;
    }


    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] args) {

        final List<String> completions = new ArrayList<>();
        final List<String> titlesInputs = new ArrayList<>();

        titlesInputs.add("title:");
        titlesInputs.add("subtitle:");
        titlesInputs.add("fadeIn:");
        titlesInputs.add("stay:");
        titlesInputs.add("fadeOut:");


        //----------------------------BASIC COMMANDS----------------------------
        if (args.length == 1){

            StringUtil.copyPartialMatches(args[0], Arrays.asList(COMMANDS), completions);

            Collections.sort(completions);

            return completions;
        }

        if (args.length == 2 && args[0].equals("help")){

            StringUtil.copyPartialMatches(args[1], Arrays.asList(EVENTCOMMANDS), completions);

            Collections.sort(completions);

            return completions;
        }

        if (args.length == 2 && args[0].equals("reload")){

            List<String> eventNames = plugin.checker.getEventsName();

            StringUtil.copyPartialMatches(args[1], eventNames, completions);

            Collections.sort(completions);

            return completions;
        }

        //--------------------------EVENT LIST------------------------------
        if(args.length == 2 && args[0].equals("event")){

            List<String> eventNames = plugin.checker.getEventsName();

            eventNames.add("list");

            StringUtil.copyPartialMatches(args[1], eventNames, completions);

            Collections.sort(completions);

            return completions;
        }

        //----------------------------EVENTS COMMANDS----------------------------
        if(!args[1].equals("list")){
            if (args.length == 3 && args[0].equals("event")){

                StringUtil.copyPartialMatches(args[2], Arrays.asList(EVENTCOMMANDS), completions);

                Collections.sort(completions);

                return completions;
            }

            //--------------------------STAND-BY
            if(args.length == 4 && args[2].equals("standby")){

                StringUtil.copyPartialMatches(args[3], Arrays.asList(BINARY), completions);

                return completions;

            }

            //--------------------------TITLES
            if(args.length == 4 && args[2].equals("titles")){

                StringUtil.copyPartialMatches(args[3], Arrays.asList(TITLES), completions);

                return completions;

            }

            if(args.length >=5 && args[2].equals("titles")){
                List<String> argsAsList = Arrays.asList(args);

                StringUtil.copyPartialMatches(args[args.length-1], updateTitleInputs(argsAsList, titlesInputs), completions);

                return completions;
            }

            //----------------------------DATES
            if(args.length >=4 && args.length <=5 && (args[2].equals("create") || args[2].equals("dates"))){

                if(args.length == 4){
                    //finchè non ne scrivo completamente uno li mostro entrambi
                    if(!args[3].contains("activationDate:") && !args[3].contains("deactivationDate:")){
                        StringUtil.copyPartialMatches(args[3], Arrays.asList(DATES), completions);
                        return completions;
                    }
                    else{
                        //capisco quale ha deciso e inserisco anche le date affianco
                        if(!args[3].contains("deactivationDate:")){
                            List<String> allDatesTemp = getActivationDatesList();
                            StringUtil.copyPartialMatches(args[3], allDatesTemp, completions);

                            return completions;

                        }else{
                            List<String> allDatesTemp = getDeactivationDatesList();

                            StringUtil.copyPartialMatches(args[3], allDatesTemp, completions);
                            return completions;
                        }

                    }

                }else{
                    //capisco quale ha deciso e inserisco anche le date affianco
                    if(args[3].contains("deactivationDate:")){
                        //finchè non lo scrivo completamente non mostro le date
                        if(!args[4].contains("activationDate:")){
                            StringUtil.copyPartialMatches(args[4], Arrays.asList("activationDate:"), completions);
                            return completions;
                        }

                        List<String> allDatesTemp = getActivationDatesList();

                        StringUtil.copyPartialMatches(args[4], allDatesTemp, completions);
                        return completions;

                    }else{
                        //finchè non lo scrivo completamente non mostro le date
                        if(!args[4].contains("deactivationDate:")){
                            StringUtil.copyPartialMatches(args[4], Arrays.asList("deactivationDate:"), completions);
                            return completions;
                        }

                        List<String> allDatesTemp = getDeactivationDatesList();

                        StringUtil.copyPartialMatches(args[4], allDatesTemp, completions);
                        return completions;
                    }
                }
            }
            //---------------------------------------------------------------------
        }

        return null;
    }
}