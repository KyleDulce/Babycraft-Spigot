package src.me.someoneawesome.commands.babycraft;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import src.me.someoneawesome.commands.BabycraftCommand;
import src.me.someoneawesome.model.requests.BcRequest;

import java.util.List;

public class CommandCancel implements BabycraftCommand {
    @Override
    public void onCommand(CommandSender sender, String[] commandArguments) {
        if(!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Only players can execute this command");
            return;
        }

        Player player = (Player) sender;
        boolean result = BcRequest.cancelRequest(player.getUniqueId());

        if(!result) {
            sender.sendMessage(ChatColor.RED + "You have no active requests you can cancel");
        }
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
        return new String[] {"babycraft cancel"};
    }

    @Override
    public String getName() {
        return "cancel";
    }
}
