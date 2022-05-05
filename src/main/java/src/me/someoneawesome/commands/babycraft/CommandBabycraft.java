package src.me.someoneawesome.commands.babycraft;

import src.me.someoneawesome.commands.BabycraftCommand;
import src.me.someoneawesome.commands.BabycraftParentCommand;

import java.util.HashSet;
import java.util.Set;

public class CommandBabycraft extends BabycraftParentCommand {

    private final HashSet<BabycraftCommand> commands;

    private CommandBabycraft() {
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
