package me.someoneawesome.babycraft.event.listeners;

import me.someoneawesome.babycraft.model.inventorymenu.InventoryMenu;
import me.someoneawesome.babycraft.model.inventorymenu.TextMenu;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class InventoryListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        InventoryMenu.onInventoryEvent(event);
        TextMenu.handleClickEvent(event);
    }

    @EventHandler
    public void onInventoryQuit(InventoryCloseEvent event) {
        InventoryMenu.onInventoryEvent(event);
        TextMenu.handleCloseEvent(event);
    }
}
