package me.someoneawesome.babycraft.model.creation;

import me.someoneawesome.babycraft.model.inventorymenu.InventoryMenu;
import me.someoneawesome.babycraft.model.inventorymenu.InventoryMenuBuilder;
import me.someoneawesome.babycraft.model.messaging.FormattedMessage;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import me.someoneawesome.babycraft.PluginLogger;
import me.someoneawesome.babycraft.config.ConfigInterface;
import me.someoneawesome.babycraft.config.PluginConfig;
import me.someoneawesome.babycraft.model.Gender;
import me.someoneawesome.babycraft.model.child.Child;

import java.util.*;
import java.util.regex.Pattern;

import static org.bukkit.ChatColor.*;
import static me.someoneawesome.babycraft.util.BabycraftUtils.*;
import static org.bukkit.Material.*;

public class ChildCreatorMenu {
    private static final PluginLogger LOGGER = PluginLogger.getLogger(ChildCreatorMenu.class);
    private static final Pattern alphaPattern = Pattern.compile("[^a-zA-Z0-9_]]");

    private Gender gender;
    private String childName;

    private UUID parent1;
    private UUID parent2;

    private Location defaultLocation;

    private ChildCreatorMenu() {}

    private void openMenu() {
        Player player = Bukkit.getPlayer(parent1);
        InventoryMenu.builder()
                .setSize(InventoryMenuBuilder.MenuSize.ONE)
                .setTitle(GREEN + "Have your Baby")
                .setPlayer(player)
                .setButton(0, getGenderStack(), () -> {
                    toggleGender();
                    refreshMenu();
                })
                .setField(1, createItemStackColored(NAME_TAG, childName, GOLD, "Name, Click to change name"),
                        newName -> {
                            if(alphaPattern.matcher(newName).find() || newName.equalsIgnoreCase(PluginConfig.instance.getDespawnAllKeyword())) {
                                player.sendMessage(RED + "Names can only contain alphanumeric characters and cannot be 'all'");
                            } else if (ConfigInterface.instance.players.doesPlayerHaveChildWithName(parent1, newName) ||
                                    ConfigInterface.instance.players.doesPlayerHaveChildWithName(parent2, newName)) {
                                player.sendMessage(RED + "You or your partner already has a child named " + newName);
                            } else {
                                childName = newName.toLowerCase();
                            }
                            refreshMenu();
                        }, GOLD + "Set your child's name!", childName)
                .setButton(8, createItemStackColored(EMERALD_BLOCK, "Done", GREEN), () -> {
                    closeMenu();
                    completeCreator();
                })
                .setOnClose(() -> {
                    closeMenu();
                    cancelCreator();
                })
                .build()
                .openMenu();
    }

    private void refreshMenu() {
        closeMenu();
        openMenu();
    }

    private void closeMenu() {
        Player player = Bukkit.getPlayer(parent1);
        if(player != null) {
            Optional<InventoryMenu> menuOptional = InventoryMenu.getMenuForPlayer(player);
            menuOptional.ifPresent(InventoryMenu::closeMenu);
        }
    }

    private ItemStack getGenderStack() {
        switch (gender) {
            case MALE -> {
                return createItemStackColored(LIGHT_BLUE_DYE, "It's a boy!", AQUA, "Gender, Click to change");
            }
            case FEMALE -> {
                return createItemStackColored(PINK_DYE, "It's a girl!", LIGHT_PURPLE, "Gender, Click to change");
            }
            case OTHER -> {
                return createItemStackColored(YELLOW_DYE, "It's a baby!", YELLOW, "Gender, Click to change");
            }
        }
        return null;
    }

    private void toggleGender() {
        switch (gender) {
            case MALE ->
                    gender = Gender.FEMALE;
            case FEMALE ->
                    gender = Gender.OTHER;
            case OTHER ->
                    gender = Gender.MALE;
        }
    }

    private void completeCreator() {
        List<UUID> parents = new ArrayList<>();
        parents.add(parent1);
        if(parent2 != null) {
            parents.add(parent2);
        }

        Child.newChildToConfig(childName, parents, gender, defaultLocation);
        FormattedMessage message = getCreatedChildMessage();

        message.sendMessage(Bukkit.getPlayer(parent1));
        if(parent2 != null) {
            message.sendMessage(Bukkit.getPlayer(parent2));
            getBroadcastMessageDual().broadcastMessage();
        } else {
            getBroadcastMessageSingle().broadcastMessage();
        }
    }

    private void cancelCreator() {
        Player player1 = Bukkit.getPlayer(parent1);
        if(player1 != null) {
            player1.sendMessage(GREEN + "You cancelled making your baby");
        }

        if(parent2 == null) {
            return;
        }
        Player player2 = Bukkit.getPlayer(parent2);
        if(player2 != null) {
            player2.sendMessage(GREEN + "Your partner (" +
                    (player1 != null? player1.getDisplayName() : "Logged off") +
                    ") cancelled making your baby");
        }
    }

    private FormattedMessage getCreatedChildMessage() {
        return FormattedMessage.builder()
                .appendMessage(FormattedMessage.FormattedTextComponent.builder()
                        .setContent("Your baby was created, do ")
                        .setColor(GREEN)
                        .setBold()
                        .build()
                ).appendMessage(FormattedMessage.FormattedTextComponent.builder()
                        .setContent("/babycraft spawnchild " + childName)
                        .setColor(AQUA)
                        .setItalics()
                        .setHoverEvent(FormattedMessage.buildBasicMessage("Click to copy into chat", AQUA))
                        .setClickEvent_suggestCommand("babycraft spawnchild " + childName)
                        .build()
                ).build();
    }

    private FormattedMessage getBroadcastMessageDual() {
        Player player1 = Bukkit.getPlayer(parent1);
        Player player2 = Bukkit.getPlayer(parent2);
        return FormattedMessage.builder()
                .appendMessage(FormattedMessage.FormattedTextComponent.builder()
                        .setContent(player1.getDisplayName() + " and " + player2.getDisplayName() + " just had a" +
                                "baby together!")
                        .setColor(GREEN)
                        .setBold()
                        .build()
                ).build();
    }

    private FormattedMessage getBroadcastMessageSingle() {
        Player player1 = Bukkit.getPlayer(parent1);
        return FormattedMessage.builder()
                .appendMessage(FormattedMessage.FormattedTextComponent.builder()
                        .setContent(player1.getDisplayName() + " just adopted a baby!")
                        .setColor(GREEN)
                        .setBold()
                        .build()
                ).build();
    }

    public static void startCreator(Player initiator, UUID otherParent) {
        LOGGER.info(initiator.getDisplayName() + " is making a child");
        ChildCreatorMenu menu = new ChildCreatorMenu();
        menu.parent1 = initiator.getUniqueId();
        menu.parent2 = otherParent;
        menu.gender = getRandomElementFromArray(Gender.MALE, Gender.FEMALE);

        String originalName;
        if(menu.gender == Gender.MALE) {
            originalName = getRandomElementFromArray(PluginConfig.instance.getBoyNames()).toLowerCase();
        } else {
            originalName = getRandomElementFromArray(PluginConfig.instance.getGirlNames()).toLowerCase();
        }
        menu.childName = originalName;
        int num = 0;
        while(ConfigInterface.instance.players.doesPlayerHaveChildWithName(menu.parent1, menu.childName) ||
                ConfigInterface.instance.players.doesPlayerHaveChildWithName(menu.parent2, menu.childName)) {
            menu.childName = originalName + "_" + (++num);
        }

        menu.defaultLocation = initiator.getLocation();
        menu.openMenu();
    }
}
