package wozniaktv.lobbypvp.Events;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedMainHandEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerItemMendEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import wozniaktv.lobbypvp.LobbyPVP;

import java.util.Objects;

import static org.bukkit.Material.NETHERITE_SWORD;

public class onHotbarItemChange implements Listener {

    @EventHandler
    public void onHotbarItemChange(PlayerItemHeldEvent event){



        if(event.getPlayer().getInventory().getItem(event.getNewSlot()) == null){

            if(JavaPlugin.getPlugin(LobbyPVP.class).timer_stop_fighting.containsKey(event.getPlayer())){
                JavaPlugin.getPlugin(LobbyPVP.class).setPlayerLeaving(event.getPlayer());
            }

            return;
        }

        if(Objects.requireNonNull(event.getPlayer().getInventory().getItem(event.getNewSlot())).getType() == JavaPlugin.getPlugin(LobbyPVP.class).get_weapon().getType()) {


            JavaPlugin.getPlugin(LobbyPVP.class).addPlayerFighting(event.getPlayer());


        }else{

            if(JavaPlugin.getPlugin(LobbyPVP.class).timer_stop_fighting.containsKey(event.getPlayer())){

                JavaPlugin.getPlugin(LobbyPVP.class).setPlayerLeaving(event.getPlayer());

            }

        }




    }


}
