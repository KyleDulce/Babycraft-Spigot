package src.me.someoneawesome.commands.babycraft;

import org.bukkit.command.CommandSender;
import src.me.someoneawesome.commands.BabycraftCommand;

import java.util.List;

public class CommandSpawnChild implements BabycraftCommand {
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
