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

public class EventMessagesGui implements InventoryHolder, Listener {

    private final Inventory inv;
    private final PlayersRequireItems plugin;
    private Event event;
    private Object previousGui;
    private final ArrayList<Integer> protectedSlots = new ArrayList<Integer>();
    private Integer skipPerClick;

    public EventMessagesGui(PlayersRequireItems plugin) {
        inv = Bukkit.createInventory(this, 54, Component.text("Events"));
        this.plugin = plugin;
    }

    public EventMessagesGui(PlayersRequireItems plugin, Event e, Object previousGui){
        this.plugin = plugin;
        event = e;
        this.previousGui = previousGui;
        inv = Bukkit.createInventory(this, 54, Component.text(event.getName() + " - Messages"));
        skipPerClick = 1;
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

        //utilities
        inv.setItem(52, createGuiItem(Material.MAGENTA_GLAZED_TERRACOTTA, Component.text("§e§lBack")));

        switch(skipPerClick){
            case 1: inv.setItem(50, createGuiItem(Material.GREEN_TERRACOTTA, Component.text("§e§lSkip per click:§f§a 1"))); break;

            case 5: inv.setItem(50, createGuiItem(Material.ORANGE_TERRACOTTA, Component.text("§e§lSkip per click:§f§6 5"))); break;

            case 10: inv.setItem(50, createGuiItem(Material.RED_TERRACOTTA, Component.text("§e§lSkip per click:§f§c 10"))); break;
        }

        inv.setItem(48, createGuiItem(Material.LEVER, Component.text("§e§lDisplay it!"),
                Component.text("§7If you click here the title will be displayed with the given settings"), Component.text("§cNOTE: The GUI will automatically reopen!")));

        inv.setItem(46, createGuiItem(Material.REDSTONE_TORCH, Component.text("§e§lReset values!"),
                Component.text("§7If you click here fadeIn, stay and fadeOut will be reset to their default values")));

        inv.setItem(8, createGuiItem(Material.ARROW, Component.text("§e§lGo to warning message settings"), Component.text("§7The warning title is a kind of title which will appear"),
                Component.text("§7if one player has their inventory full so"), Component.text("§7items can't be added to it")));

        inv.setItem(11, createGuiItem(Material.BIRCH_SIGN, Component.text("§e§lFade in:§r§7 "+event.getEventFadeIn()),
                Component.text("§7fadeIn is the time that the title fades in for")));

        inv.setItem(13, createGuiItem(Material.OAK_SIGN, Component.text("§e§lStay in:§r§7 "+event.getEventStay()),
                Component.text("§7stay is the time the title stays for")));

        inv.setItem(15, createGuiItem(Material.JUNGLE_SIGN, Component.text("§e§lFade out:§r§7 "+event.getEventFadeOut()),
                Component.text("§7fadeOut is the time the title fades out for")));

        inv.setItem(30, createGuiItem(Material.CRIMSON_SIGN, Component.text(event.getEventTitle()),
                Component.text("§7This is the title, to modify it type /pri help titles")));

        inv.setItem(32, createGuiItem(Material.WARPED_SIGN, Component.text(event.getEventSubtitle()),
                Component.text("§7This is the subtitle, to modify it type /pri help titles")));

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

    public void editFadeIn(Integer number){

        event.setEventFadeIn(event.getEventFadeIn()-(number * -1));

    }

    public void editStay(Integer number){

        event.setEventStay(event.getEventStay()-(number * -1));

    }

    public void editFadeOut(Integer number){

        event.setEventFadeOut(event.getEventFadeOut()-(number * -1));

    }

    public void onGUIClick(Player whoClicked, int slot, ItemStack clickedItem, InventoryAction action) {

        if(clickedItem == null || clickedItem.getType().equals(Material.AIR) || plugin.eventManager.getNumberOfEvents()==0)
            return;

        if(slot == 52){
            if(previousGui instanceof EventSettingsGui){
                EventSettingsGui gui = (EventSettingsGui) previousGui;
                gui.openInventory(whoClicked);
            }else{
                plugin.eventManager.startEventsGui(whoClicked);
            }
        }

        if(slot == 50){
            switch(skipPerClick){
                case 1: skipPerClick = 5; break;

                case 5: skipPerClick = 10; break;

                case 10: skipPerClick = 1; break;
            }

            openInventory(whoClicked);
        }

        if(slot == 48){
            whoClicked.closeInventory();

            whoClicked.sendTitle(event.configManager.getEventTitle(), event.configManager.getEventSubtitle(),
                    event.configManager.getEventFadeIn(), event.configManager.getEventStay() , event.configManager.getEventFadeOut());

            Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
                @Override
                public void run() {
                    openInventory(whoClicked);
                }
            }, event.configManager.getEventFadeIn()+event.configManager.getEventStay()+event.configManager.getEventFadeOut());

        }

        if(slot == 46){
            event.setTitleDefault();
            openInventory(whoClicked);
        }

        if(slot == 11){
            if(action.equals(InventoryAction.PICKUP_HALF))
                editFadeIn(skipPerClick);
            else
                editFadeIn(-skipPerClick);

            openInventory(whoClicked);
        }

        if(slot == 13){
            if(action.equals(InventoryAction.PICKUP_HALF))
                editStay(skipPerClick);
            else
                editStay(-skipPerClick);

            openInventory(whoClicked);
        }

        if(slot == 15){
            if(action.equals(InventoryAction.PICKUP_HALF))
                editFadeOut(skipPerClick);
            else
                editFadeOut(-skipPerClick);

            openInventory(whoClicked);
        }

        if(slot == 8){
            EventMessagesGui_Inv gui = new EventMessagesGui_Inv(plugin, event, previousGui);
            gui.openInventory(whoClicked);
        }


    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if(e.getInventory().getHolder() instanceof EventMessagesGui) {
            if((e.getRawSlot() < e.getInventory().getSize())) {
                e.setCancelled(true);

                EventMessagesGui gui = (EventMessagesGui) e.getInventory().getHolder();
                gui.onGUIClick((Player)e.getWhoClicked(), e.getRawSlot(), e.getCurrentItem(), e.getAction());

            }else{
                if(e.getAction().equals(InventoryAction.MOVE_TO_OTHER_INVENTORY)){
                    e.setCancelled(true);
                }
            }
        }
    }
}
