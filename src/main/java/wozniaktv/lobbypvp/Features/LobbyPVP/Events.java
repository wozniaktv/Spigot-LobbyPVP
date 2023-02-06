package wozniaktv.lobbypvp.Features.LobbyPVP;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.*;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import wozniaktv.lobbypvp.Main;

import java.util.Objects;

public class Events implements Listener {

    private LobbyPVP manager = null;
    public Events(LobbyPVP manager){

        this.manager = manager;

    }

    @EventHandler
    public void onDeathEntity(EntityDeathEvent event){

        event.getDrops().clear();

    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDeath(PlayerDeathEvent event){


        event.getEntity().setInvisible(true);
        LivingEntity animation_entity = (LivingEntity) event.getEntity().getLocation().getWorld().spawnEntity(event.getEntity().getLocation(), EntityType.SKELETON);
        animation_entity.getEquipment().setItem(EquipmentSlot.HEAD,event.getEntity().getEquipment().getItem(EquipmentSlot.HEAD));
        animation_entity.getEquipment().setItem(EquipmentSlot.CHEST,event.getEntity().getEquipment().getItem(EquipmentSlot.CHEST));
        animation_entity.getEquipment().setItem(EquipmentSlot.LEGS,event.getEntity().getEquipment().getItem(EquipmentSlot.LEGS));
        animation_entity.getEquipment().setItem(EquipmentSlot.FEET,event.getEntity().getEquipment().getItem(EquipmentSlot.FEET));
        animation_entity.getEquipment().setItem(EquipmentSlot.HAND,event.getEntity().getEquipment().getItem(EquipmentSlot.HAND));
        animation_entity.getEquipment().setItem(EquipmentSlot.OFF_HAND,event.getEntity().getEquipment().getItem(EquipmentSlot.OFF_HAND));
        animation_entity.setSilent(true);
        animation_entity.setHealth(0);

        // Rimozione inventario e status di combattimento alla vittima


        manager.removePlayerInventory(event.getEntity());

        if(manager.timer_stop_fighting.containsKey(event.getEntity())) {

            manager.timer_stop_fighting.remove(event.getEntity());
            manager.bossBar.get(event.getEntity()).setVisible(false);
            manager.bossBar.replace(event.getEntity(), null);
            manager.bossBar.remove(event.getEntity());
            manager.isPlayerLeaving.remove(event.getEntity());
            manager.ability_charging.remove(event.getEntity());

        }

        if(manager.cooldown_ability.containsKey(event.getEntity())) {

            manager.cooldown_ability.remove(event.getEntity());
            event.getEntity().setExp(0);
            event.getEntity().setLevel(0);

        }

        event.getEntity().playSound(event.getEntity().getLocation(), Sound.ENTITY_IRON_GOLEM_DEATH,100F,1.3F);
        if(event.getEntity().getKiller()== null){

            return;

        }

        event.getEntity().getKiller().playSound(event.getEntity().getLocation(), Sound.ENTITY_IRON_GOLEM_DEATH, 100F, 1.3F);
        event.getEntity().getKiller().setHealth(20);


        String death_message = JavaPlugin.getPlugin(Main.class).getConfig().getString("messages.player_kill");
        Player victim = event.getEntity();
        Player killer = event.getEntity().getKiller();
        death_message = death_message.replaceAll("%victim%",victim.getName());
        death_message = death_message.replaceAll("%killer%",killer.getName());
        event.setDeathMessage(ChatColor.translateAlternateColorCodes('&',death_message));


    }


    @EventHandler
    public void EntityDamagedByOtherEntity(EntityDamageByEntityEvent event){


        if(!(event.getEntity() instanceof Player)) return;

        if(event.getCause() == EntityDamageEvent.DamageCause.PROJECTILE && !manager.timer_stop_fighting.containsKey(((Player)event.getEntity()))){


            if(JavaPlugin.getPlugin(Main.class).getConfig().getBoolean("config.suppress-damage")){
                event.setDamage(0);
            }
            else
                event.setCancelled(true);

        }

        if(event.getEntity() instanceof Player && event.getDamager() instanceof Player){


            if(!manager.timer_stop_fighting.containsKey(((Player)event.getEntity())) || !manager.timer_stop_fighting.containsKey((Player)event.getDamager())){

                if(JavaPlugin.getPlugin(Main.class).getConfig().getBoolean("config.suppress-damage")){
                    event.setDamage(0);
                }
                else
                    event.setCancelled(true);
            }


        }

    }

    @EventHandler
    public void EntityDamageEvent(EntityDamageEvent event){

        if(!JavaPlugin.getPlugin(Main.class).getConfig().getBoolean("config.prevent-all-damage-when-pvp-off")) return;

        if(!(event.getEntity() instanceof Player)){
            return;
        }

        if(!manager.timer_stop_fighting.containsKey((Player) event.getEntity())) event.setCancelled(true);

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

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event){

        event.getPlayer().setInvisible(false);

        event.getPlayer().getInventory().setItem(JavaPlugin.getPlugin(Main.class).getConfig().getInt("inventory.weapon.inventory-index"),manager.get_weapon());
        event.getPlayer().setNoDamageTicks(0);
        manager.removePlayerInventory(event.getPlayer());

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


    @EventHandler
    public void onInventoryClick(InventoryClickEvent event){

        if(event.getSlotType() == InventoryType.SlotType.ARMOR){
            event.setCancelled(true);
        }

    }



}
