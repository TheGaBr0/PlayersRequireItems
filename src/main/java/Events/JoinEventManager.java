package Events;

import me.pri.maven.PlayersRequireItems;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.ArrayList;

public class JoinEventManager implements Listener {

    private final PlayersRequireItems plugin;

    public JoinEventManager(PlayersRequireItems plugin){
        this.plugin = plugin;
    }


    @EventHandler
    public void onJoin(PlayerJoinEvent event){

        if(!plugin.eventManager.getTodayEvents().isEmpty()){

            Player player = event.getPlayer();
            ArrayList<Event> events_today = plugin.eventManager.getTodayEvents();

            for(Event e : events_today){

                if(!e.hasJoined(player)){

                    if(e.getItemList() == null){
                        e.addJoinedPlayer(player);
                        sendTitle(e, false, player);
                    }else{
                        if(hasEnoughSlot(player, e.getItemList().size())){
                            e.addJoinedPlayer(player);
                            itemDropEffect(player, e);
                            sendTitle(e, false, player);
                        }else{
                            sendTitle(e, true, player);
                        }
                    }
                }
            }
        }
    }

    public void sendTitle(Event e, boolean isFull, Player player){

        if(isFull){
            player.sendTitle(e.configManager.getEventInvTitle(), e.configManager.getEventInvSubtitle(),
                    e.configManager.getEventInvFadeIn(), e.configManager.getEventInvStay() , e.configManager.getEventInvFadeOut());
        }else{
            player.sendTitle(e.configManager.getEventTitle(), e.configManager.getEventSubtitle(),
                    e.configManager.getEventFadeIn(), e.configManager.getEventStay() , e.configManager.getEventFadeOut());
        }

    }

    public void itemDropEffect(Player p, Event e){

        Location loc = p.getLocation();

        for(ItemStack item : e.getItemList()){
            p.getWorld().dropItem(loc.add(0,1,0), item);
        }

    }

    public void itemGiver(Player p, Event e){

        for(ItemStack item : e.getItemList()){
            p.getInventory().addItem(item);
        }

    }

    public boolean hasEnoughSlot(Player player, int howManyClear){
        PlayerInventory inv = player.getInventory();
        int emptySlots=0;


        for (ItemStack item: inv.getArmorContents()) {
            if(item == null){
                emptySlots--;
            }
        }


        if(inv.getItemInOffHand().getType() == Material.AIR){
            emptySlots--;
        }

        for (ItemStack item: inv.getContents()) {

            if(item == null) {
                emptySlots++;
            }
        }

        return emptySlots >= howManyClear;
    }
}
