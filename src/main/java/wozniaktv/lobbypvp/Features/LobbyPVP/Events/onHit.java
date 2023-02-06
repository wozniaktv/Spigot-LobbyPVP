package wozniaktv.lobbypvp.Features.LobbyPVP.Events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.plugin.java.JavaPlugin;
import wozniaktv.lobbypvp.Features.LobbyPVP.LobbyPVP;
import wozniaktv.lobbypvp.Main;

public class onHit implements Listener {


    private LobbyPVP manager;

    public onHit(LobbyPVP manager){
        this.manager = manager;
    }

    @EventHandler
    public void EntityDamagedByOtherEntity(EntityDamageByEntityEvent event){


        if(!(event.getEntity() instanceof Player)) return;

        if(event.getCause() == EntityDamageEvent.DamageCause.PROJECTILE && !manager.timer_stop_fighting.containsKey(((Player)event.getEntity()))){


            if(JavaPlugin.getPlugin(Main.class).getConfig().getBoolean("config.suppress-damage")){
                event.setDamage(0);
            }
            else
                event.setCancelled(true);

        }

        if(event.getEntity() instanceof Player && event.getDamager() instanceof Player){


            if(!manager.timer_stop_fighting.containsKey(((Player)event.getEntity())) || !manager.timer_stop_fighting.containsKey((Player)event.getDamager())){

                if(JavaPlugin.getPlugin(Main.class).getConfig().getBoolean("config.suppress-damage")){
                    event.setDamage(0);
                }
                else
                    event.setCancelled(true);
            }


        }

    }

    @EventHandler
    public void EntityDamageEvent(EntityDamageEvent event){

        if(!JavaPlugin.getPlugin(Main.class).getConfig().getBoolean("config.prevent-all-damage-when-pvp-off")) return;

        if(!(event.getEntity() instanceof Player)){
            return;
        }

        if(!manager.timer_stop_fighting.containsKey((Player) event.getEntity())) event.setCancelled(true);

    }




}
