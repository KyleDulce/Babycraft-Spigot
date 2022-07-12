package src.me.someoneawesome.commands.babycraft;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import src.me.someoneawesome.commands.BabycraftCommand;
import src.me.someoneawesome.config.ConfigInterface;
import src.me.someoneawesome.config.PluginConfig;
import src.me.someoneawesome.model.comparator.AlphabeticalComparator;
import src.me.someoneawesome.model.child.Child;

import java.util.*;

public class CommandDespawn implements BabycraftCommand {
    @Override
    public void onCommand(CommandSender sender, String[] commandArguments) {
        if(!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Only players can execute this command");
            return;
        }
        Player player = (Player) sender;

        if(commandArguments.length >= 1) {
            sender.sendMessage(ChatColor.RED + "You must add at least 1 argument");
            return;
        }

        List<UUID> childrenList = ConfigInterface.instance.players.getPlayerChildren(player.getUniqueId());
        if(childrenList.size() <= 0) {
            sender.sendMessage(ChatColor.RED + "You have no children to despawn");
            return;
        }

        if(listContainsDespawnAllKeyword(commandArguments)) {
            for(UUID childUId: childrenList) {
                Optional<Child> childOptional = Child.getChildFromUID(childUId);
                if(childOptional.isPresent()) {
                    childOptional.get().getChildEntity().despawn();
                }
            }
            sender.sendMessage(ChatColor.GREEN + "All children despawned");
        } else {
            HashMap<String, UUID> names = ConfigInterface.instance.children.childUIDListToNameUidMap(childrenList);
            for(String curName: commandArguments) {
                if(!names.containsKey(curName)) {
                    sender.sendMessage(ChatColor.RED + "You do not have a child named that");
                    continue;
                }
                Optional<Child> childOptional = Child.getChildFromUID(names.get(curName));
                if(childOptional.isEmpty()) {
                    sender.sendMessage(ChatColor.RED + curName + " is not spawned");
                    continue;
                }
                sender.sendMessage(ChatColor.GREEN + curName + " was despawned");
            }
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] commandArguments) {
        if(!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Only players can execute this command");
            return null;
        }
        Player player = (Player) sender;

        if(commandArguments.length >= 1) {
            List<String> results = new LinkedList<>();
            results.add(PluginConfig.instance.getDespawnAllKeyword());

            List<UUID> childrenList = ConfigInterface.instance.players.getPlayerChildren(player.getUniqueId());
            HashMap<UUID, String> names = ConfigInterface.instance.children.childUIDListToUidNameMap(childrenList);

            for(UUID childUid : childrenList) {
                if(Child.getChildFromUID(childUid).isPresent()) {
                    String name = names.get(childUid);
                    if(name.toLowerCase().startsWith(commandArguments[commandArguments.length - 1].toLowerCase())) {
                        results.add(name);
                    }
                }
            }
            results.sort(new AlphabeticalComparator());
            return results;
        }
        return null;
    }

    @Override
    public boolean isCommand(String cmd) {
        return cmd.equalsIgnoreCase(getName())||
                cmd.equalsIgnoreCase("d");
    }

    @Override
    public String[] getUsage() {
        return new String[] {"babycraft despawn " + PluginConfig.instance.getDespawnAllKeyword(),
                            "babycraft despawn <child name>"};
    }

    @Override
    public String getName() {
        return "despawn";
    }

    private static boolean listContainsDespawnAllKeyword(String[] list) {
        String keyword = PluginConfig.instance.getDespawnAllKeyword();
        for(String item : list) {
            if(item.equalsIgnoreCase(keyword)) {
                return true;
            }
        }
        return false;
    }
}
