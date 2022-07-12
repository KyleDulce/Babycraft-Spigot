package src.me.someoneawesome.commands.admin;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import src.me.someoneawesome.PluginLogger;
import src.me.someoneawesome.commands.BabycraftCommand;
import src.me.someoneawesome.commands.CommandManager;
import src.me.someoneawesome.commands.babycraft.CommandWarpChild;
import src.me.someoneawesome.config.ConfigManager;
import src.me.someoneawesome.model.child.Child;
import src.me.someoneawesome.model.permissions.BcPermission;
import src.me.someoneawesome.model.requirement.RequirementCheck;
import src.me.someoneawesome.model.requirement.RequirementVerifierBuilder;

import java.util.List;

public class CommandSaveConfig implements BabycraftCommand {
    private PluginLogger LOGGER = PluginLogger.getLogger(CommandSaveConfig.class,
            CommandManager.COMMAND_MANAGER_LOGGER_LABEL);

    @Override
    public void onCommand(CommandSender sender, String[] commandArguments) {
        if(sender instanceof Player){
            Player player = (Player) sender;
            RequirementCheck requirementCheck = RequirementVerifierBuilder.builder()
                    .playerHasPermission(player, BcPermission.ADMIN_SAVE_CONFIG,
                            "You do not have permission to work with the config")
                    .build().doesMeetRequirements();

            if(!requirementCheck.isSuccess()) {
                sender.sendMessage(ChatColor.RED + requirementCheck.getMessage());
                return;
            }
        }
        ConfigManager.instance.saveConfigs().block();
        sender.sendMessage(ChatColor.GREEN + "Config Saved!");

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
        return new String[] {"bcadmin saveConfig"};
    }

    @Override
    public String getName() {
        return "saveConfig";
    }
}
