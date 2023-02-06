package wozniaktv.lobbypvp.Features.LobbyPVP.Commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import wozniaktv.lobbypvp.Features.LobbyPVP.LobbyPVP;
import wozniaktv.lobbypvp.Main;

public class Lpvpreload implements CommandExecutor {


    private LobbyPVP manager;

    public Lpvpreload(LobbyPVP manager){

        this.manager = manager;

    }


    @Override
    public boolean onCommand(CommandSender sender,Command command,String label, String[] args) {

        JavaPlugin.getPlugin(Main.class).reloadConfig();
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&',"&2Reload successful."));

        for(Player p : JavaPlugin.getPlugin(Main.class).getServer().getOnlinePlayers()){

            p.getInventory().setItem(JavaPlugin.getPlugin(Main.class).getConfig().getInt("inventory.weapon.inventory-index"),manager.get_weapon());
            if(manager.timer_stop_fighting.containsKey(p)){

                manager.setPlayerInventory(p);

            }

        }

        return true;
    }




}
