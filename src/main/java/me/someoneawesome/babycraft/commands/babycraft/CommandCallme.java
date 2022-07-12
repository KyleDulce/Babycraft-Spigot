package me.someoneawesome.babycraft.commands.babycraft;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import me.someoneawesome.babycraft.commands.BabycraftCommand;
import me.someoneawesome.babycraft.config.ConfigInterface;
import me.someoneawesome.babycraft.model.comparator.AlphabeticalComparator;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class CommandCallme implements BabycraftCommand {
    @Override
    public void onCommand(CommandSender sender, String[] commandArguments) {
        if(!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Only players can execute this command");
            return;
        }

        if(commandArguments.length <= 0) {
            sender.sendMessage(ChatColor.RED + "Usage: /" + getUsage());
        }

        Player player = (Player) sender;
        ConfigInterface.instance.players.setPlayerCallme(player.getUniqueId(), commandArguments[0]);
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] commandArguments) {
        if(commandArguments.length == 1) {
            List<String> list = new LinkedList<>();

            String[] defaultCallMes = new String[] {
                    ConfigInterface.instance.main.getMaleCallme(),
                    ConfigInterface.instance.main.getFemaleCallme(),
                    ConfigInterface.instance.main.getOtherCallme()
            };

            for(String value : defaultCallMes) {
                if(value.toLowerCase().startsWith(commandArguments[0].toLowerCase())) {
                    list.add(value);
                }
            }

            list.sort(new AlphabeticalComparator());
            return list;
        }
        return Collections.emptyList();
    }

    @Override
    public boolean isCommand(String cmd) {
        return cmd.equalsIgnoreCase(getName());
    }

    @Override
    public String[] getUsage() {
        return new String[] {"babycraft callme <name for child to call you>"};
    }

    @Override
    public String getName() {
        return "CallMe";
    }
}
