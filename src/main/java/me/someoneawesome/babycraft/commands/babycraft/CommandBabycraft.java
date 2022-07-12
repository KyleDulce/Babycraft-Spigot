package me.someoneawesome.babycraft.commands.babycraft;

import me.someoneawesome.babycraft.commands.BabycraftCommand;
import me.someoneawesome.babycraft.commands.BabycraftParentCommand;

import java.util.HashSet;
import java.util.Set;

public class CommandBabycraft extends BabycraftParentCommand {

    private final HashSet<BabycraftCommand> commands;

    public CommandBabycraft() {
        commands = new HashSet<>();
        //true subcommands
        commands.add(new CommandHaveChild());
        commands.add(new CommandAdoptChild());
        commands.add(new CommandSetGender());
        commands.add(new CommandSpawnChild());
        commands.add(new CommandDespawn());
        commands.add(new CommandWarpChild());

        //general
        commands.add(new CommandAccept());
        commands.add(new CommandDeny());
        commands.add(new CommandCancel());
        commands.add(new CommandList());
    }

    @Override
    public boolean isCommand(String cmd) {
        return cmd.equalsIgnoreCase(getName()) ||
                cmd.equalsIgnoreCase("bc") ||
                cmd.equalsIgnoreCase("baby");
    }

    @Override
    public String getName() {
        return "babycraft";
    }

    @Override
    protected Set<BabycraftCommand> getSubCommands() {
        return commands;
    }
}
