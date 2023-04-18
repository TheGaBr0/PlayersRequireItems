package Commands;

import Events.*;
import me.pri.maven.PlayersRequireItems;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class PriTitles {

    private final PlayersRequireItems plugin;
    private final Event e;
    private final CommandSender sender;
    private final String[] args;

    private final String titleType;

    private String title = null;
    private String subtitle = null;
    private String stay = null;
    private String fadeIn = null;
    private String fadeOut = null;

    private final int TITLELENGHT = 80;

    StringBuilder finalMessage = new StringBuilder();

    final List<String> titlesInputs = new ArrayList<String>(){
        {
            add("title:");
            add("subtitle:");
            add("fadeIn:");
            add("stay:");
            add("fadeOut:");
        }
    };


    public PriTitles(PlayersRequireItems plugin, CommandSender sender, String[] args, Event e){
        this.plugin = plugin;
        this.sender = sender;
        this.e = e;
        this.args = args;

        titleType = args[3];
        int max = 0;

        if(!(titleType.equals("normal") || titleType.equals("warning"))){
            sender.sendMessage("[§6P.R.I.§f]§7 Comando non riconosciuto, prova con /pri §5help!");
            return;
        }

        for (String s : args) {
            if(max < titlesInputs.toArray().length){
                boolean StringEmpty = s.substring(s.indexOf(":") + 1).equals("");
                if (s.contains("title:") && s.startsWith("title:")){

                    if(StringEmpty)
                        title = null;
                    else
                        title = s.substring(s.indexOf(":") + 1) + titleFinder("title:");

                    max++;
                }

                if (s.contains("subtitle:") && s.startsWith("subtitle:")){

                    if(StringEmpty)
                        subtitle = null;
                    else
                        subtitle = s.substring(s.indexOf(":") + 1) + titleFinder("subtitle:");

                    max++;
                }

                if (s.contains("stay:") && s.startsWith("stay:")){

                    if(StringEmpty)
                        stay = null;
                    else
                        stay = s.substring(s.indexOf(":") + 1);

                    max++;
                }

                if (s.contains("fadeIn:") && s.startsWith("fadeIn:")){

                    if(StringEmpty)
                        fadeIn = null;
                    else
                        fadeIn = s.substring(s.indexOf(":") + 1);

                    max++;
                }

                if (s.contains("fadeOut:") && s.startsWith("fadeOut:")){

                    if(StringEmpty)
                        fadeOut = null;
                    else
                        fadeOut = s.substring(s.indexOf(":") + 1);

                    max++;
                }

            }else
                break;
        }

        execute();
    }

    private void execute() {

        if(title==null && subtitle == null && stay == null && fadeIn == null && fadeOut == null){
            sender.sendMessage("[§6P.R.I.§f]§7 Nessun valore è stato cambiato!");
            return;
        }
        finalMessage.append("[§6P.R.I.§f]§7-----Titolo evento ").append(e.getName()).append(" aggiornato-----§r[§6P.R.I.§f]").append("\n");

        if(titleType.equals("normal"))
            finalMessage.append("                       §7-Titolo Normale-").append("\n").append(" ").append("\n");
        else
            finalMessage.append("                       §7-Titolo Warning-").append("\n").append(" ").append("\n");

        if(title!=null){
            if(title.length() <= TITLELENGHT){
                if(titleType.equals("normal")){
                    e.setEventTitle(ChatColor.translateAlternateColorCodes('&', title));
                    finalMessage.append("§etitolo§7 aggiornato a: ").append(ChatColor.translateAlternateColorCodes('&', title)).append("§7").append("\n");
                }
                else{
                    e.setEventInvTitle(ChatColor.translateAlternateColorCodes('&', title));
                    finalMessage.append("§etitolo§7 aggiornato a: ").append(ChatColor.translateAlternateColorCodes('&', title)).append("§7").append("\n");
                }
            }else{
                sender.sendMessage("[§6P.R.I.§f]§7 Il titolo è troppo lungo! Massimo "+TITLELENGHT+" caratteri");
                return;
            }
        }

        if(subtitle!=null){
            if(subtitle.length() <= TITLELENGHT){
                if(titleType.equals("normal")){
                    e.setEventSubtitle(ChatColor.translateAlternateColorCodes('&', subtitle));
                    finalMessage.append("§esottotitolo§7 aggiornato a: ").append(ChatColor.translateAlternateColorCodes('&', subtitle)).append("§7").append("\n");
                }
                else{
                    e.setEventInvSubtitle(ChatColor.translateAlternateColorCodes('&', subtitle));
                    finalMessage.append("§esottotitolo§7 aggiornato a: ").append(ChatColor.translateAlternateColorCodes('&', subtitle)).append("§7").append("\n");
                }
            }else{
                sender.sendMessage("[§6P.R.I.§f]§7 Il sottotitolo è troppo lungo! Massimo "+TITLELENGHT+" caratteri");
                return;
            }
        }

        if(stay!=null){
            if(checkInteger(stay)){
                if(titleType.equals("normal")){
                    e.setEventStay(Integer.parseInt(stay));
                    finalMessage.append("§estay§7 aggiornato a: ").append(stay).append("\n");
                }else{
                    e.setEventInvStay(Integer.parseInt(stay));
                    finalMessage.append("§estay§7 aggiornato a: ").append(stay).append("\n");
                }
            }else{
                sender.sendMessage("[§6P.R.I.§f]§7 I valori di stay, fadeIn e fadeOut devono essere numeri!");
                return;
            }
        }

        if(fadeIn!=null){
            if(checkInteger(fadeIn)){
                if(titleType.equals("normal")){
                    e.setEventFadeIn(Integer.parseInt(fadeIn));
                    finalMessage.append("§efadeIn§7 aggiornato a: ").append(fadeIn).append("\n");
                }else{
                    e.setEventInvFadeIn(Integer.parseInt(fadeIn));
                    finalMessage.append("§efadeIn§7 aggiornato a: ").append(fadeIn).append("\n");
                }
            }
            else{
                sender.sendMessage("[§6P.R.I.§f]§7 I valori di stay, fadeIn e fadeOut devono essere numeri!");
                return;
            }
        }

        if(fadeOut!=null){
            if(checkInteger(fadeOut)){
                if(titleType.equals("normal")){
                    e.setEventFadeOut(Integer.parseInt(fadeOut));
                    finalMessage.append("§efadeOut§7 aggiornato a: ").append(fadeOut).append("\n");
                }else{
                    e.setEventInvFadeOut(Integer.parseInt(fadeOut));
                    finalMessage.append("§efadeOut§7 aggiornato a: ").append(fadeOut).append("\n");
                }
            }else{
                sender.sendMessage("[§6P.R.I.§f]§7 I valori di stay, fadeIn e fadeOut devono essere numeri!");
                return;
            }
        }

        sender.sendMessage(finalMessage.toString());
    }

    private String titleFinder(String variable){

        int indexOfTitle = 0;
        int indexOfArg;
        int i = 4; //index da dove partono i valori di titlesInputs
        boolean notFound = true;
        StringBuilder titleBuilder = new StringBuilder();

        //trovo posizione del titolo o sottotitolo
        while(i < args.length && notFound){
            if(args[i].contains(variable) && args[i].startsWith(variable)){
                notFound = false;
                indexOfTitle = i;
            }else{
                i++;
            }
        }

        //aggiungo al titolo ogni argomento dopo title: o subtitle: finchè non trovo un altro comando come stay:

        indexOfArg = indexOfTitle+1;
        boolean nextArg = true;
        while(indexOfArg < args.length && nextArg){

            for(String input : titlesInputs) {
                if ((args[indexOfArg].contains(input) && args[indexOfArg].startsWith(input))) {
                    nextArg = false;
                    break;
                }
            }

            if(nextArg){
                titleBuilder.append(" ").append(args[indexOfArg]);
            }

            indexOfArg++;

        }


        return titleBuilder.toString();
    }

    private boolean checkInteger(String s){
        try
        {
            Integer.parseInt(s);
            return true;
        } catch (NumberFormatException ex)
        {
            return false;
        }
    }

}