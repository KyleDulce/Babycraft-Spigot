package me.someoneawesome.babycraft.commands.admin;

import me.someoneawesome.babycraft.commands.BabycraftCommand;
import me.someoneawesome.babycraft.commands.BabycraftParentCommand;

import java.util.HashSet;
import java.util.Set;

public class CommandAdmin extends BabycraftParentCommand {

    private final HashSet<BabycraftCommand> commands;

    public CommandAdmin() {
        commands = new HashSet<>();

        commands.add(new CommandReload());
        commands.add(new CommandResetConfig());
        commands.add(new CommandSaveConfig());
        commands.add(new CommandDespawnAll());
    }

    @Override
    public boolean isCommand(String cmd) {
        return cmd.equalsIgnoreCase(getName());
    }

    @Override
    public String getName() {
        return "bcadmin";
    }

    @Override
    protected Set<BabycraftCommand> getSubCommands() {
        return commands;
    }
}
