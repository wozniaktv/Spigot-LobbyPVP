package wozniaktv.lobbypvp.Features.LobbyPVP.Events;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import wozniaktv.lobbypvp.Features.LobbyPVP.LobbyPVP;
import wozniaktv.lobbypvp.Main;

import java.util.List;
import java.util.Objects;

public class onRightClick implements Listener {


    private LobbyPVP manager;

    public onRightClick(LobbyPVP manager){
        this.manager = manager;
    }

    @EventHandler
    public void RightClick(PlayerInteractEvent event){

        if(!manager.timer_stop_fighting.containsKey(event.getPlayer())) return;

        if((event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) && (event.getPlayer().getInventory().getItemInMainHand()).getType()== manager.get_weapon().getType()){


            if(manager.cooldown_ability.containsKey(event.getPlayer())){

                event.getPlayer().playSound(event.getPlayer().getLocation(),Sound.ENTITY_VILLAGER_NO,100F,1F);
                event.getPlayer().sendTitle(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(JavaPlugin.getPlugin(Main.class).getConfig().getString("config.warning-ability-message.title"))),ChatColor.translateAlternateColorCodes('&',Objects.requireNonNull(JavaPlugin.getPlugin(Main.class).getConfig().getString("config.warning-ability-message.subtitle"))),0,20,0);

                return;
            }

            if(!manager.ability_charging.containsKey(event.getPlayer())){

                event.getPlayer().playSound(event.getPlayer().getLocation(),Sound.ENTITY_VILLAGER_NO,100F,1F);
                event.getPlayer().sendTitle(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(JavaPlugin.getPlugin(Main.class).getConfig().getString("config.mustcharge-ability-message.title"))),ChatColor.translateAlternateColorCodes('&',Objects.requireNonNull(JavaPlugin.getPlugin(Main.class).getConfig().getString("config.mustcharge-ability-message.subtitle"))),0,20,0);

                return;
            }

            if(manager.ability_charging.get(event.getPlayer()) < JavaPlugin.getPlugin(Main.class).getConfig().getInt("config.ability-needed-charge")){

                event.getPlayer().playSound(event.getPlayer().getLocation(),Sound.ENTITY_VILLAGER_NO,100F,1F);
                event.getPlayer().sendTitle(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(JavaPlugin.getPlugin(Main.class).getConfig().getString("config.mustcharge-ability-message.title"))),ChatColor.translateAlternateColorCodes('&',Objects.requireNonNull(JavaPlugin.getPlugin(Main.class).getConfig().getString("config.mustcharge-ability-message.subtitle"))),0,20,0);

                return;

            }

            event.getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(""));
            manager.ability_charging.remove(event.getPlayer());


            event.getPlayer().getWorld().playSound(event.getPlayer().getLocation(),Sound.ENTITY_WARDEN_SONIC_CHARGE,100F,1F);

            new BukkitRunnable() {
                double t = 0;
                Location location = event.getPlayer().getLocation();
                Boolean isPassableBlock = true;
                Vector direction = location.getDirection().normalize();

                public void run() {

                    location = event.getPlayer().getLocation();
                    Vector direction = location.getDirection().normalize();

                    t = t + 4.3;

                    double x = direction.getX() * t;

                    double y = direction.getY() * t + 1.4;

                    double z = direction.getZ() * t;

                    location.add(x, y, z);


                    location.getWorld().spawnParticle(Particle.SONIC_BOOM, location, 1, 0, 0, 0, 0);
                    location.getWorld().spawnParticle(Particle.ASH, location, 30, 0.15, 0.15, 0.15, 0);
                    location.getWorld().spawnParticle(Particle.ELECTRIC_SPARK, location, 30, 0.15, 0.15, 0.15, 0);

                    if (!location.getBlock().isPassable()) {

                        isPassableBlock = false;

                    }

                    location.getWorld().playSound(location, Sound.ENTITY_WARDEN_SONIC_BOOM, 0.6F, 0.9F);

                    List<Entity> entities = (List<Entity>) location.getWorld().getNearbyEntities(location, 2, 2, 2);

                    for (Entity entity : entities) {

                        if (entity instanceof Player){

                            Player player = (Player) entity;

                            if (!(player.getUniqueId() == event.getPlayer().getUniqueId())) {

                                if(manager.timer_stop_fighting.containsKey(player)){

                                    player.damage(JavaPlugin.getPlugin(Main.class).getConfig().getInt("config.ability-damage"));
                                    location.getWorld().spawnParticle(Particle.CLOUD, player.getLocation(), 50, 1, 1, 1, 0);
                                    this.cancel();

                                }


                            }

                        }
                    }

                    location.subtract(x, y, z);

                    if (t > 150 || !isPassableBlock) { //int here is length
                        this.cancel();
                    }
                }
            }.runTaskTimer(JavaPlugin.getPlugin(Main.class), 30, 1);

            manager.cooldown_ability.put(event.getPlayer(),JavaPlugin.getPlugin(Main.class).getConfig().getInt("config.ability-cooldown"));
            event.getPlayer().setExp(0);
            event.getPlayer().setLevel(JavaPlugin.getPlugin(Main.class).getConfig().getInt("config.ability-cooldown"));



        }

    }

}
