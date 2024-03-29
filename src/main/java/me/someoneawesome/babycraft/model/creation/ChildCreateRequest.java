package me.someoneawesome.babycraft.model.creation;

import me.someoneawesome.babycraft.model.messaging.FormattedMessage;
import me.someoneawesome.babycraft.model.requests.BcRequest;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.UUID;

public class ChildCreateRequest extends BcRequest {
    public ChildCreateRequest(UUID requester, UUID receiver) {
        super(requester, receiver);
    }

    @Override
    protected FormattedMessage.FormattedTextComponent getRequestText(Player requester) {
        return FormattedMessage.buildBasicComponent(
                requester.getDisplayName() + " wants to have a baby with you",
                ChatColor.AQUA
        );
    }

    @Override
    public void commit() {
        ChildCreatorMenu.startCreator(Bukkit.getPlayer(requester), receiver);
    }

    public static void createAndSendRequest(UUID p1, UUID p2) {
        new ChildCreateRequest(p1, p2).sendRequest();
    }
}
