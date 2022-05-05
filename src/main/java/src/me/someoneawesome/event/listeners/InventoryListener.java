package src.me.someoneawesome.event.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import src.me.someoneawesome.model.inventorymenu.InventoryMenu;
import src.me.someoneawesome.model.inventorymenu.TextMenu;

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
