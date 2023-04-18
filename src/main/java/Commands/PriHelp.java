package Commands;

import me.pri.maven.PlayersRequireItems;
import org.bukkit.command.CommandSender;

public class PriHelp{

    private PlayersRequireItems plugin;
    private final String[] args;

    public PriHelp(CommandSender sender, String[] args, PlayersRequireItems plugin) {
        this.plugin = plugin;
        this.args = args;
        execute(sender);
    }

    public void execute(CommandSender sender) {

        if(sender.hasPermission("pri.help")){

            if(args.length == 1){
                sender.sendMessage(
                        "§7---[ Sezione aiuto §6P.R.I. §7]---\n" +
                                "§7- /pri §cevent list \n§7Apre la GUI che mostra le informazioni di ogni evento creato\n" +
                                "§7- /pri §cevent §aeventName §ccreate \n§7Comando per creare un evento\n" +
                                "§7- /pri §cevent §aeventName §citems \n§7Apre la GUI che mostra gli oggetti dell'evento selezionato\n" +
                                "§7- /pri §cevent §aeventName §cdelete \n§7Comando per eliminare un evento\n" +
                                "§7- /pri §cevent §aeventName §cstandby \n§7Comando per impostare un evento in standby\n" +
                                "§7- /pri §cevent §aeventName §cdates \n§7Comando per modificare le date di un evento\n" +
                                "§7- /pri §cevent §aeventName §ctitles \n§7Comando per modificare i messaggi di un evento\n" +
                                "§7\nSe cerchi qualcosa di più specifico prova con: \n/pri §chelp §ecommand§7!"
                );
            }else{
                switch(args[1]){
                    case "create":
                        sender.sendMessage(
                                "§7---[ Sezione aiuto §6P.R.I. §7]---\n" + " " +
                                        "\n§7Sintassi comando §ccreate:\n" +
                                        "§7/pri §cevent §aeventName §ccreate §e[optionalArgs] \n" + " " +
                                        "\n§eactivationDate:§amm§7-§add§7 - data nella quale si attiva l'evento\n" +
                                        "§edeactivationDate:§amm§7-§add§7 - data nella quale si disattiva l'evento\n" + " " +
                                        "\nFormattazione data: §amm§7-§add§7-yyyy\n" +
                                        "\n§8§oL'anno è facolativo, se non viene specificato l'evento verrà\n" +
                                        "impostato automaticamente su modalità periodica\n" + " " +
                                        "\n§7La modalità periodica è una proprietà di un evento che\n" +
                                        "aggiorna automaticamente l'anno di attivazione e\n" +
                                        "disattivazione di quest'ultimo."
                        );
                        break;
                    case "items":
                        sender.sendMessage(
                                "§7---[ Sezione aiuto §6P.R.I. §7]---\n" + " " +
                                        "\n§7Sintassi comando §citems:\n" +
                                        "§7/pri §cevent §aeventName §citems\n" + " " +
                                        "\n§7Apre la GUI che mostra gli oggetti dell'evento selezionato"
                        );
                        break;
                    case "delete":
                        sender.sendMessage(
                                "§7---[ Sezione aiuto §6P.R.I. §7]---\n" + " " +
                                        "\n§7Sintassi comando §cdelete:\n" +
                                        "§7/pri §cevent §aeventName §cdelete\n" + " " +
                                        "\n§7Elimina l'evento selezionato"
                        );
                        break;
                    case "standby":
                        sender.sendMessage(
                                "§7---[ Sezione aiuto §6P.R.I. §7]---\n" + " " +
                                        "\n§7Sintassi comando §cstandby:\n" +
                                        "§7/pri §cevent §aeventName §cstandby §e[true/false]\n" + " " +
                                        "\n§7Se l'evento è in standby allora non si attiverà."
                        );
                        break;
                    case "dates":
                        sender.sendMessage(
                                "§7---[ Sezione aiuto §6P.R.I. §7]---\n" + " " +
                                        "\n§7Sintassi comando §cdates:\n" +
                                        "§7/pri §cevent §aeventName §cdates §e[optionalArgs] \n" + " " +
                                        "\n§eactivationDate:§amm§7-§add§7 - data nella quale si attiva l'evento\n" +
                                        "§edeactivationDate:§amm§7-§add§7 - data nella quale si disattiva l'evento\n" + " " +
                                        "\nFormattazione data: §amm§7-§add§7-yyyy\n" +
                                        "\n§8§oL'anno è facolativo, se non viene specificato l'evento verrà\n" +
                                        "impostato automaticamente su modalità periodica\n" + " " +
                                        "\n§7La modalità periodica è una proprietà di un evento che\n" +
                                        "aggiorna automaticamente l'anno di attivazione e\n" +
                                        "disattivazione di quest'ultimo."
                        );
                        break;
                    case "titles":
                        sender.sendMessage(
                                "§7---[ Sezione aiuto §6P.R.I. §7]---\n" + " " +
                                        "\n§7Sintassi comando §ctitles:\n" +
                                        "§7/pri §cevent §aeventName §ctitles [normal/warning] §e[optionalArgs] \n" + " " +
                                        "\n§etitle: §7- titolo dell'evento (sono ammessi spazi)\n" +
                                        "\n§esubtitle: §7- sottotitolo dell'evento (sono ammessi spazi)\n" +
                                        "\n§efadeIn: §7- tempo che ci mette il titolo per apparire\n" +
                                        "\n§estay: §7- tempo con cui il titolo rimane\n" +
                                        "\n§efadeOut: §7- tempo che ci mette il titolo per scomparire\n" + " " +
                                        "\n§8§oEsempio:\n" +
                                        "§7/pri event eventName normal title:&5&lThis is the title fadeIn:4\n" +
                                        "\n§8§oQuesto comando aggiornerà titolo e fadeIn all'evento \n" +
                                        "eventName"
                        );
                        break;
                    default: sender.sendMessage("[§6P.R.I.§f]§7 Comando non riconosciuto, riprova!");
                }
            }
        }
    }
}