package me.someoneawesome.babycraft.commands.babycraft;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import me.someoneawesome.babycraft.commands.BabycraftCommand;
import me.someoneawesome.babycraft.config.ConfigInterface;
import me.someoneawesome.babycraft.model.Gender;
import me.someoneawesome.babycraft.model.messaging.BasicMessageBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class CommandList implements BabycraftCommand {
    @Override
    public void onCommand(CommandSender sender, String[] commandArguments) {
        if(!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Only players can execute this command");
            return;
        }
        Player player = (Player) sender;

        ConfigInterface config = ConfigInterface.instance;
        if(!config.players.contains(player.getUniqueId()) ||
                config.players.getPlayerChildren(player.getUniqueId()).size() <= 0) {
            sender.sendMessage(ChatColor.RED + "You do not have children");
            return;
        }

        List<UUID> list = config.players.getPlayerChildren(player.getUniqueId());
        HashMap<UUID, String> nameMap = config.children.childUIDListToUidNameMap(list);

        BasicMessageBuilder messageBuilder = BasicMessageBuilder.builder();

        for(Map.Entry<UUID, String> child : nameMap.entrySet()) {
            Gender childGender = config.children.getChildGender(child.getKey());
            if(childGender.equals(Gender.MALE)) {
                messageBuilder.addLine(child.getValue(), ChatColor.AQUA);
            } else if(childGender.equals(Gender.FEMALE)) {
                messageBuilder.addLine(child.getValue(), ChatColor.LIGHT_PURPLE);
            } else {
                messageBuilder.addLine(child.getValue(), ChatColor.YELLOW);
            }
        }
        sender.sendMessage(messageBuilder.build());
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] commandArguments) {
        return null;
    }

    @Override
    public boolean isCommand(String cmd) {
        return cmd.equalsIgnoreCase(getName());
    }

    @Override
    public String[] getUsage() {
        return new String[] {"babycraft list"};
    }

    @Override
    public String getName() {
        return "list";
    }
}
