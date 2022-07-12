package me.someoneawesome.babycraft.commands.babycraft;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import me.someoneawesome.babycraft.PluginLogger;
import me.someoneawesome.babycraft.commands.BabycraftCommand;
import me.someoneawesome.babycraft.commands.CommandManager;
import me.someoneawesome.babycraft.config.ConfigInterface;
import me.someoneawesome.babycraft.model.child.Child;
import me.someoneawesome.babycraft.model.comparator.AlphabeticalComparator;

import java.util.*;

public class CommandSpawnChild implements BabycraftCommand {
    private PluginLogger LOGGER = PluginLogger.getLogger(CommandSpawnChild.class,
            CommandManager.COMMAND_MANAGER_LOGGER_LABEL);

    @Override
    public void onCommand(CommandSender sender, String[] commandArguments) {
        if(!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Only players can execute this command");
            return;
        }
        Player player = (Player) sender;

        if(commandArguments.length < 1) {
            sender.sendMessage(ChatColor.RED + "Usage: " + getUsage()[0]);
            return;
        }

        ConfigInterface config = ConfigInterface.instance;

        List<UUID> children = config.players.getPlayerChildren(player.getUniqueId());
        if(children.size() <= 0) {
            sender.sendMessage(ChatColor.RED + "You have no children");
            return;
        }
        String childName = commandArguments[0].toLowerCase();
        HashMap<String, UUID> childrenNamesToUID = config.children.childUIDListToNameUidMap(children);
        if(!childrenNamesToUID.containsKey(childName)) {
            sender.sendMessage(ChatColor.RED + "You do not have a child named " + commandArguments[0]);
            return;
        }

        UUID childUUID = childrenNamesToUID.get(childName);
        Optional<Child> childObjOptional = Child.getChildFromUID(childUUID);

        if(childObjOptional.isPresent()) {
            sender.sendMessage(ChatColor.RED + "This child is already spawned");
            return;
        }

        Child child = Child.spawnChildFromConfig(childUUID, player.getLocation());
        sender.sendMessage(ChatColor.GREEN + "Spawned " + childName);
        LOGGER.info("Spawned child uuid" + childUUID + " with mob id" + child.getChildEntity().getMobID());
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] commandArguments) {
        if(!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Only players can execute this command");
            return null;
        }
        Player player = (Player) sender;

        if(commandArguments.length == 1) {
            ConfigInterface config = ConfigInterface.instance;

            List<UUID> children = config.players.getPlayerChildren(player.getUniqueId());
            if(children.size() <= 0) {
                return null;
            }

            List<String> result = new LinkedList<>();
            HashMap<String, UUID> childrenNamesToUID = config.children.childUIDListToNameUidMap(children);

            for(String name : childrenNamesToUID.keySet()) {
                if(name.startsWith(commandArguments[0].toLowerCase())) {
                    result.add(name);
                }
            }

            result.sort(new AlphabeticalComparator());
            return result;
        }
        return null;
    }

    @Override
    public boolean isCommand(String cmd) {
        return cmd.equalsIgnoreCase(getName()) ||
                cmd.equalsIgnoreCase("spawn") ||
                cmd.equalsIgnoreCase("s");
    }

    @Override
    public String[] getUsage() {
        return new String[] {"babycraft spawnchild <child name>"};
    }

    @Override
    public String getName() {
        return "spawnChild";
    }
}
