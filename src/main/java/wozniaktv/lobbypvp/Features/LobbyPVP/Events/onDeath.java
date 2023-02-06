package wozniaktv.lobbypvp.Features.LobbyPVP.Events;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.plugin.java.JavaPlugin;
import wozniaktv.lobbypvp.Features.LobbyPVP.LobbyPVP;
import wozniaktv.lobbypvp.Main;

public class onDeath implements Listener {

    private LobbyPVP manager;
    public onDeath(LobbyPVP manager){
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


}