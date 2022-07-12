package me.someoneawesome.babycraft.commands.admin;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import me.someoneawesome.babycraft.PluginLogger;
import me.someoneawesome.babycraft.commands.BabycraftCommand;
import me.someoneawesome.babycraft.commands.CommandManager;
import me.someoneawesome.babycraft.model.child.Child;
import me.someoneawesome.babycraft.model.permissions.BcPermission;
import me.someoneawesome.babycraft.model.requirement.RequirementCheck;
import me.someoneawesome.babycraft.model.requirement.RequirementVerifierBuilder;

import java.util.Collections;
import java.util.List;

public class CommandDespawnAll implements BabycraftCommand {
    private PluginLogger LOGGER = PluginLogger.getLogger(CommandDespawnAll.class,
            CommandManager.COMMAND_MANAGER_LOGGER_LABEL);

    @Override
    public void onCommand(CommandSender sender, String[] commandArguments) {
        if(sender instanceof Player){
            Player player = (Player) sender;
            RequirementCheck requirementCheck = RequirementVerifierBuilder.builder()
                    .playerHasPermission(player, BcPermission.ADMIN_DESPAWN_ALL,
                            "You do not have permission to despawn all babies")
                    .build().doesMeetRequirements();

            if(!requirementCheck.isSuccess()) {
                sender.sendMessage(ChatColor.RED + requirementCheck.getMessage());
                return;
            }
        }
        LOGGER.info("Despawning all children");
        Child.despawnAll();
        sender.sendMessage(ChatColor.GREEN + "All despawned!");
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] commandArguments) {
        return Collections.emptyList();
    }

    @Override
    public boolean isCommand(String cmd) {
        return cmd.equalsIgnoreCase(getName());
    }

    @Override
    public String[] getUsage() {
        return new String[] {"bcadmin despawnAll"};
    }

    @Override
    public String getName() {
        return "despawnAll";
    }
}
