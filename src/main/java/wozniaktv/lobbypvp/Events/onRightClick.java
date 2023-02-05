package wozniaktv.lobbypvp.Events;
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
import wozniaktv.lobbypvp.LobbyPVP;

import java.util.List;
import java.util.Objects;

public class onRightClick implements Listener {

    @EventHandler
    public void RightClick(PlayerInteractEvent event){

        if(!JavaPlugin.getPlugin(LobbyPVP.class).timer_stop_fighting.containsKey(event.getPlayer())) return;

        if((event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) && (event.getPlayer().getInventory().getItemInMainHand()).getType()== JavaPlugin.getPlugin(LobbyPVP.class).get_weapon().getType()){


            if(JavaPlugin.getPlugin(LobbyPVP.class).cooldown_ability.containsKey(event.getPlayer())){

                event.getPlayer().playSound(event.getPlayer().getLocation(),Sound.ENTITY_VILLAGER_NO,100F,1F);
                event.getPlayer().sendTitle(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(JavaPlugin.getPlugin(LobbyPVP.class).getConfig().getString("config.warning-ability-message.title"))),ChatColor.translateAlternateColorCodes('&',Objects.requireNonNull(JavaPlugin.getPlugin(LobbyPVP.class).getConfig().getString("config.warning-ability-message.subtitle"))),0,20,0);

                return;
            }

            if(!JavaPlugin.getPlugin(LobbyPVP.class).ability_charging.containsKey(event.getPlayer())){

                event.getPlayer().playSound(event.getPlayer().getLocation(),Sound.ENTITY_VILLAGER_NO,100F,1F);
                event.getPlayer().sendTitle(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(JavaPlugin.getPlugin(LobbyPVP.class).getConfig().getString("config.mustcharge-ability-message.title"))),ChatColor.translateAlternateColorCodes('&',Objects.requireNonNull(JavaPlugin.getPlugin(LobbyPVP.class).getConfig().getString("config.mustcharge-ability-message.subtitle"))),0,20,0);

                return;
            }

            if(JavaPlugin.getPlugin(LobbyPVP.class).ability_charging.get(event.getPlayer()) < JavaPlugin.getPlugin(LobbyPVP.class).getConfig().getInt("config.ability-needed-charge")){

                event.getPlayer().playSound(event.getPlayer().getLocation(),Sound.ENTITY_VILLAGER_NO,100F,1F);
                event.getPlayer().sendTitle(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(JavaPlugin.getPlugin(LobbyPVP.class).getConfig().getString("config.mustcharge-ability-message.title"))),ChatColor.translateAlternateColorCodes('&',Objects.requireNonNull(JavaPlugin.getPlugin(LobbyPVP.class).getConfig().getString("config.mustcharge-ability-message.subtitle"))),0,20,0);

                return;

            }

            event.getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(""));
            JavaPlugin.getPlugin(LobbyPVP.class).ability_charging.remove(event.getPlayer());


            new BukkitRunnable() {
                double t = 0;
                Location location = event.getPlayer().getLocation();
                Boolean isPassableBlock = true;
                Vector direction = location.getDirection().normalize();

                public void run() {
                    t = t + 4.3;

                    double x = direction.getX() * t;

                    double y = direction.getY() * t + 1.4;

                    double z = direction.getZ() * t;

                    location.add(x, y, z);


                    location.getWorld().spawnParticle(Particle.SONIC_BOOM, location, 1, 0, 0, 0, 0);
                    location.getWorld().spawnParticle(Particle.FLAME, location, 15, 0.5, 0.5, 0.5, 0);
                    location.getWorld().spawnParticle(Particle.ELECTRIC_SPARK, location, 15, 0.5, 0.5, 0.5, 0);

                    if (!location.getBlock().isPassable()) {

                        isPassableBlock = false;

                    }

                    location.getWorld().playSound(location, Sound.ENTITY_WARDEN_SONIC_BOOM, 0.6F, 0.9F);

                    List<Entity> entities = (List<Entity>) location.getWorld().getNearbyEntities(location, 2, 2, 2);

                    for (Entity entity : entities) {

                        if (entity instanceof Player){

                            Player player = (Player) entity;

                            if (!(player.getUniqueId() == event.getPlayer().getUniqueId())) {

                                if(JavaPlugin.getPlugin(LobbyPVP.class).timer_stop_fighting.containsKey(player)){

                                    player.damage(JavaPlugin.getPlugin(LobbyPVP.class).getConfig().getInt("config.ability-damage"));
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
            }.runTaskTimer(JavaPlugin.getPlugin(LobbyPVP.class), 0, 1);

            for(Player p : JavaPlugin.getPlugin(LobbyPVP.class).getServer().getOnlinePlayers()){

                p.playSound(event.getPlayer().getLocation(), Sound.ENTITY_ARROW_SHOOT,100f,1.2f);

            }

            JavaPlugin.getPlugin(LobbyPVP.class).cooldown_ability.put(event.getPlayer(),JavaPlugin.getPlugin(LobbyPVP.class).getConfig().getInt("config.ability-cooldown"));
            event.getPlayer().setExp(0);
            event.getPlayer().setLevel(JavaPlugin.getPlugin(LobbyPVP.class).getConfig().getInt("config.ability-cooldown"));



        }

    }

}
