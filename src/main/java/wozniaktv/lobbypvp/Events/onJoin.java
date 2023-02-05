package wozniaktv.lobbypvp.Events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffectType;
import wozniaktv.lobbypvp.LobbyPVP;

public class onJoin implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event){

        event.getPlayer().setInvisible(false);

        event.getPlayer().getInventory().setItem(JavaPlugin.getPlugin(LobbyPVP.class).getConfig().getInt("inventory.weapon.inventory-index"),JavaPlugin.getPlugin(LobbyPVP.class).get_weapon());
        event.getPlayer().setExp(0.9999F);
        event.getPlayer().setLevel(0);

        event.getPlayer().removePotionEffect(PotionEffectType.SLOW);

    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event){

        if(JavaPlugin.getPlugin(LobbyPVP.class).timer_stop_fighting.containsKey(event.getPlayer())){

            JavaPlugin.getPlugin(LobbyPVP.class).timer_stop_fighting.remove(event.getPlayer());
            JavaPlugin.getPlugin(LobbyPVP.class).bossBar.get(event.getPlayer()).setVisible(false);
            JavaPlugin.getPlugin(LobbyPVP.class).bossBar.replace(event.getPlayer(),null);
            JavaPlugin.getPlugin(LobbyPVP.class).bossBar.remove(event.getPlayer());
            JavaPlugin.getPlugin(LobbyPVP.class).isPlayerLeaving.remove(event.getPlayer());

        }

        JavaPlugin.getPlugin(LobbyPVP.class).ability_charging.remove(event.getPlayer());
        event.getPlayer().removePotionEffect(PotionEffectType.SLOW);

        if(JavaPlugin.getPlugin(LobbyPVP.class).cooldown_ability.containsKey(event.getPlayer())){

            JavaPlugin.getPlugin(LobbyPVP.class).cooldown_ability.remove(event.getPlayer());
            event.getPlayer().setExp(0.9999F);
            event.getPlayer().setLevel(0);




        }




    }

}
