package me.someoneawesome.babycraft.commands.babycraft;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import me.someoneawesome.babycraft.commands.BabycraftCommand;
import me.someoneawesome.babycraft.model.requests.BcRequest;

import java.util.Collections;
import java.util.List;

public class CommandAccept implements BabycraftCommand {
    @Override
    public void onCommand(CommandSender sender, String[] commandArguments) {
        if(!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Only players can execute this command");
            return;
        }

        Player player = (Player) sender;
        boolean result = BcRequest.acceptRequest(player.getUniqueId());

        if(!result) {
            sender.sendMessage(ChatColor.RED + "You have no active requests you can accept");
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] commandArguments) {
        return Collections.emptyList();
    }

    @Override
    public boolean isCommand(String cmd) {
        return cmd.equalsIgnoreCase(getName());
    }

    @Override
    public String[] getUsage() {
        return new String[] {"babycraft accept"};
    }

    @Override
    public String getName() {
        return "accept";
    }
}
