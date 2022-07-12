package src.me.someoneawesome.commands.admin;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import src.me.someoneawesome.commands.BabycraftCommand;
import src.me.someoneawesome.config.ConfigManager;
import src.me.someoneawesome.model.child.Child;
import src.me.someoneawesome.model.permissions.BcPermission;
import src.me.someoneawesome.model.requirement.RequirementCheck;
import src.me.someoneawesome.model.requirement.RequirementVerifierBuilder;

import java.util.List;

public class CommandResetConfig implements BabycraftCommand {
    @Override
    public void onCommand(CommandSender sender, String[] commandArguments) {
        if(sender instanceof Player){
            Player player = (Player) sender;
            RequirementCheck requirementCheck = RequirementVerifierBuilder.builder()
                    .playerHasPermission(player, BcPermission.ADMIN_SAVE_CONFIG,
                            "You do not have permission to work with the config files")
                    .build().doesMeetRequirements();

            if(!requirementCheck.isSuccess()) {
                sender.sendMessage(ChatColor.RED + requirementCheck.getMessage());
                return;
            }
        }
        if(commandArguments.length != 1 || !commandArguments[0].equalsIgnoreCase("confirm")) {
            sender.sendMessage(ChatColor.RED + "Are you sure you want to RESET the config files? do /bcadmin resetConfig confirm");
            return;
        }

        Child.despawnAll();
        ConfigManager.instance.resetConfigs().block();
        sender.sendMessage(ChatColor.GREEN + "Config Reset and loaded!");
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] commandArguments) {
        return null;
    }

    @Override
    public boolean isCommand(String cmd) {
        return cmd.equalsIgnoreCase(getName());
    }

    @Override
    public String[] getUsage() {
        return new String[] {"bcadmin resetConfig"};
    }

    @Override
    public String getName() {
        return "resetConfig";
    }
}
