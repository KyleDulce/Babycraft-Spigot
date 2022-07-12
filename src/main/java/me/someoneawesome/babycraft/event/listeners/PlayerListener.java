package me.someoneawesome.babycraft.event.listeners;

import me.someoneawesome.babycraft.model.child.Child;
import me.someoneawesome.babycraft.model.inventorymenu.InventoryMenu;
import me.someoneawesome.babycraft.model.requests.BcRequest;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerQuitEvent;

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
