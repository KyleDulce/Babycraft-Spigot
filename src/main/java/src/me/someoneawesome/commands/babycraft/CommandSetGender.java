package src.me.someoneawesome.commands.babycraft;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import src.me.someoneawesome.PluginLogger;
import src.me.someoneawesome.commands.BabycraftCommand;
import src.me.someoneawesome.commands.CommandManager;
import src.me.someoneawesome.config.ConfigInterface;
import src.me.someoneawesome.model.Gender;
import src.me.someoneawesome.model.comparator.AlphabeticalComparator;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

public class CommandSetGender implements BabycraftCommand {
    private PluginLogger LOGGER = PluginLogger.getLogger(CommandSetGender.class,
            CommandManager.COMMAND_MANAGER_LOGGER_LABEL);

    @Override
    public void onCommand(CommandSender sender, String[] commandArguments) {
        if(!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Only players can execute this command");
            return;
        }
        Player player = (Player) sender;

        if(commandArguments.length <= 0) {
            sender.sendMessage(ChatColor.RED + "Usage: " + getUsage()[0]);
            return;
        }

        Gender resultGender = Gender.fromString(commandArguments[0]);

        if(resultGender == Gender.NULL) {
            sender.sendMessage(ChatColor.RED + "Usage: " + getUsage()[0]);
            return;
        }

        ConfigInterface.instance.players.setPlayerGender(player.getUniqueId(), resultGender);
        sender.sendMessage(ChatColor.GREEN + "Gender has been set to " + resultGender);
        LOGGER.info(player.getName() + " is setting their gender");
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] commandArguments) {
        if(!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Only players can execute this command");
            return null;
        }

        if(commandArguments.length == 1) {
            List<String> genders = Arrays.asList(
                    Gender.MALE.toString(),
                    Gender.FEMALE.toString(),
                    Gender.OTHER.toString()
            );
            List<String> result = new LinkedList<>();

            for(String g : genders) {
                if(g.toLowerCase().startsWith(commandArguments[0].toLowerCase())) {
                    result.add(g);
                }
            }
            result.sort(new AlphabeticalComparator());
            return result;
        }
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
        return new String[] {"babycraft setGender <male/female/other>"};
    }

    @Override
    public String getName() {
        return "setGender";
    }
}
