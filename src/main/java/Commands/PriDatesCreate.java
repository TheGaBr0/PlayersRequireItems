package Commands;

import Events.*;
import me.pri.maven.PlayersRequireItems;
import org.bukkit.command.CommandSender;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class PriDatesCreate {

    private final PlayersRequireItems plugin;
    private final CommandSender sender;
    private String activationDate;
    private String deactivationDate;
    private final String[] args;
    private boolean isPeriodic;

    int year = LocalDateTime.now().getYear();
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    public PriDatesCreate(PlayersRequireItems plugin, CommandSender sender, String[] args) {
        this.plugin = plugin;
        this.sender = sender;
        this.args = args;

        if(args.length > 4) {
            if(args[3].contains("activationDate:") && args[4].contains("deactivationDate:")){
                activationDate = args[3].substring(15);
                deactivationDate = args[4].substring(17);
            }else{
                if(args[3].contains("deactivationDate:") && args[4].contains("activationDate:")){
                    activationDate = args[4].substring(15);
                    deactivationDate = args[3].substring(17);
                }else{
                    sender.sendMessage("[§6P.R.I.§f]§7 Sintassi del comando errata!");
                    return;
                }

            }

            isPeriodic = activationDate.length() == 5 || deactivationDate.length() == 5;

            if(isPeriodic){
                if(activationDate.length() == 5)
                    activationDate += "-" + year;

                if(deactivationDate.length() == 5)
                    deactivationDate += "-" + year;
            }

            execute();
        }else{
            sender.sendMessage("[§6P.R.I.§f]§7 Troppi pochi argomenti nel comando! Riprova!");
        }
    }

    public void execute() {

        if (!plugin.checker.eventExists(args[1])) {
            if(args.length > 3){
                if(isValid(activationDate) && isValid(deactivationDate)){
                    if(!isFirstDateLaterOrEqual(activationDate, deactivationDate) || isPeriodic) {
                        Event e = new Event(args[1], plugin);

                        if(isPeriodic){
                            e.setPeriodic(true);
                        }

                        try {
                            e.setActivationDate(LocalDate.parse(activationDate, dateTimeFormatter));

                            if(isPeriodic && isFirstDateLaterOrEqual(activationDate, deactivationDate))
                                e.setDectivationDate(LocalDate.parse(deactivationDate, dateTimeFormatter).plusYears(1));
                            else
                                e.setDectivationDate(LocalDate.parse(deactivationDate, dateTimeFormatter));
                        } catch (IOException Exception) {
                            Exception.printStackTrace();
                        }

                        plugin.eventManager.addEvent(e);
                        sender.sendMessage("[§6P.R.I.§f]§7 Evento " + args[1] + " creato!");
                    }else{
                        sender.sendMessage("[§6P.R.I.§f]§7 La data di attivazione deve essere prima di quella per la disattivazione!");
                    }
                }else{
                    sender.sendMessage("[§6P.R.I.§f]§7 La data di attivazione o quella di disattivazione è invalida!");
                }
            }else{
                Event e = new Event(args[1], plugin);
                sender.sendMessage("[§6P.R.I.§f]§7 Evento " + args[1] + " creato!");
            }
        }else{
            sender.sendMessage("[§6P.R.I.§f]§7 L'evento " + args[1] + " è già stato creato!");
        }
    }

    private boolean isFirstDateLaterOrEqual(String activationDate, String deactivationDate){

        LocalDate activationLocalDate = LocalDate.parse(activationDate, dateTimeFormatter);
        LocalDate deactivationLocalDate = LocalDate.parse(deactivationDate, dateTimeFormatter);


        if(activationLocalDate.isAfter(deactivationLocalDate))
            return true;
        else{
            return activationLocalDate.isEqual(deactivationLocalDate);
        }


    }

    private boolean isValid(String date) {
        LocalDate parsedDate = null;

        try {
            parsedDate = LocalDate.parse(date, dateTimeFormatter);
        } catch (DateTimeParseException e) {
            return false;
        }
        return true;
    }

}

