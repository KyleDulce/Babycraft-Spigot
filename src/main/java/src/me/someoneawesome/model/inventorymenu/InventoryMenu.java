package src.me.someoneawesome.model.inventorymenu;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import src.me.someoneawesome.exceptions.InvalidInventoryMenuException;

import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

import static src.me.someoneawesome.util.BabycraftUtils.*;

public class InventoryMenu {

    private static final HashMap<UUID, InventoryMenu> openMenus = new HashMap<>();

    public static void onInventoryEvent(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        if(!openMenus.containsKey(player.getUniqueId())) {
            return;
        }

        InventoryMenu menu = openMenus.get(player.getUniqueId());
        if(!event.getInventory().equals(menu.inventory)) {
            return;
        }

        menu.onEvent(event);
    }

    public static void onInventoryEvent(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();
        if(!openMenus.containsKey(player.getUniqueId())) {
            return;
        }

        InventoryMenu menu = openMenus.get(player.getUniqueId());
        if(!event.getInventory().equals(menu.inventory)) {
            return;
        }

        menu.onCloseEvent();
    }

    public static void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if(!openMenus.containsKey(player.getUniqueId())) {
            return;
        }

        openMenus.remove(player.getUniqueId());
    }

    public static Optional<InventoryMenu> getMenuForPlayer(Player player) {
        if(!openMenus.containsKey(player.getUniqueId())) {
            return Optional.empty();
        }
        return Optional.of(openMenus.get(player.getUniqueId()));
    }

    public static InventoryMenuBuilder builder() {
        return new InventoryMenuBuilder();
    }

    HashMap<Integer, MenuComponent> menuOptions;
    Inventory inventory;
    Player player;
    Runnable closeEvent;
    private boolean refreshing = false;

    InventoryMenu() {}

    public void refresh() {
        refreshing = true;
        player.closeInventory();
        inventory.clear();
        setupInventoryObject();
        player.openInventory(inventory);
        refreshing = false;
    }

    public void openMenu() {
        openMenus.put(player.getUniqueId(), this);
        refresh();
    }

    public void closeMenu() {
        player.closeInventory();
        openMenus.remove(player.getUniqueId());
    }

    private void onEvent(InventoryClickEvent event) {
        int slot = event.getSlot();
        if(menuOptions.containsKey(slot)) {
            menuOptions.get(slot).onComponentSelect(event, this);
        }
    }

    private void onCloseEvent() {
        if(!refreshing && closeEvent != null) {
            closeEvent.run();
        }
    }

    private void setupInventoryObject() {
        for(Integer slot : menuOptions.keySet()) {
            inventory.setItem(slot, menuOptions.get(slot).getItemStackDisplay());
        }
    }

    static ItemStack getNullSlotItemStack(String label) {
        return createItemStack(Material.BARRIER, 1, label, "Put item into slot!");
    }

    interface MenuComponent {
        void validate();
        ItemStack getItemStackDisplay();
        void onComponentSelect(InventoryClickEvent event, InventoryMenu instance);
    }

    static class MenuLabel implements MenuComponent {
        protected ItemStack itemStack = null;
        @Override
        public void validate() {
            if(itemStack == null) {
                throw new InvalidInventoryMenuException("Menu Label has invalid itemstack");
            }
        }
        @Override
        public ItemStack getItemStackDisplay() {
            return itemStack;
        }
        @Override
        public void onComponentSelect(InventoryClickEvent event, InventoryMenu instance) {
            event.setCancelled(true);
        }
    }

    static class MenuButton implements MenuComponent {
        protected Runnable callback = null;
        protected ItemStack itemStack = null;
        @Override
        public void validate() {
            if(itemStack == null) {
                throw new InvalidInventoryMenuException("Menu button has invalid itemstack");
            }
            if(callback == null) {
                throw new InvalidInventoryMenuException("Menu button has invalid callback runnable");
            }
        }
        @Override
        public ItemStack getItemStackDisplay() {
            return itemStack;
        }
        @Override
        public void onComponentSelect(InventoryClickEvent event, InventoryMenu instance) {
            event.setCancelled(true);
            callback.run();
        }
    }

    static class MenuSlot implements MenuComponent {
        protected Consumer<ItemStack> callback = null;
        protected ItemStack currentItem = null;
        protected String label = null;
        @Override
        public void validate() {
            if(callback == null) {
                throw new InvalidInventoryMenuException("Menu slot has invalid callback runnable");
            }
        }
        @Override
        public ItemStack getItemStackDisplay() {
            if(currentItem != null) {
                return currentItem;
            } else {
                return getNullSlotItemStack(label);
            }
        }
        @Override
        public void onComponentSelect(InventoryClickEvent event, InventoryMenu instance) {
            Player player = (Player) event.getWhoClicked();
            ItemStack itemOnCursor = player.getItemOnCursor();
            Inventory inventory = event.getInventory();
            int slot = event.getSlot();

            //nothing in slot + nothing on cursor
            if(currentItem == null && itemOnCursor.getType().equals(Material.AIR)) {
                //cancel
                event.setCancelled(true);
            //nothing in slot + something on cursor
            } else if(currentItem == null && !itemOnCursor.getType().equals(Material.AIR)) {
                //put item in slot
                inventory.setItem(slot, null);
                currentItem = itemOnCursor;
                callback.accept(currentItem);
            //something on slot + something on cursor
            } else if(currentItem != null && !itemOnCursor.getType().equals(Material.AIR)) {
                //swap
                currentItem = itemOnCursor;
                callback.accept(currentItem);
            //something on slot + nothing on cursor
            } else {
                //grab and put null
                player.setItemOnCursor(getNullSlotItemStack(label));
                currentItem = null;
                callback.accept(null);
            }
        }
    }

    static class ArmorSlot extends MenuSlot {
        protected EquipmentSlot expectedSlot;
        @Override
        public void validate() {
            super.validate();
            if(expectedSlot != EquipmentSlot.CHEST &&
                    expectedSlot != EquipmentSlot.FEET &&
                    expectedSlot != EquipmentSlot.HEAD &&
                    expectedSlot != EquipmentSlot.LEGS) {
                throw new InvalidInventoryMenuException("Menu slot has invalid Equipment slot value runnable");
            }
        }
        @Override
        public void onComponentSelect(InventoryClickEvent event, InventoryMenu instance) {
            ItemStack itemOnCursor = event.getWhoClicked().getItemOnCursor();
            Material itemMaterial = itemOnCursor.getType();

            //head treated like normal slot
            //air cursor will be ignored
            if(expectedSlot != EquipmentSlot.HEAD && !itemMaterial.equals(Material.AIR)) {
                if(     (expectedSlot == EquipmentSlot.CHEST && !materialIsChestplate(itemMaterial)) ||
                        (expectedSlot == EquipmentSlot.LEGS && !materialIsLeggings(itemMaterial)) ||
                        (expectedSlot == EquipmentSlot.FEET && !materialIsBoots(itemMaterial))) {
                    //invalid armor
                    event.setCancelled(true);
                    return;
                }
            }
            super.onComponentSelect(event, instance);
        }
    }

    static class MenuField implements MenuComponent {
        protected Consumer<String> callback = null;
        protected ItemStack itemStack = null;
        protected String value = null;
        protected String menuTitle = null;
        protected TextMenu menu = null;
        @Override
        public void validate() {
            if(itemStack == null) {
                throw new InvalidInventoryMenuException("Menu field has invalid itemstack");
            }
            if(callback == null) {
                throw new InvalidInventoryMenuException("Menu field has invalid callback runnable");
            }
        }
        @Override
        public ItemStack getItemStackDisplay() {
            return itemStack;
        }
        @Override
        public void onComponentSelect(InventoryClickEvent event, InventoryMenu instance) {
            //open text field select
            menu = TextMenu.openTextMenu(instance.player, menuTitle, value, outValue -> {
                value = outValue;
                instance.refresh();
            }, instance::refresh);
        }
    }
}
