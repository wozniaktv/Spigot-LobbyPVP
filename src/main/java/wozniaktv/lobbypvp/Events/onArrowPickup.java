package wozniaktv.lobbypvp.Events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPickupArrowEvent;

public class onArrowPickup implements Listener {

    @EventHandler
    public void onArrowPickup(PlayerPickupArrowEvent event){

        event.setCancelled(true);

    }

}
