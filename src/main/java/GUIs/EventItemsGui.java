package GUIs;

import Events.Event;
import me.pri.maven.PlayersRequireItems;
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
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EventItemsGui implements InventoryHolder, Listener {

    private Inventory inv;
    private final PlayersRequireItems plugin;
    private Event event;
    private Object previousGui;
    private final ArrayList<Integer> protectedSlots = new ArrayList<Integer>();


    public EventItemsGui(PlayersRequireItems plugin) {
        this.plugin = plugin;
    }

    public EventItemsGui(PlayersRequireItems plugin, Event e, Object previousGui){
        this.plugin = plugin;
        event = e;
        this.previousGui = previousGui;
        inv = Bukkit.createInventory(this, 54, Component.text(event.getName() + " - Items"));
    }

    @Override
    public Inventory getInventory() {
        return inv;
    }

    public void initializeItems() {

        int leftIndex = 9;
        int rightIndex = 17;
        int i;

        inv.clear();

        //Crea bordi interfaccia

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
        //inserimento utilities

        inv.setItem(47, createGuiItem(Material.BARRIER, Component.text("§e§lRemove everything")));

        inv.setItem(51, createGuiItem(Material.MAGENTA_GLAZED_TERRACOTTA, Component.text("§e§lBack")));

        //Inserimento oggetti dell'evento

        if(!(event.getItemList() == null)){
            int indexItemList = 0;
            for(i = 0; i<54; i++){
                if(!protectedSlots.contains(i)){
                    if(indexItemList == event.getItemList().size()){
                        break;
                    }

                    inv.setItem(i, event.getItemList().get(indexItemList));
                    indexItemList++;
                }
            }
        }

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


    public void onGUIClick(Player whoClicked, int slot, ItemStack clickedItem) {

        if(clickedItem == null || clickedItem.getType().equals(Material.AIR))
            return;

        if(slot == 47){
            clearItems(whoClicked);
        }

        if(slot == 51){
            if(previousGui instanceof EventSettingsGui){
                EventSettingsGui gui = (EventSettingsGui) previousGui;
                gui.openInventory(whoClicked);
            }else{
                plugin.eventManager.startEventsGui(whoClicked);
            }
        }

        if(!protectedSlots.contains(slot)) {
            ConfirmationGui confirmationGui = new ConfirmationGui(plugin, event, clickedItem, this);
            confirmationGui.openInventory(whoClicked);
        }
    }

    public void onGUIAddition(Player whoClicked, InventoryAction action, ItemStack clickedItem) {
        final boolean placing = action.equals(InventoryAction.PLACE_ONE)
                || action.equals(InventoryAction.PLACE_ALL)
                || action.equals(InventoryAction.PLACE_SOME);

        if(!(event.getItemList() == null)){
            if(!(event.getItemList().size() == 28)){
                if(clickedItem != null){
                    event.addItem(clickedItem);
                    if(!placing)
                        openInventory(whoClicked);
                }
            }else{
                whoClicked.sendMessage("[§6P.R.I.§f]§7 Puoi inserire fino ad un massimo di 28 oggetti per evento!");
            }
        }else{
            if(clickedItem != null){
                event.addItem(clickedItem);
                if(!placing)
                    openInventory(whoClicked);
            }
        }
    }

    public void clearItems(Player whoClicked){
        List<ItemStack> temp;

        if(!(event.getItemList() == null || event.getItemList().isEmpty())){
            temp = event.getItemList();
            for (ItemStack itemStack : temp) {
                event.removeItem(itemStack);
                openInventory(whoClicked);
            }
        }else{
            whoClicked.sendMessage("[§6P.R.I.§f]§7 Questo evento non ha oggetti da rimuovere!");
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if(e.getInventory().getHolder() instanceof EventItemsGui) {
            if((e.getRawSlot() < e.getInventory().getSize())){
                if(e.getSlot() == 49 || e.getSlot() == 47) {
                    e.setCancelled(true);
                    EventItemsGui gui = (EventItemsGui) e.getInventory().getHolder();
                    gui.onGUIClick((Player) e.getWhoClicked(), e.getRawSlot(), e.getCurrentItem());
                }
                else {
                    if (e.getAction().equals(InventoryAction.PICKUP_ALL)) {
                        e.setCancelled(true);
                        EventItemsGui gui = (EventItemsGui) e.getInventory().getHolder();
                        gui.onGUIClick((Player) e.getWhoClicked(), e.getRawSlot(), e.getCurrentItem());
                    } else {
                        if (e.getAction().equals(InventoryAction.PLACE_ONE)
                                || e.getAction().equals(InventoryAction.PLACE_ALL)
                                || e.getAction().equals(InventoryAction.PLACE_SOME)) {

                            EventItemsGui gui = (EventItemsGui) e.getInventory().getHolder();
                            gui.onGUIAddition((Player) e.getWhoClicked(), e.getAction(), e.getWhoClicked().getItemOnCursor());
                        } else {
                            e.setCancelled(true);
                        }
                    }
                }
            }else{
                if(e.getAction().equals(InventoryAction.MOVE_TO_OTHER_INVENTORY)){
                    e.setCancelled(true);
                    EventItemsGui gui = (EventItemsGui) e.getInventory().getHolder();
                    gui.onGUIAddition((Player)e.getWhoClicked(), e.getAction(), e.getCurrentItem());
                }
            }
        }
    }
}
