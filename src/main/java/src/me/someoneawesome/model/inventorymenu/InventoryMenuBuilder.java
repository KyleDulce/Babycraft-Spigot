package src.me.someoneawesome.model.inventorymenu;

import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import src.me.someoneawesome.Babycraft;
import src.me.someoneawesome.exceptions.InvalidInventoryMenuException;
import src.me.someoneawesome.util.BabycraftUtils;

import java.util.HashMap;
import java.util.function.Consumer;

import static src.me.someoneawesome.model.inventorymenu.InventoryMenu.*;

public class InventoryMenuBuilder {

    private int size = 9;
    private String title;
    private Player player;
    private HashMap<Integer, MenuComponent> menuOptions = new HashMap<>();
    private Runnable onCloseEvent;

    InventoryMenuBuilder() {}

    public InventoryMenuBuilder setSize(MenuSize size) {
        this.size = size.size;
        return this;
    }

    public InventoryMenuBuilder setTitle(String title) {
        this.title = title;
        return this;
    }

    public InventoryMenuBuilder setPlayer(Player player) {
        this.player = player;
        return this;
    }

    public InventoryMenuBuilder setOnClose(Runnable runnable) {
        this.onCloseEvent = runnable;
        return this;
    }

    public InventoryMenuBuilder setLabel(int slot, ItemStack itemStack) {
        MenuLabel label = new MenuLabel();
        label.itemStack = itemStack;
        menuOptions.put(slot, label);
        return this;
    }

    public InventoryMenuBuilder setButton(int slot, ItemStack itemStack, Runnable callback) {
        MenuButton button = new MenuButton();
        button.itemStack = itemStack;
        button.callback = callback;
        menuOptions.put(slot, button);
        return this;
    }

    public InventoryMenuBuilder setSlot(int slot, ItemStack currentItem, String label, Consumer<ItemStack> onChange) {
        MenuSlot menuSlot = new MenuSlot();
        menuSlot.callback = onChange;
        menuSlot.label = label;
        menuSlot.currentItem = currentItem;
        menuOptions.put(slot, menuSlot);
        return this;
    }

    public InventoryMenuBuilder setArmorSlot(int slot,
                                             ItemStack currentItem,
                                             EquipmentSlot equipmentSlot,
                                             String label,
                                             Consumer<ItemStack> onChange) {
        ArmorSlot menuSlot = new ArmorSlot();
        menuSlot.callback = onChange;
        menuSlot.label = label;
        menuSlot.currentItem = currentItem;
        menuSlot.expectedSlot = equipmentSlot;
        menuOptions.put(slot, menuSlot);
        return this;
    }

    public InventoryMenuBuilder setField(int slot, ItemStack itemStack, Consumer<String> onChange, String label, String menuTitle, String defaultValue) {
        MenuField menuField = new MenuField();
        menuField.callback = onChange;
        menuField.label = label;
        menuField.value = defaultValue;
        menuField.itemStack = itemStack;
        menuField.menuTitle = menuTitle;
        menuOptions.put(slot, menuField);
        return this;
    }

    public InventoryMenu build() {
        validate();

        InventoryMenu menu = new InventoryMenu();
        menu.menuOptions = menuOptions;
        menu.player = player;
        menu.inventory = Babycraft.instance.getServer().createInventory(null, size, title);
        menu.closeEvent = onCloseEvent;

        return menu;
    }

    private void validate() {
        if(size % 9 != 0 || size > 54) {
            throw new InvalidInventoryMenuException("Unexpected exception, invalid size");
        }

        if(BabycraftUtils.isStringNullOrEmpty(title)) {
            throw new InvalidInventoryMenuException("Bad Menu title");
        }

        if(player == null || !player.isOnline()) {
            throw new InvalidInventoryMenuException("Invalid player for menu");
        }

        for(MenuComponent comp : menuOptions.values()) {
            comp.validate();
        }
    }

    public enum MenuSize {
        ONE(9),
        TWO(18),
        THREE(27),
        FOUR(36),
        FIVE(45),
        SIX(54);
        int size;
        MenuSize(int size) {
            this.size = size;
        }
    }
}
