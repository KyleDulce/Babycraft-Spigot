package src.me.someoneawesome.commands.babycraft;

import org.bukkit.command.CommandSender;
import src.me.someoneawesome.commands.BabycraftCommand;

import java.util.List;

public class CommandSetGender implements BabycraftCommand {
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
                cmd.equalsIgnoreCase("gender") ||
                cmd.equalsIgnoreCase("set") ||
                cmd.equalsIgnoreCase("g");
    }

    @Override
    public String[] getUsage() {
        return new String[] {"babycraft setGender <boy/girl/male/female/other>"};
    }

    @Override
    public String getName() {
        return "setGender";
    }
}
