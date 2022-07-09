package src.me.someoneawesome.model.creation;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import src.me.someoneawesome.model.messaging.FormattedMessage;
import src.me.someoneawesome.model.requests.BcRequest;

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
}
