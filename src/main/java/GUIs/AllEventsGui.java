package GUIs;

import Events.Event;
import me.pri.maven.PlayersRequireItems;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;

public class AllEventsGui implements InventoryHolder, Listener {

    private final Inventory inv;
    private final PlayersRequireItems plugin;
    private final ArrayList<Integer> protectedSlots = new ArrayList<>();

    public AllEventsGui(PlayersRequireItems plugin) {
        inv = Bukkit.createInventory(this, 54, Component.text("Events"));
        this.plugin = plugin;
    }

    @Override
    public Inventory getInventory() {
        return inv;
    }

    public void initializeItems() {

        int leftIndex = 9;
        int rightIndex = 17;
        int i;

        String missingFiles = "§6Missing files detected! It may cause errors!";
        Event currentEvent;

        inv.clear();

        //creo bordi gui
        for(i = 0; i<54; i++){
            if(i<=9){
                inv.setItem(i, createGuiItem(Material.BLACK_STAINED_GLASS_PANE , Component.text("§f[§6P.R.I.§f]§7")));
                protectedSlots.add(i);
            }


            if(i == leftIndex){
                inv.setItem(i, createGuiItem(Material.BLACK_STAINED_GLASS_PANE , Component.text("§f[§6P.R.I.§f]§7")));
                leftIndex +=  9;
                protectedSlots.add(i);
            }

            if(i == rightIndex){
                inv.setItem(i, createGuiItem(Material.BLACK_STAINED_GLASS_PANE , Component.text("§f[§6P.R.I.§f]§7")));
                rightIndex +=  9;
                protectedSlots.add(i);
            }

            if(i>=45){
                inv.setItem(i, createGuiItem(Material.BLACK_STAINED_GLASS_PANE , Component.text("§f[§6P.R.I.§f]§7")));
                protectedSlots.add(i);
            }
        }

        if(plugin.eventManager.getNumberOfEvents()!=0){
            int indexItemList = 0;
            for(i = 0; i<54; i++){
                if(!protectedSlots.contains(i)){
                    if(indexItemList == plugin.eventManager.getNumberOfEvents()){
                        break;
                    }

                    currentEvent = plugin.eventManager.getEventAtIndex(indexItemList);

                    if(currentEvent.getIfMissingFiles()) {
                        inv.setItem(i, createGuiItem(getIcon(currentEvent), Component.text("§e" + currentEvent.getName()), Component.text(getRunningSentence(currentEvent)),
                                Component.text(getStandbySentence(currentEvent)), Component.text(getPeriodicSentence(currentEvent)),
                                Component.text("§7Activation on: "+getActivationDate(currentEvent)), Component.text("§7Deactivation on: "+getDeactivationDate(currentEvent)),
                                Component.text(missingFiles)));
                    }else{
                        inv.setItem(i, createGuiItem(getIcon(currentEvent), Component.text("§e" + currentEvent.getName()),
                                Component.text(getRunningSentence(currentEvent)), Component.text(getStandbySentence(currentEvent)), Component.text(getPeriodicSentence(currentEvent)),
                                Component.text("§7Activation on: "+getActivationDate(currentEvent)), Component.text("§7Deactivation on: "+getDeactivationDate(currentEvent))));
                    }

                    indexItemList++;
                }
            }
        }else{
            inv.setItem(22, createGuiItem(Material.BARRIER , Component.text("§l§cThere are no events created!"), Component.text("§7Type /pri help to get the commands list!")));
        }
    }

    private String getActivationDate(Event e){
        if(e.getActivationDate() == null){
            return "-";
        }else{
            DateTimeFormatter formatters = DateTimeFormatter.ofPattern("dd-MM-yyyy");

            return e.getActivationDate().format(formatters);
        }
    }

    private String getDeactivationDate(Event e){
        if(e.getDeactivationDate() == null){
            return "-";
        }else{

            DateTimeFormatter formatters = DateTimeFormatter.ofPattern("dd-MM-yyyy");

            return e.getDeactivationDate().format(formatters);
        }
    }

    private String getStandbySentence(Event e){
        if(e.isStandby())
            return "§7Stand-by mode: §2Yes";
        else
            return "§7Stand-by mode: §4No";
    }

    private String getRunningSentence(Event e){
        if(e.isRunning())
            return "§aThis event is currently running";
        else
            return "§cThis event is currently not running";
    }

    private String getPeriodicSentence(Event e){
        if(e.isPeriodic())
            return "§7Periodic mode: §2Yes";
        else
            return "§7Periodic mode: §4No";
    }

    private Material getIcon(Event e){
        if(e.getIfMissingFiles()){
            return Material.ORANGE_DYE;
        }
        if(e.isStandby())
            return Material.RED_DYE;

        return Material.PAPER;

    }

    private ItemStack createGuiItem(Material material, @Nullable TextComponent name, @Nullable TextComponent...lore) {
        ItemStack item = new ItemStack(material, 1);
        ItemMeta meta = item.getItemMeta();
        meta.displayName(name);

        ArrayList<Component> metalore = new ArrayList<Component>(Arrays.asList(lore));

        meta.lore(metalore);
        item.setItemMeta(meta);
        return item;
    }

    public void openInventory(Player p) {
        initializeItems();
        p.openInventory(inv);
    }

    public void onGUIClick(Player whoClicked, int slot, ItemStack clickedItem, InventoryAction action) {

        String cutName = "";

        if(clickedItem == null || clickedItem.getType().equals(Material.AIR) || plugin.eventManager.getNumberOfEvents()==0)
            return;

        if(!protectedSlots.contains(slot)) {
            if(clickedItem.getItemMeta().hasDisplayName())
                cutName = clickedItem.getItemMeta().getDisplayName().substring(2);

            if(action.equals(InventoryAction.PICKUP_HALF)){

                ConfirmationGui confirmationGui = new ConfirmationGui(plugin, plugin.eventManager.getEvent(cutName), clickedItem, this);
                confirmationGui.openInventory(whoClicked);

            }else{

                EventSettingsGui settingsGui = new EventSettingsGui(plugin, plugin.eventManager.getEvent(cutName), this);
                settingsGui.openInventory(whoClicked);

            }
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if(e.getInventory().getHolder() instanceof AllEventsGui) {
            if((e.getRawSlot() < e.getInventory().getSize())) {
                e.setCancelled(true);

                AllEventsGui gui = (AllEventsGui) e.getInventory().getHolder();
                gui.onGUIClick((Player)e.getWhoClicked(), e.getRawSlot(), e.getCurrentItem(), e.getAction());

            }else{
                if(e.getAction().equals(InventoryAction.MOVE_TO_OTHER_INVENTORY)){
                    e.setCancelled(true);
                }
            }
        }
    }
}
