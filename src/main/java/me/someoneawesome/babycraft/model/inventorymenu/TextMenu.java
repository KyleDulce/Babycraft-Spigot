package me.someoneawesome.babycraft.model.inventorymenu;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import me.someoneawesome.babycraft.PluginLogger;
import me.someoneawesome.babycraft.util.BabycraftUtils;

import java.util.HashMap;
import java.util.UUID;
import java.util.function.Consumer;

public class TextMenu {
    private static final int OUT_SLOT = 2;

    private static HashMap<UUID, TextMenu> openMenus = new HashMap<>();
    private static PluginLogger LOGGER = PluginLogger.getLogger(TextMenu.class);

    public static TextMenu openTextMenu(Player player, String title, String currentText, Consumer<String> onTextSelect, Runnable onCancel) {
        TextMenu textMenu = new TextMenu();
        textMenu.inventory = Bukkit.getServer().createInventory(null, InventoryType.ANVIL, title);
        textMenu.inventory.setItem(0, BabycraftUtils.createItemStack(Material.PAPER, 1, currentText));
        textMenu.onTextSelect = onTextSelect;
        textMenu.onCancel = onCancel;
        textMenu.playerUid = player.getUniqueId();

        openMenus.put(player.getUniqueId(), textMenu);

        return textMenu;
    }

    public static TextMenu openTextMenu(Player player, String title, Consumer<String> onTextSelect, Runnable onCancel) {
        return openTextMenu(player, title, "", onTextSelect, onCancel);
    }

    public static void handleClickEvent(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        if(!openMenus.containsKey(player.getUniqueId())) {
            return;
        }

        TextMenu menu = openMenus.get(player.getUniqueId());
        if(!menu.inventory.equals(event.getInventory())) {
            return;
        }

        event.setCancelled(true);

        int rawSlot = event.getRawSlot();
        if(rawSlot != OUT_SLOT) {
            return;
        }

        ItemStack item = menu.inventory.getItem(OUT_SLOT);
        if(item == null) {
            LOGGER.error("Inventory item is null! Not expected!");
            return;
        }
        String result = item.hasItemMeta() ? item.getItemMeta().getDisplayName() : "";
        menu.onTextSelect.accept(result);
        menu.closeInventory();
    }

    public static void handleCloseEvent(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();
        if(!openMenus.containsKey(player.getUniqueId())) {
            return;
        }

        TextMenu menu = openMenus.get(player.getUniqueId());
        if(!menu.inventory.equals(event.getInventory())) {
            return;
        }

        if(menu.closed) {
            return;
        }

        menu.onCancel.run();
    }

    private TextMenu() {}

    private UUID playerUid;
    private Inventory inventory;
    private Consumer<String> onTextSelect;
    private Runnable onCancel;
    private boolean closed = false;

    public void closeInventory() {
        closed = true;
        Player player = Bukkit.getPlayer(playerUid);
        if(player != null && player.getOpenInventory().equals(inventory)) {
            player.closeInventory();
        }
    }
}
