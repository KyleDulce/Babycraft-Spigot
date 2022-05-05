package src.me.someoneawesome.commands.babycraft;

import org.bukkit.command.CommandSender;
import src.me.someoneawesome.commands.BabycraftCommand;

import java.util.List;

public class CommandWarpChild implements BabycraftCommand {
    @Override
    public void onCommand(CommandSender sender, String[] commandArguments) {
        //TODO implement
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] commandArguments) {
        //TODO implement
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
