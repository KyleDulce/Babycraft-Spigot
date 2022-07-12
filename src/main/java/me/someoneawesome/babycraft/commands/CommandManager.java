package me.someoneawesome.babycraft.commands;

import me.someoneawesome.babycraft.PluginLogger;
import me.someoneawesome.babycraft.commands.admin.CommandAdmin;
import me.someoneawesome.babycraft.commands.babycraft.CommandBabycraft;
import me.someoneawesome.babycraft.config.ConfigManager;
import me.someoneawesome.babycraft.model.messaging.BasicMessageBuilder;
import me.someoneawesome.babycraft.util.BabycraftUtils;
import org.apache.maven.plugin.logging.Log;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import java.util.*;

public class CommandManager implements TabExecutor {

    private final Set<BabycraftCommand> subCommands;
    public static final String COMMAND_MANAGER_LOGGER_LABEL = "Command Manager";
    private static final PluginLogger LOGGER = PluginLogger.getLogger(CommandManager.class, COMMAND_MANAGER_LOGGER_LABEL);

    public CommandManager() {
        subCommands = new HashSet<>();
        subCommands.add(new CommandBabycraft());
        subCommands.add(new CommandAdmin());
    }

    //Tab Executor methods
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] commandArguments) {
        for(BabycraftCommand bcCommand : subCommands) {
            if(bcCommand.isCommand(command.getName())) {
                bcCommand.onCommand(commandSender, commandArguments);
                return true;
            }
        }
        LOGGER.warn("Command manager failed to find command. You should not be able to see this.");
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String label, String[] commandArguments) {
        for(BabycraftCommand bcCommand : subCommands) {
            if(bcCommand.isCommand(command.getName())) {
                return bcCommand.onTabComplete(commandSender, commandArguments);
            }
        }
        LOGGER.warn("Command manager failed to find command on tab complete. You should not be able to see this.");
        return Collections.emptyList();
    }
}
