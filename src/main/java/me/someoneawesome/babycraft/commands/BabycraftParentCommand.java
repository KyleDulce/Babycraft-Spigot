package me.someoneawesome.babycraft.commands;

import me.someoneawesome.babycraft.model.messaging.BasicMessageBuilder;
import org.bukkit.command.CommandSender;
import me.someoneawesome.babycraft.util.BabycraftUtils;

import java.util.*;

public abstract class BabycraftParentCommand implements BabycraftCommand {
    protected abstract Set<BabycraftCommand> getSubCommands();

    @Override
    public void onCommand(CommandSender sender, String[] commandArguments) {
        //attempt to execute command
        for(BabycraftCommand command : getSubCommands()) {
            if(command.isCommand(commandArguments[0])) {
                command.onCommand(sender, BabycraftUtils.removeFirstItemFromArray(commandArguments));
                return;
            }
        }

        String[] usages = getUsage();
        BasicMessageBuilder messageBuilder = BasicMessageBuilder.builder();

        if(usages.length > 1) {
            messageBuilder.addLine("&b~~~~~~~~~~~~~~~~~~~~~~~~~")
                    .addLine("&b&lUsage:");
            for(String usageCommand: usages) {
                messageBuilder.addLine("&b" + usageCommand);
            }
            messageBuilder.addLine("&b~~~~~~~~~~~~~~~~~~~~~~~~~");
        } else {
            messageBuilder.addLine("&b&lUsage:")
                    .addLine(usages[0]);
        }

        sender.sendMessage(messageBuilder.build());
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] commandArguments) {
        Set<BabycraftCommand> subCommands = getSubCommands();
        //check for full completion of current Command
        for(BabycraftCommand command : subCommands) {
            if(command.isCommand(commandArguments[0])) {
                return command.onTabComplete(sender, BabycraftUtils.removeFirstItemFromArray(commandArguments));
            }
        }

        LinkedList<String> result = new LinkedList<>();
        if(commandArguments.length > 0) {
            for(BabycraftCommand command : subCommands) {
                if(command.getName().toLowerCase().startsWith(commandArguments[0].toLowerCase())) {
                    result.add(command.getName());
                }
            }
        } else {
            for(BabycraftCommand command : subCommands) {
                result.add(command.getName());
            }
        }

        Collections.sort(result);
        return result;
    }

    @Override
    public String[] getUsage() {
        LinkedList<String> usages = new LinkedList<>();
        for(BabycraftCommand commands : getSubCommands()) {
            usages.addAll(Arrays.asList(commands.getUsage()));
        }
        return (String[]) usages.toArray();
    }
}
