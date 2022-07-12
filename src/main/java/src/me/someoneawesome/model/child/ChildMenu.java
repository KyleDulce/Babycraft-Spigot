package src.me.someoneawesome.model.child;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import src.me.someoneawesome.PluginLogger;
import src.me.someoneawesome.model.inventorymenu.InventoryMenu;
import src.me.someoneawesome.model.inventorymenu.InventoryMenuBuilder;

import java.util.Optional;
import java.util.Random;

import static src.me.someoneawesome.util.BabycraftUtils.*;
import static org.bukkit.Material.*;
import static org.bukkit.ChatColor.*;

public class ChildMenu {
    private static final PluginLogger LOGGER = PluginLogger.getLogger(ChildMenu.class);
    public static void openChildMenu(Player player, Child child) {
        setMainMenu(player, child);
    }

    private static void setMainMenu(Player player, Child child) {
        InventoryMenu.builder()
                .setSize(InventoryMenuBuilder.MenuSize.ONE)
                .setTitle(AQUA + "Child's Menu")
                .setPlayer(player)
                .setButton(0, createItemStackColored(IRON_CHESTPLATE, "Child's Armor", AQUA,
                        "Change your child's armor"), () ->
                        setArmorMenu(player, child))
                .setButton(1, createItemStackColored(OAK_SIGN,  "Interact", AQUA,
                        "Interact with your child"), () ->
                        setInteractMenu(player, child))
                .setButton(2, createItemStackColored(OAK_DOOR, "Go Home", AQUA,
                        "Send Your child home"), () -> {
                    child.getChildEntity().teleportHome();
                    closeMenu(player);
                })
                .setButton(3, createItemStackColored(Material.COMPASS, "Movement", AQUA,
                        "The movement settings for your child"), () ->
                        setMovementMenu(player, child))
                .setButton(6, createItemStackColored(RED_BED, "Set Home", AQUA,
                        "Set Your child's home"), () -> {
                    child.getChildEntity().setHome(player.getLocation());
                    closeMenu(player);
                })
                .setButton(7, createItemStackColored(REDSTONE, "Settings", AQUA,
                        "Change the settings for your child"), () ->
                        setSettingsMenu(player, child))
                .setButton(8, createItemStackColored(BARRIER, "Close", RED,
                        "Leave the menu"), () ->
                        closeMenu(player))
                .build()
                .openMenu();
    }

    private static void setArmorMenu(Player player, Child child) {
        ItemStack[] childArmor = child.getChildEntity().getArmor();
        InventoryMenu.builder()
                .setSize(InventoryMenuBuilder.MenuSize.FOUR)
                .setTitle(AQUA + "Child's Armor")
                .setPlayer(player)
                .setArmorSlot(4, childArmor[0], EquipmentSlot.HEAD, "Helmet Slot", stack ->
                        child.getChildEntity().setArmor(stack, EquipmentSlot.HEAD)
                )
                .setArmorSlot(13, childArmor[1], EquipmentSlot.CHEST, "Chestplate Slot", stack ->
                        child.getChildEntity().setArmor(stack, EquipmentSlot.CHEST)
                )
                .setArmorSlot(22, childArmor[2], EquipmentSlot.LEGS, "Leggings Slot", stack ->
                        child.getChildEntity().setArmor(stack, EquipmentSlot.LEGS)
                )
                .setArmorSlot(31, childArmor[3], EquipmentSlot.FEET, "Boots Slot", stack ->
                        child.getChildEntity().setArmor(stack, EquipmentSlot.FEET)
                )
                .setButton(35, createItemStackColored(RED_WOOL, "Back", RED,
                        "Go back to the main menu"), () ->
                        setMainMenu(player, child))
                .build()
                .openMenu();
    }

