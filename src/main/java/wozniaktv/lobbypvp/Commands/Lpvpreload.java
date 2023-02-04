package wozniaktv.lobbypvp.Commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import wozniaktv.lobbypvp.LobbyPVP;

public class Lpvpreload implements CommandExecutor {



    @Override
    public boolean onCommand(CommandSender sender,Command command,String label, String[] args) {

        JavaPlugin.getPlugin(LobbyPVP.class).reloadConfig();
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&',"&2Reload successful."));

        for(Player p : JavaPlugin.getPlugin(LobbyPVP.class).getServer().getOnlinePlayers()){

            p.getInventory().setItem(JavaPlugin.getPlugin(LobbyPVP.class).getConfig().getInt("inventory.weapon.inventory-index"),JavaPlugin.getPlugin(LobbyPVP.class).get_weapon());
            if(JavaPlugin.getPlugin(LobbyPVP.class).timer_stop_fighting.containsKey(p)){

                JavaPlugin.getPlugin(LobbyPVP.class).setPlayerInventory(p);

            }

        }

        return true;
    }




}
