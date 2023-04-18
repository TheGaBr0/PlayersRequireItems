package FileHandlers;

import Events.Event;
import me.pri.maven.PlayersRequireItems;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ItemsManager {

    private final String fileSeparator = System.getProperty("file.separator");
    private final PlayersRequireItems plugin;

    public ItemsManager(PlayersRequireItems plugin){
        this.plugin = plugin;
    }

    public List<ItemStack> getItemList(Event e){

        File itemsToGive = new File(plugin.getDataFolder() + fileSeparator + "Events" + fileSeparator +
                e.getName() + fileSeparator + "itemstogive.yml");

        FileConfiguration ymlItemsToGive = YamlConfiguration.loadConfiguration(itemsToGive);

        return (List<ItemStack>) ymlItemsToGive.getList("Items");

    }

    public void addItem(ItemStack itemInHand, Event e) throws IOException {


        File itemsToGive = new File(plugin.getDataFolder() + fileSeparator + "Events" + fileSeparator +
                e.getName() + fileSeparator + "itemstogive.yml");

        FileConfiguration ymlItemsToGive = YamlConfiguration.loadConfiguration(itemsToGive);

        if(e.getItemList() == null){

            List<ItemStack> items = new ArrayList<>();

            items.add(itemInHand);

            ymlItemsToGive.set("Items", items);

        }else{

            List<ItemStack> items = e.getItemList();

            items.add(itemInHand);

            ymlItemsToGive.set("Items", items);

        }

        ymlItemsToGive.save(itemsToGive);

    }

    public void removeItem(ItemStack itemToRemove, Event e) throws IOException {

        File itemsToGive = new File(plugin.getDataFolder() + fileSeparator + "Events" + fileSeparator +
                e.getName() + fileSeparator + "itemstogive.yml");

        FileConfiguration ymlItemsToGive = YamlConfiguration.loadConfiguration(itemsToGive);

        List<ItemStack> items = e.getItemList();

        items.remove(itemToRemove);

        ymlItemsToGive.set("Items", items);

        ymlItemsToGive.save(itemsToGive);

    }
}
