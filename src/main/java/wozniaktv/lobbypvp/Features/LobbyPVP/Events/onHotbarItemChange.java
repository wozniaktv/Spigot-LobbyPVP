package wozniaktv.lobbypvp.Features.LobbyPVP.Events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.plugin.java.JavaPlugin;
import wozniaktv.lobbypvp.Features.LobbyPVP.LobbyPVP;

import java.util.Objects;

public class onHotbarItemChange implements Listener {


    private LobbyPVP manager;
    public onHotbarItemChange(LobbyPVP manager){
        this.manager = manager;
    }

    @EventHandler
    public void onHotbarItemChange(PlayerItemHeldEvent event){



        if(event.getPlayer().getInventory().getItem(event.getNewSlot()) == null){

            if(manager.timer_stop_fighting.containsKey(event.getPlayer())){
                manager.setPlayerLeaving(event.getPlayer());
            }

            return;
        }

        if(Objects.requireNonNull(event.getPlayer().getInventory().getItem(event.getNewSlot())).getType() == manager.get_weapon().getType()) {


            manager.addPlayerFighting(event.getPlayer());


        }else{

            if(manager.timer_stop_fighting.containsKey(event.getPlayer())){

                manager.setPlayerLeaving(event.getPlayer());

            }

        }




    }


}
