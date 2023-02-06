package wozniaktv.lobbypvp;

import org.bukkit.plugin.java.JavaPlugin;
import wozniaktv.lobbypvp.Features.LobbyPVP.LobbyPVP;

public class Main extends JavaPlugin {

    private LobbyPVP lobbyPVP_Manager = null;

    @Override
    public void onEnable(){

        getLogger().info("Starting up lobbyPVP manager...");
        lobbyPVP_Manager = new LobbyPVP();
        lobbyPVP_Manager.onEnable();
        getLogger().info("Success, lobbyPVP manager has been started!");

    }

    @Override
    public void onDisable(){

        getLogger().info("Disabling lobbyPVP manager...");
        lobbyPVP_Manager.onDisable();
        getLogger().info("Success, lobbyPVP manager has been disabled!");


    }

}
