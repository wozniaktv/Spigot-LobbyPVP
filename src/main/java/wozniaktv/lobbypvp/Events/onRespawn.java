package wozniaktv.lobbypvp.Events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.plugin.java.JavaPlugin;
import wozniaktv.lobbypvp.LobbyPVP;

public class onRespawn implements Listener {

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event){

        event.getPlayer().setInvisible(false);

        event.getPlayer().getInventory().setItem(JavaPlugin.getPlugin(LobbyPVP.class).getConfig().getInt("inventory.weapon.inventory-index"),JavaPlugin.getPlugin(LobbyPVP.class).get_weapon());
        event.getPlayer().setNoDamageTicks(0);
        JavaPlugin.getPlugin(LobbyPVP.class).removePlayerInventory(event.getPlayer());

    }

}
