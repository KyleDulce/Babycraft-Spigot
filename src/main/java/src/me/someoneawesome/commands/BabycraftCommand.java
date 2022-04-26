package src.me.someoneawesome.commands;

import org.bukkit.command.CommandSender;

import java.util.List;

public interface BabycraftCommand {
    public void onCommand(CommandSender sender, String[] commandArguments);
    public List<String> onTabComplete(CommandSender sender, String[] commandArguments);
    public boolean isCommand(String cmd);
    public String[] getUsage();
    public String getName();
}
