package wozniaktv.lobbypvp.Events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import wozniaktv.lobbypvp.LobbyPVP;

public class onArrowHit implements Listener {

    @EventHandler
    public void ArrowHit(ProjectileHitEvent event){


        if(event.getEntity().getShooter() == event.getHitEntity()) event.setCancelled(true);


    }

}
