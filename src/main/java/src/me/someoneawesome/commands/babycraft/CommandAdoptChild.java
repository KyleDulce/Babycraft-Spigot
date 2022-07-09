package src.me.someoneawesome.commands.babycraft;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import src.me.someoneawesome.commands.BabycraftCommand;
import src.me.someoneawesome.model.creation.ChildCreatorMenu;
import src.me.someoneawesome.model.permissions.BcPermission;
import src.me.someoneawesome.model.requirement.RequirementCheck;
import src.me.someoneawesome.model.requirement.RequirementVerifier;
import src.me.someoneawesome.model.requirement.RequirementVerifierBuilder;

import java.util.List;

public class CommandAdoptChild implements BabycraftCommand {
    @Override
    public void onCommand(CommandSender sender, String[] commandArguments) {
        if(!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Only players can execute this command");
            return;
        }

        Player player = (Player) sender;

        RequirementCheck result = RequirementVerifierBuilder.builder()
                .playerExistInConfig(player,
                        "You have not setup your parent details. Do /bc setGender <gender>")
                .playerHasPermission(player, BcPermission.BABYCRAFT_SOLO,
                        "You do not have permission to adopt a child")
                .playerHasPermission(player, BcPermission.BABYCRAFT_CHILDREN,
                        "You do not have permission to have a child")
                .build().doesMeetRequirements();

        if(result.isSuccess()) {
            ChildCreatorMenu.startCreator(player, null);
        } else {
            sender.sendMessage(ChatColor.RED + result.getMessage());
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] commandArguments) {
        return null;
    }

    @Override
    public boolean isCommand(String cmd) {
        return cmd.equalsIgnoreCase(getName()) ||
                cmd.equalsIgnoreCase("adopt") ||
                cmd.equalsIgnoreCase("a");
    }

    @Override
    public String[] getUsage() {
        return new String[] {"babycraft adoptChild"};
    }

    @Override
    public String getName() {
        return "adoptChild";
    }
}
