package wozniaktv.lobbypvp.Events;
import org.bukkit.*;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.AbstractArrow;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.util.Vector;
import wozniaktv.lobbypvp.LobbyPVP;

import java.util.Objects;

public class onRightClick implements Listener {

    @EventHandler
    public void RightClick(PlayerInteractEvent event){

        if(!JavaPlugin.getPlugin(LobbyPVP.class).timer_stop_fighting.containsKey(event.getPlayer())) return;

        if((event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) && (event.getPlayer().getInventory().getItemInMainHand()).getType()== JavaPlugin.getPlugin(LobbyPVP.class).get_weapon().getType()){


            if(JavaPlugin.getPlugin(LobbyPVP.class).cooldown_arrow.containsKey(event.getPlayer())){

                event.getPlayer().playSound(event.getPlayer().getLocation(),Sound.ENTITY_VILLAGER_NO,100F,1F);
                event.getPlayer().sendTitle(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(JavaPlugin.getPlugin(LobbyPVP.class).getConfig().getString("config.warning-ability-message.title"))),ChatColor.translateAlternateColorCodes('&',Objects.requireNonNull(JavaPlugin.getPlugin(LobbyPVP.class).getConfig().getString("config.warning-ability-message.subtitle"))),0,20,0);

                return;
            }

            double pitch = ((event.getPlayer().getLocation().getPitch() + 90) * Math.PI) / 180;
            double yaw  = ((event.getPlayer().getLocation().getYaw() + 90)  * Math.PI) / 180;

            double x = Math.sin(pitch) * Math.cos(yaw);
            double y = Math.sin(pitch) * Math.sin(yaw);
            double z = Math.cos(pitch);

            Vector vector = new Vector(x, z, y);
            float speed = JavaPlugin.getPlugin(LobbyPVP.class).getConfig().getInt("config.ability-speed");
            Arrow arrow = event.getPlayer().getWorld().spawnArrow(event.getPlayer().getLocation().add(0,1,0),vector,speed,0f);
            arrow.setDamage(JavaPlugin.getPlugin(LobbyPVP.class).getConfig().getInt("config.ability-damage"));
            arrow.setKnockbackStrength(JavaPlugin.getPlugin(LobbyPVP.class).getConfig().getInt("config.ability-knockback"));
            arrow.setPickupStatus(AbstractArrow.PickupStatus.DISALLOWED);
            arrow.setShooter(event.getPlayer());

            for(Player p : JavaPlugin.getPlugin(LobbyPVP.class).getServer().getOnlinePlayers()){

                p.playSound(event.getPlayer().getLocation(), Sound.ENTITY_ARROW_SHOOT,100f,1.2f);

            }

            JavaPlugin.getPlugin(LobbyPVP.class).cooldown_arrow.put(event.getPlayer(),JavaPlugin.getPlugin(LobbyPVP.class).getConfig().getInt("config.ability-cooldown"));
            event.getPlayer().setExp(0);
            event.getPlayer().setLevel(JavaPlugin.getPlugin(LobbyPVP.class).getConfig().getInt("config.ability-cooldown"));



        }

    }

}
