package wozniaktv.lobbypvp.Features.LobbyPVP.Events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffectType;
import wozniaktv.lobbypvp.Features.LobbyPVP.LobbyPVP;
import wozniaktv.lobbypvp.Main;

public class onJoin implements Listener {

    private LobbyPVP manager;

    public onJoin(LobbyPVP manager){

        this.manager = manager;

    }


    @EventHandler
    public void onJoin(PlayerJoinEvent event){

        event.getPlayer().setInvisible(false);

        event.getPlayer().getInventory().setItem(JavaPlugin.getPlugin(Main.class).getConfig().getInt("inventory.weapon.inventory-index"),manager.get_weapon());
        event.getPlayer().setExp(0.9999F);
        event.getPlayer().setLevel(0);

        event.getPlayer().removePotionEffect(PotionEffectType.SLOW);

    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event){

        if(manager.timer_stop_fighting.containsKey(event.getPlayer())){

            manager.timer_stop_fighting.remove(event.getPlayer());
            manager.bossBar.get(event.getPlayer()).setVisible(false);
            manager.bossBar.replace(event.getPlayer(),null);
            manager.bossBar.remove(event.getPlayer());
            manager.isPlayerLeaving.remove(event.getPlayer());

        }

        manager.ability_charging.remove(event.getPlayer());
        event.getPlayer().removePotionEffect(PotionEffectType.SLOW);

        if(manager.cooldown_ability.containsKey(event.getPlayer())){

            manager.cooldown_ability.remove(event.getPlayer());
            event.getPlayer().setExp(0.9999F);
            event.getPlayer().setLevel(0);




        }




    }

}
