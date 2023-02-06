package wozniaktv.lobbypvp;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import wozniaktv.lobbypvp.Commands.Lpvpreload;
import wozniaktv.lobbypvp.Events.*;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public final class LobbyPVP extends JavaPlugin {

    public HashMap<Player, Double> timer_stop_fighting;
    public HashMap<Player, BossBar> bossBar;
    public HashMap<Player, Boolean> isPlayerLeaving;

    public HashMap<Player, Integer> cooldown_ability;

    public HashMap<Player,Integer> ability_charging;

    @Override
    public void onEnable() {   //SkillExecutor

        timer_stop_fighting = new HashMap<>();

        bossBar = new HashMap<>();

        isPlayerLeaving = new HashMap<>();

        cooldown_ability = new HashMap<>();

        ability_charging = new HashMap<>();

        saveDefaultConfig();

        reloadConfig();

        saveConfig();

        start_timer();



        getCommand("lpvpreload").setExecutor(new Lpvpreload());



        getServer().getPluginManager().registerEvents(new onHotbarItemChange(),this);

        getServer().getPluginManager().registerEvents(new onHit(), this);

        getServer().getPluginManager().registerEvents(new onDeath(), this);

        getServer().getPluginManager().registerEvents(new onJoin(),this);

        getServer().getPluginManager().registerEvents(new onRespawn(), this);

        getServer().getPluginManager().registerEvents(new onRightClick(), this);

        getServer().getPluginManager().registerEvents(new onSneakToggle(), this);


        for(Player p : getServer().getOnlinePlayers()){


            if(p.getInventory().getHeldItemSlot() == getConfig().getInt("inventory.weapon.inventory-index")){
                addPlayerFighting(p);
                p.getInventory().setItem(JavaPlugin.getPlugin(LobbyPVP.class).getConfig().getInt("inventory.weapon.inventory-index"),JavaPlugin.getPlugin(LobbyPVP.class).get_weapon());
            }

        }


    }

    @Override
    public void onDisable() {

        for(Player p : getServer().getOnlinePlayers()){

            removePlayerInventory(p);
            timer_stop_fighting.remove(p);
            bossBar.get(p).setVisible(false);
            bossBar.replace(p,null);
            bossBar.remove(p);
            isPlayerLeaving.remove(p);

            cooldown_ability.remove(p);
            p.setExp(0);
            p.setLevel(0);

        }

    }


    public ItemStack get_weapon(){

        ItemStack weapon = new ItemStack(Objects.requireNonNull(Material.getMaterial(Objects.requireNonNull(JavaPlugin.getPlugin(LobbyPVP.class).getConfig().getString("inventory.weapon.type")))));

        ItemMeta weapon_meta = weapon.getItemMeta();

        weapon_meta.setDisplayName(ChatColor.translateAlternateColorCodes('&',JavaPlugin.getPlugin(LobbyPVP.class).getConfig().getString("inventory.weapon.display-name")));

        weapon_meta.setUnbreakable(true);

        weapon_meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        weapon_meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        weapon_meta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL,1,true);

        List<String> lore_weapon = getConfig().getStringList("inventory.weapon.lore");

        if(lore_weapon!=null){


            for(int i=0;i<lore_weapon.size();i++){

                lore_weapon.set(i,ChatColor.translateAlternateColorCodes('&',lore_weapon.get(i)));

            }

            weapon_meta.setLore(lore_weapon);
        }

        weapon_meta.addEnchant(Enchantment.DAMAGE_ALL,4,true);

        weapon.setItemMeta(weapon_meta);

        return weapon;

    }

    public void start_timer(){

        getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {



            @Override
            public void run(){


                for(Player p : Bukkit.getOnlinePlayers()){

                    if(ability_charging.containsKey(p)){

                        int needed_charge = getConfig().getInt("config.ability-needed-charge");


                        String msg = getConfig().getString("config.ability-message-charge");

                        if(ability_charging.get(p)<needed_charge) {

                            ability_charging.replace(p, ability_charging.get(p) + 1);
                            p.playSound(p,Sound.BLOCK_AMETHYST_BLOCK_STEP,100f,1f);

                        }

                        msg = msg.replaceAll("%charge%",ability_charging.get(p).toString());
                        msg = msg.replaceAll("%needed_seconds%", String.valueOf(needed_charge));

                        msg = ChatColor.translateAlternateColorCodes('&',msg);



                        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.translateAlternateColorCodes('&',msg)));




                    }

                    if(cooldown_ability.containsKey(p)){

                        cooldown_ability.replace(p, cooldown_ability.get(p)-1);

                        float l = cooldown_ability.get(p);

                        p.setLevel(cooldown_ability.get(p));


                        if(cooldown_ability.get(p) <= 0){


                            cooldown_ability.remove(p);
                            p.setExp(0.9999F);
                            p.setLevel(0);
                            p.playSound(p.getLocation(),Sound.BLOCK_STONE_BUTTON_CLICK_ON,100F,2F);


                        }else{
                            p.playSound(p.getLocation(),Sound.BLOCK_STONE_BUTTON_CLICK_ON,100F,1F);
                        }

                    }


                    if(timer_stop_fighting.containsKey(p) || bossBar.containsKey(p)) {

                        if(!isPlayerLeaving.get(p)) continue;

                        timer_stop_fighting.replace(p,timer_stop_fighting.get(p)-1);

                        bossBar.get(p).setProgress(timer_stop_fighting.get(p)/6);

                        if(timer_stop_fighting.get(p) <= 0){

                            timer_stop_fighting.remove(p);
                            bossBar.get(p).setVisible(false);
                            bossBar.replace(p,null);
                            bossBar.remove(p);
                            removePlayerInventory(p);

                            isPlayerLeaving.remove(p);
                            ability_charging.remove(p);
                            p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(""));

                            p.playSound(p,Sound.BLOCK_NOTE_BLOCK_BANJO,100,2);
                            p.setHealth(20);
                        }
                        else{
                            p.playSound(p,Sound.BLOCK_NOTE_BLOCK_BASS,100,1);
                        }

                    }


                }


            }


        } , 0L, 20L);


    }

    public BossBar bossBar_create(){


        BossBar bb;

        bb = Bukkit.createBossBar("-", BarColor.RED, BarStyle.SEGMENTED_6);

        bb.setTitle(ChatColor.translateAlternateColorCodes('&',getConfig().getString("bossbar.display.name")));
        bb.setVisible(true);
        bb.setProgress(1.0);

        return bb;

    }

    public void setPlayerInventory(Player p){

        ItemStack helmet = new ItemStack(Objects.requireNonNull(Material.getMaterial(Objects.requireNonNull(getConfig().getString("inventory.helmet.type")))),1);

        ItemMeta helmet_itemMeta = helmet.getItemMeta();

        helmet_itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(getConfig().getString("inventory.helmet.display-name"))));
        List<String> lore_helmet = getConfig().getStringList("inventory.helmet.lore");

        if(lore_helmet!=null){


            for(int i=0;i<lore_helmet.size();i++){

                lore_helmet.set(i,ChatColor.translateAlternateColorCodes('&',lore_helmet.get(i)));

            }

            helmet_itemMeta.setLore(lore_helmet);
        }

        helmet_itemMeta.setUnbreakable(true);
        helmet_itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        helmet_itemMeta.addEnchant(Enchantment.DAMAGE_ALL,1,true);
        helmet.setItemMeta(helmet_itemMeta);
        p.getInventory().setItem(EquipmentSlot.HEAD,helmet);





        ItemStack chestplate = new ItemStack(Objects.requireNonNull(Material.getMaterial(Objects.requireNonNull(getConfig().getString("inventory.chestplate.type")))),1);

        ItemMeta chestplate_itemMeta = chestplate.getItemMeta();

        chestplate_itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(getConfig().getString("inventory.chestplate.display-name"))));

        List<String> lore_chestplate = getConfig().getStringList("inventory.chestplate.lore");

        if(lore_chestplate!=null){


            for(int i=0; i< lore_chestplate.size(); i++){

                lore_chestplate.set(i,ChatColor.translateAlternateColorCodes('&',lore_chestplate.get(i)));

            }

            chestplate_itemMeta.setLore(lore_chestplate);
        }

        chestplate_itemMeta.setUnbreakable(true);
        chestplate_itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        chestplate_itemMeta.addEnchant(Enchantment.DAMAGE_ALL,1,true);

        chestplate.setItemMeta(chestplate_itemMeta);


        p.getInventory().setItem(EquipmentSlot.CHEST,chestplate);


        ItemStack leggings = new ItemStack(Objects.requireNonNull(Material.getMaterial(Objects.requireNonNull(getConfig().getString("inventory.leggings.type")))),1);

        ItemMeta leggings_itemMeta = leggings.getItemMeta();

        leggings_itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(getConfig().getString("inventory.leggings.display-name"))));
        List<String> lore_leggings = getConfig().getStringList("inventory.leggings.lore");

        if(lore_leggings!=null){


            for(int i=0; i< lore_leggings.size(); i++){

                lore_leggings.set(i,ChatColor.translateAlternateColorCodes('&',lore_leggings.get(i)));

            }

            leggings_itemMeta.setLore(lore_leggings);
        }

        leggings_itemMeta.setUnbreakable(true);

        leggings_itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        leggings_itemMeta.addEnchant(Enchantment.DAMAGE_ALL,1,true);

        leggings.setItemMeta(leggings_itemMeta);


        p.getInventory().setItem(EquipmentSlot.LEGS,leggings);

        ItemStack boots = new ItemStack(Objects.requireNonNull(Material.getMaterial(Objects.requireNonNull(getConfig().getString("inventory.boots.type")))),1);

        ItemMeta boots_itemMeta = boots.getItemMeta();

        boots_itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(getConfig().getString("inventory.boots.display-name"))));
        List<String> lore_boots = getConfig().getStringList("inventory.boots.lore");

        if(lore_boots!=null){


            for(int i=0; i< lore_boots.size(); i++){

                lore_boots.set(i,ChatColor.translateAlternateColorCodes('&',lore_boots.get(i)));

            }

            boots_itemMeta.setLore(lore_boots);
        }

        boots_itemMeta.setUnbreakable(true);

        boots_itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        boots_itemMeta.addEnchant(Enchantment.DAMAGE_ALL,1,true);

        boots.setItemMeta(boots_itemMeta);


        p.getInventory().setItem(EquipmentSlot.FEET,boots);


    }

    public void removePlayerInventory(Player p){

        p.getInventory().setItem(EquipmentSlot.HEAD,new ItemStack(Material.AIR));
        p.getInventory().setItem(EquipmentSlot.CHEST,new ItemStack(Material.AIR));
        p.getInventory().setItem(EquipmentSlot.LEGS,new ItemStack(Material.AIR));
        p.getInventory().setItem(EquipmentSlot.FEET,new ItemStack(Material.AIR));

    }

    public double getTimeToStopFighting(Player p){

        if(!timer_stop_fighting.containsKey(p)) return -1;

        else return timer_stop_fighting.get(p);

    }

    public void addPlayerFighting(Player p){

        if(timer_stop_fighting.containsKey(p)) timer_stop_fighting.remove(p);
        timer_stop_fighting.put(p,6.0);
        if(!bossBar.containsKey(p)) {

            bossBar.put(p,bossBar_create());
            bossBar.get(p).addPlayer(p);
            p.setExp(0.9999F);
            p.setLevel(0);

        }else{
            bossBar.get(p).setProgress(1.0);
        }

        isPlayerLeaving.put(p,false);
        setPlayerInventory(p);

    }

    public void setPlayerLeaving(Player p){

        isPlayerLeaving.replace(p,true);

    }




}