    private static void setInteractMenu(Player player, Child child) {
        InventoryMenu.builder()
                .setSize(InventoryMenuBuilder.MenuSize.ONE)
                .setTitle(AQUA + "Interact")
                .setPlayer(player)
                .setButton(0, createItemStackColored(BOOK, "Chat", AQUA,
                        "Chat with your child"), () -> {
                    int random = new Random().nextInt(25);
                    if(random == 24) {
                        ReactionCreator.sendMessage(ActionMessage.EASTER_EGG, child, player);
                    } else {
                        ReactionCreator.sendMessage(ActionMessage.CHAT, child, player);
                    }
                    closeMenu(player);
                })
                .setButton(1, createItemStackColored(TROPICAL_FISH, "Joke", AQUA,
                        "Tell a joke to your child"), () -> {
                    ReactionCreator.sendMessage(ActionMessage.JOKE, child, player);
                    closeMenu(player);
                        })
                .setButton(2, createItemStackColored(TOTEM_OF_UNDYING, "Hug", AQUA,
                        "Hug your child"), () -> {
                    ReactionCreator.sendMessage(ActionMessage.HUG, child, player);
                    closeMenu(player);
                })
                .setButton(3, createItemStackColored(POPPY, "Kiss", AQUA,
                        "Kiss your child"), () -> {
                    ReactionCreator.sendMessage(ActionMessage.KISS, child, player);
                    closeMenu(player);
                })
                .setButton(8, createItemStackColored(RED_WOOL, "Back", AQUA,
                        "Go back to the main menu"), () -> {
                    setMainMenu(player, child);
                })
                .build()
                .openMenu();
    }

    private static void setSettingsMenu(Player player, Child child) {
        InventoryMenu.builder()
                .setSize(InventoryMenuBuilder.MenuSize.ONE)
                .setTitle(AQUA + "Settings")
                .setPlayer(player)
                .setButton(0, createItemStackColored(DIAMOND_SWORD, "Despawn Child", RED,
                        "Despawn your child"), () -> {
                    child.getChildEntity().despawn();
                    closeMenu(player);
                })
                .setButton(1, createItemStackColored(LEATHER_CHESTPLATE, "Clothes Color", AQUA,
                        "Change the color of your child's clothes"), () -> {
                    child.getChildEntity().setNextClothing();
                    closeMenu(player);
                })
                .setButton(4, createItemStackColored(RED_STAINED_GLASS_PANE, "REMOVE FROM EXISTANCE", RED,
                        "REMOVE CHILD FROM EXISTANCE, CANNOT BE UNDONE"), () -> {
                    setVerifyMenu(player, child);
                })
                .setButton(8, createItemStackColored(RED_WOOL, "Back", AQUA,
                        "Go back to the main menu"), () -> {
                    setMainMenu(player,child);
                })
                .build()
                .openMenu();

    }

    private static void setVerifyMenu(Player player, Child child) {
        InventoryMenu.builder()
                .setSize(InventoryMenuBuilder.MenuSize.ONE)
                .setTitle(RED + "Are you sure")
                .setPlayer(player)
                .setButton(2, createItemStackColored(RED_WOOL, "Yes", RED,
                        "THIS ACTION CANNOT BE UNDONE"), () -> {
                    child.getChildEntity().despawn();
                    Child.removeChild(child.getUuid());
                    closeMenu(player);
                    LOGGER.info(player.getDisplayName() + " just deleted child " + child.getUuid().toString());
                })
                .setLabel(4, createItemStackColored(COMPASS, "Are you sure you want to remove this child?", AQUA))
                .setButton(6, createItemStackColored(GREEN_WOOL, "No", GREEN), () ->
                    setSettingsMenu(player, child))
                .build()
                .openMenu();
    }

    private static void setMovementMenu(Player player, Child child) {
        InventoryMenu.builder()
                .setSize(InventoryMenuBuilder.MenuSize.ONE)
                .setTitle(AQUA + "Child's Movement Settings")
                .setPlayer(player)
                .setButton(0, createItemStackColored(COMPASS, "Follow me", AQUA,
                        "Have your child follow you"), () -> {
                    child.getChildEntity().follow(player);
                    closeMenu(player);
                })
                .setButton(1, createItemStackColored(BEDROCK, "Stay here", AQUA,
                        "Have your child stay here"), () -> {
                    child.getChildEntity().stay(player);
                    closeMenu(player);
                })
                .setButton(2, createItemStackColored(FEATHER, "Roam Around", AQUA,
                        "Have your child roam around"), () -> {
                    child.getChildEntity().roam(player);
                    closeMenu(player);
                })
                .setButton(8, createItemStackColored(RED_WOOL, "Back", AQUA,
                        "Go back to the main menu"), () ->
                        setMainMenu(player, child))
                .build()
                .openMenu();
    }

    private static void closeMenu(Player player) {
        Optional<InventoryMenu> menuOptional = InventoryMenu.getMenuForPlayer(player);
        menuOptional.ifPresent(InventoryMenu::closeMenu);
    }
}
