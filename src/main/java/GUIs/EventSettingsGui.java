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

public class EventSettingsGui implements InventoryHolder, Listener {

    private Inventory inv;
    private final PlayersRequireItems plugin;
    private Event event;
    private Object previousGui;

    public EventSettingsGui(PlayersRequireItems plugin) {
        this.plugin = plugin;
    }

    public EventSettingsGui(PlayersRequireItems plugin, Event e, Object previousGui){
        this.plugin = plugin;
        this.previousGui = previousGui;
        event = e;
        inv = Bukkit.createInventory(this, 27, Component.text(event.getName()+" - Settings"));
    }

    @Override
    public Inventory getInventory() {
        return inv;
    }

    public void initializeItems() {

        inv.clear();

        //riempio tutto

        for(int i = 0; i < 27; i++){
            inv.setItem(i, createGuiItem(Material.BLACK_STAINED_GLASS_PANE, Component.text("§f[§6P.R.I.§f]§7")));
        }

        //utilities
        if(event.isStandby()){
            inv.setItem(11, createGuiItem(Material.GREEN_CONCRETE, Component.text("§e§lStand-by: §2ON"), Component.text("§7This event will not activate if this option is turned §2ON§7!")));
        }else{
            inv.setItem(11, createGuiItem(Material.RED_CONCRETE, Component.text("§e§lStand-by: §4OFF"), Component.text("§7This event will not activate if this option is turned §2ON§7!")));
        }

        inv.setItem(13, createGuiItem(Material.OAK_SIGN, Component.text("§e§lTitles"), Component.text("§7Click here to open messages management GUI")));

        inv.setItem(15, createGuiItem(Material.NETHER_STAR, Component.text("§e§lItems"), Component.text("§7Click here to open items management GUI")));

        inv.setItem(18, createGuiItem(Material.PLAYER_HEAD, Component.text("§e§lPlayers who joined the event: §6"+ event.getJoinedPlayers().size())));

        inv.setItem(26, createGuiItem(Material.MAGENTA_GLAZED_TERRACOTTA, Component.text("§e§lBack")));
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

        if(clickedItem == null || clickedItem.getType().equals(Material.AIR) || plugin.eventManager.getNumberOfEvents()==0)
            return;

        if(slot == 11){
            event.setStandby(!event.isStandby());
            openInventory(whoClicked);
        }

        if(slot == 13){
            EventMessagesGui messagesGui = new EventMessagesGui(plugin, event, this);
            messagesGui.openInventory(whoClicked);
        }

        if(slot == 15){
            EventItemsGui itemsGui = new EventItemsGui(plugin, event, this);
            itemsGui.openInventory(whoClicked);
        }

        if(slot == 26){
            AllEventsGui gui = (AllEventsGui) previousGui;

            if(gui == null)
                gui = new AllEventsGui(plugin);

            gui.openInventory(whoClicked);
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if(e.getInventory().getHolder() instanceof EventSettingsGui) {
            if((e.getRawSlot() < e.getInventory().getSize())) {
                e.setCancelled(true);

                EventSettingsGui gui = (EventSettingsGui) e.getInventory().getHolder();
                gui.onGUIClick((Player)e.getWhoClicked(), e.getRawSlot(), e.getCurrentItem(), e.getAction());

            }
        }
    }
}
