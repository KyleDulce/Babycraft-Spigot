package src.me.someoneawesome.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CommandManager extends BabycraftParentCommand implements TabExecutor {

    private final Set<BabycraftCommand> subCommands;

    public CommandManager() {
        subCommands = new HashSet<>();
    }

    //Tab Executor methods
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] commandArguments) {
        onCommand(commandSender, commandArguments);
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String label, String[] commandArguments) {
        return onTabComplete(commandSender, commandArguments);
    }

    //Parent Command methods
    @Override
    public boolean isCommand(String cmd) {
        return false;
    }

    @Override
    public String getName() {
        return "";
    }

    @Override
    protected Set<BabycraftCommand> getSubCommands() {
        return subCommands;
    }
}
