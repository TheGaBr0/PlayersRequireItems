package GUIs;

import Events.Event;
import me.pri.maven.PlayersRequireItems;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
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

public class ConfirmationGui implements InventoryHolder, Listener {

    private Inventory inv;
    private final PlayersRequireItems plugin;
    private ItemStack itemToRemove;
    private Object previousGui;
    private Event e;

    public ConfirmationGui(PlayersRequireItems plugin) {
        this.plugin = plugin;
    }

    public ConfirmationGui(PlayersRequireItems plugin, Event e, ItemStack itemToRemove, Object gui){

        this.plugin = plugin;

        inv = Bukkit.createInventory(this, 27, Component.text("Confirm removal"));

        this.e = e;

        this.itemToRemove = itemToRemove;

        this.previousGui = gui;

    }

    @Override
    public Inventory getInventory() {
        return inv;
    }


    public void initializeItems() {

        //riempio tutto

        for(int i = 0; i < 27; i++){
            inv.setItem(i, createGuiItem(new ItemStack(Material.BLACK_STAINED_GLASS_PANE, 1) , Component.text("§f[§6P.R.I.§f]§7")));
        }

        //inserisco si e  no
        inv.setItem(11, createGuiItem(new ItemStack(Material.GREEN_CONCRETE, 1) , Component.text("§2§lYes")));
        inv.setItem(15, createGuiItem(new ItemStack(Material.RED_CONCRETE, 1) , Component.text("§4§lNo")));
        //inserisco oggetto da eliminare
        inv.setItem(13, itemToRemove);


    }


    private ItemStack createGuiItem(ItemStack item, @Nullable TextComponent name, @Nullable TextComponent...lore) {
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

    public void onGUIClick(Player whoClicked, int slot) {

        if(slot == 11) {

            if(previousGui instanceof EventItemsGui){
                EventItemsGui gui = (EventItemsGui) previousGui;
                e.removeItem(itemToRemove);
                gui.openInventory(whoClicked);

            }else{
                AllEventsGui gui = (AllEventsGui) previousGui;
                e.kill();
                gui.openInventory(whoClicked);
            }
        }

        if(slot == 15){

            if(previousGui instanceof EventItemsGui){
                EventItemsGui gui = (EventItemsGui) previousGui;
                gui.openInventory(whoClicked);
            }else{
                AllEventsGui gui = (AllEventsGui) previousGui;
                gui.openInventory(whoClicked);
            }

        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if(e.getInventory().getHolder() instanceof ConfirmationGui) {
            if((e.getRawSlot() < e.getInventory().getSize())) {
                e.setCancelled(true);
                ConfirmationGui gui = (ConfirmationGui) e.getInventory().getHolder();
                gui.onGUIClick((Player)e.getWhoClicked(), e.getRawSlot());
            }
        }
    }
}
