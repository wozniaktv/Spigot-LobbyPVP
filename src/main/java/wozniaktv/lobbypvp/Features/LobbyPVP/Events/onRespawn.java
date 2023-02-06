package wozniaktv.lobbypvp.Features.LobbyPVP.Events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.plugin.java.JavaPlugin;
import wozniaktv.lobbypvp.Features.LobbyPVP.LobbyPVP;
import wozniaktv.lobbypvp.Main;

public class onRespawn implements Listener {

    private LobbyPVP manager;

    public onRespawn(LobbyPVP manager){
        this.manager = manager;
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event){

        event.getPlayer().setInvisible(false);

        event.getPlayer().getInventory().setItem(JavaPlugin.getPlugin(Main.class).getConfig().getInt("inventory.weapon.inventory-index"),manager.get_weapon());
        event.getPlayer().setNoDamageTicks(0);
        manager.removePlayerInventory(event.getPlayer());

    }

}
