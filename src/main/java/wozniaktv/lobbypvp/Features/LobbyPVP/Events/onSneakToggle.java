package wozniaktv.lobbypvp.Features.LobbyPVP.Events;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import wozniaktv.lobbypvp.Features.LobbyPVP.LobbyPVP;
import wozniaktv.lobbypvp.Main;

import java.util.Objects;

public class onSneakToggle implements Listener {

    private LobbyPVP manager;

    public onSneakToggle(LobbyPVP manager){
        this.manager = manager;
    }

    @EventHandler
    public void onSneakToggle(PlayerToggleSneakEvent event){


        if(!manager.timer_stop_fighting.containsKey(event.getPlayer())) return;

        if(event.isSneaking()){


            if(manager.cooldown_ability.containsKey(event.getPlayer())){

                event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.ENTITY_VILLAGER_NO,100F,1F);
                event.getPlayer().sendTitle(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(JavaPlugin.getPlugin(Main.class).getConfig().getString("config.warning-ability-message.title"))),ChatColor.translateAlternateColorCodes('&',Objects.requireNonNull(JavaPlugin.getPlugin(Main.class).getConfig().getString("config.warning-ability-message.subtitle"))),0,20,0);
                return;

            }

            manager.ability_charging.put(event.getPlayer(),0);
            event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SLOW,222222,0));

        }else{

            manager.ability_charging.remove(event.getPlayer());
            event.getPlayer().removePotionEffect(PotionEffectType.SLOW);
            event.getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(""));

        }


    }


}
