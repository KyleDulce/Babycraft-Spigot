package src.me.someoneawesome.commands.babycraft;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import src.me.someoneawesome.PluginLogger;
import src.me.someoneawesome.commands.BabycraftCommand;
import src.me.someoneawesome.commands.CommandManager;
import src.me.someoneawesome.config.ConfigInterface;
import src.me.someoneawesome.model.child.Child;
import src.me.someoneawesome.model.comparator.AlphabeticalComparator;

import java.util.*;

public class CommandWarpChild implements BabycraftCommand {
    private PluginLogger LOGGER = PluginLogger.getLogger(CommandWarpChild.class,
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

        if(childObjOptional.isEmpty()) {
            sender.sendMessage(ChatColor.RED + "This child is not spawned");
            return;
        }

        Child child = childObjOptional.get();
        child.getChildEntity().teleportHome();
        sender.sendMessage(ChatColor.GREEN + "Teleported " + childName + " home");
        LOGGER.info("Teleported child uuid" + childUUID + " home");
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

            for(Map.Entry<String, UUID> name : childrenNamesToUID.entrySet()) {
                if(Child.getChildFromUID(name.getValue()).isPresent()) {
                    if(name.getKey().startsWith(commandArguments[0].toLowerCase())) {
                        result.add(name.getKey());
                    }
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
                cmd.equalsIgnoreCase("warp") ||
                cmd.equalsIgnoreCase("home") ||
                cmd.equalsIgnoreCase("goHome") ||
                cmd.equalsIgnoreCase("warpHome") ||
                cmd.equalsIgnoreCase("h") ||
                cmd.equalsIgnoreCase("w");
    }

    @Override
    public String[] getUsage() {
        return new String[] {"babycraft warpChild <childname/all>"};
    }

    @Override
    public String getName() {
        return "warpChild";
    }
}
