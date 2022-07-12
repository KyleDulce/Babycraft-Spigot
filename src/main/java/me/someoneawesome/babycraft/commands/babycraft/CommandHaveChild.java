package me.someoneawesome.babycraft.commands.babycraft;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import me.someoneawesome.babycraft.commands.BabycraftCommand;
import me.someoneawesome.babycraft.config.ConfigInterface;
import me.someoneawesome.babycraft.model.comparator.AlphabeticalComparator;
import me.someoneawesome.babycraft.model.creation.ChildCreateRequest;
import me.someoneawesome.babycraft.model.permissions.BcPermission;
import me.someoneawesome.babycraft.model.requirement.RequirementCheck;
import me.someoneawesome.babycraft.model.requirement.RequirementVerifierBuilder;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class CommandHaveChild implements BabycraftCommand {

    @Override
    public void onCommand(CommandSender sender, String[] commandArguments) {
        if(!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Only players can execute this command");
            return;
        }
        Player player = (Player) sender;

        if(commandArguments.length < 1) {
            sender.sendMessage(ChatColor.RED + "Usage: " + getUsage()[0]);
            return;
        }

        Player other = Bukkit.getServer().getPlayer(commandArguments[0]);
        if(other == null || !other.isOnline()) {
            sender.sendMessage(ChatColor.RED + "Cannot find player " + commandArguments[0]);
            return;
        }

        RequirementCheck requirementCheck = RequirementVerifierBuilder.builder()
                .playerHasPermission(player, BcPermission.BABYCRAFT_CHILDREN,
                        "You do not have permission to have children")
                .playerHasPermission(other, BcPermission.BABYCRAFT_CHILDREN,
                        "Your partner does not have permission to have children")
                .playersWithinRadius(player, other, ConfigInterface.instance.main.haveChildMaxDistance(),
                        "You and your partner is too far apart")
                .playerExistInConfig(player,
                        "You have not setup your parent details. Do /bc setGender <gender>")
                .playerExistInConfig(other,
                        "Your partner have not setup your parent details. They should do /bc setGender <gender>")
                .playerGenderNotNull(player,
                        "You have not setup your parent details. Do /bc setGender <gender>")
                .playerGenderNotNull(other,
                        "Your partner have not setup your parent details. They should do /bc setGender <gender>")
                .haveChildSameGenderCheck(player, other,
                        "You or your partner does not have permission to have a child with someone of the same gender")
                .build().doesMeetRequirements();

        if(requirementCheck.isSuccess()) {
            ChildCreateRequest.createAndSendRequest(player.getUniqueId(), other.getUniqueId());
        } else {
            sender.sendMessage(ChatColor.RED + requirementCheck.getMessage());
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] commandArguments) {
        if(!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Only players can execute this command");
            return Collections.emptyList();
        }
        Player player = (Player) sender;

        if(commandArguments.length == 1) {
            List<String> results = new LinkedList<>();
            Collection<? extends Player> players = Bukkit.getOnlinePlayers();

            for(Player curPlayer : players) {
                if(!curPlayer.getUniqueId().equals(player.getUniqueId())) {
                    String playerName = curPlayer.getName();
                    if(playerName.toLowerCase().startsWith(commandArguments[0].toLowerCase())) {
                        results.add(playerName);
                    }
                }
            }
            results.sort(new AlphabeticalComparator());
            return results;
        }
        return Collections.emptyList();
    }

    @Override
    public boolean isCommand(String cmd) {
        return cmd.equalsIgnoreCase(getName()) ||
                cmd.equalsIgnoreCase("child") ||
                cmd.equalsIgnoreCase("havebaby") ||
                cmd.equalsIgnoreCase("hc") ||
                cmd.equalsIgnoreCase("have");
    }

    @Override
    public String[] getUsage() {
        return new String[] {"babycraft havechild <username>"};
    }

    @Override
    public String getName() {
        return "haveChild";
    }
}
