package src.me.someoneawesome.commands.admin;

import org.bukkit.command.CommandSender;
import src.me.someoneawesome.commands.BabycraftCommand;

import java.util.List;

public class CommandResetConfig implements BabycraftCommand {
    @Override
    public void onCommand(CommandSender sender, String[] commandArguments) {
        //TODO implement
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
        return new String[] {"bcadmin resetConfig"};
    }

    @Override
    public String getName() {
        return "resetConfig";
    }
}
