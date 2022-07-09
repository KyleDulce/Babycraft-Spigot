package src.me.someoneawesome.event.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import src.me.someoneawesome.model.requests.child.Child;
import src.me.someoneawesome.model.inventorymenu.InventoryMenu;
import src.me.someoneawesome.model.requests.BcRequest;

public class PlayerListener implements Listener {
    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        Child.handlePlayerInteract(event);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Child.handlePlayerQuit(event);
        InventoryMenu.onPlayerQuit(event);
        BcRequest.onPlayerQuit(event);
    }
}
