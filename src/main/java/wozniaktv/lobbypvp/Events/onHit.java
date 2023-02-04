package wozniaktv.lobbypvp.Events;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;
import wozniaktv.lobbypvp.LobbyPVP;

public class onHit implements Listener {


    @EventHandler
    public void EntityDamagedByOtherEntity(EntityDamageByEntityEvent event){


        if(!(event.getEntity() instanceof Player)) return;

        if(event.getCause() == EntityDamageEvent.DamageCause.PROJECTILE && !JavaPlugin.getPlugin(LobbyPVP.class).timer_stop_fighting.containsKey(((Player)event.getEntity()))){


            if(JavaPlugin.getPlugin(LobbyPVP.class).getConfig().getBoolean("config.suppress-damage")){
                event.setDamage(0);
            }
            else
                event.setCancelled(true);

        }

        if(event.getEntity() instanceof Player && event.getDamager() instanceof Player){


            if(!JavaPlugin.getPlugin(LobbyPVP.class).timer_stop_fighting.containsKey(((Player)event.getEntity())) || !JavaPlugin.getPlugin(LobbyPVP.class).timer_stop_fighting.containsKey((Player)event.getDamager())){

                if(JavaPlugin.getPlugin(LobbyPVP.class).getConfig().getBoolean("config.suppress-damage")){
                    event.setDamage(0);
                }
                else
                    event.setCancelled(true);
            }


        }

    }

    @EventHandler
    public void EntityDamageEvent(EntityDamageEvent event){

        if(!JavaPlugin.getPlugin(LobbyPVP.class).getConfig().getBoolean("config.prevent-all-damage-when-pvp-off")) return;

        if(!(event.getEntity() instanceof Player)){
            return;
        }

        if(!JavaPlugin.getPlugin(LobbyPVP.class).timer_stop_fighting.containsKey((Player) event.getEntity())) event.setCancelled(true);

    }




}
