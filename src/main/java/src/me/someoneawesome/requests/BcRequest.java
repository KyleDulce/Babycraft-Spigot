package src.me.someoneawesome.requests;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import src.me.someoneawesome.Babycraft;
import src.me.someoneawesome.config.ConfigInterface;
import src.me.someoneawesome.model.FormattedMessage;

import java.util.HashMap;
import java.util.UUID;

public abstract class BcRequest {
    private static HashMap<UUID, BcRequest> activeRequests = new HashMap<>();

    private static void registerRequest(UUID p1, UUID p2, BcRequest req) {
        activeRequests.put(p1, req);
        activeRequests.put(p2, req);
    }

    private static void removeRequest(BcRequest req) {
        activeRequests.remove(req.requester);
        activeRequests.remove(req.receiver);
    }

    private static void removeRequest(UUID p) {
        removeRequest(activeRequests.get(p));
    }

    public static BcRequest getRequest(UUID p) {
        return activeRequests.get(p);
    }

    public static boolean requestContains(UUID p) {
        return activeRequests.containsKey(p);
    }

    protected UUID requester;
    protected UUID receiver;
    private int schedulerTask;

    public BcRequest(UUID requester, UUID receiver) {
        this.requester = requester;
        this.receiver = receiver;
    }

    protected void sendRequest(FormattedMessage.FormattedTextComponent requestText,
                               String acceptCommand,
                               String denyCommand,
                               String cancelCommand) {
        Player playerRequester = Bukkit.getPlayer(requester);
        Player playerReceiver = Bukkit.getPlayer(receiver);

        FormattedMessage requestMessage = FormattedMessage.builder()
                .appendMessage(requestText)
                .appendMessage(
                        FormattedMessage.FormattedTextComponent.builder()
                                .setContent(" [ACCEPT] ")
                                .setColor(ChatColor.GREEN)
                                .setBold()
                                .setClickEvent_runCommand(acceptCommand)
                                .setHoverEvent(
                                        FormattedMessage.buildBasicMessage(
                                                "Click to accept request", ChatColor.GOLD
                                        )
                                ).build())
                .appendMessage(FormattedMessage.FormattedTextComponent.builder()
                        .setContent(" [DENY] ")
                        .setColor(ChatColor.RED)
                        .setBold()
                        .setClickEvent_runCommand(denyCommand)
                        .setHoverEvent(
                                FormattedMessage.buildBasicMessage(
                                        "Click to deny request", ChatColor.GOLD
                                )
                        ).build())
                .build();

        FormattedMessage confirmationMessage = FormattedMessage.builder()
                .appendMessage(
                        FormattedMessage.buildBasicComponent(
                                "Your request was sent to " + playerReceiver.getDisplayName(),
                                ChatColor.GREEN
                        )
                )
                .appendMessage(
                        FormattedMessage.FormattedTextComponent.builder()
                                .setContent(" [CANCEL]")
                                .setColor(ChatColor.GOLD)
                                .setBold()
                                .setClickEvent_runCommand(cancelCommand)
                                .setHoverEvent(
                                        FormattedMessage.buildBasicMessage(
                                        "Click to cancel request", ChatColor.GOLD
                                        )
                                )
                                .build()
                )
                .build();

        schedulerTask = Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(
                Babycraft.instance,
                this::requestTimeout,
                ConfigInterface.instance.main.getRequestTimeout()
        );

        requestMessage.sendMessage(playerReceiver);
        confirmationMessage.sendMessage(playerRequester);
    }

    public boolean accept(UUID user) {
        if(receiver.equals(user)) {
            cancelTask();
            removeRequest(this);
            Player playerRequester = Bukkit.getPlayer(requester);
            Player playerReceiver = Bukkit.getPlayer(receiver);
            FormattedMessage.buildBasicMessage("Your request was accepted", ChatColor.GREEN)
                    .sendMessage(playerRequester);
            FormattedMessage.buildBasicMessage("You accepted the request", ChatColor.GREEN)
                    .sendMessage(playerReceiver);

            commit();
            return true;
        } else {
            return false;
        }
    }

    public boolean deny(UUID user) {
        if(receiver.equals(user)) {
            cancelTask();
            removeRequest(this);
            Player playerRequester = Bukkit.getPlayer(requester);
            Player playerReceiver = Bukkit.getPlayer(receiver);
            FormattedMessage.buildBasicMessage("Your request was denied", ChatColor.RED)
                    .sendMessage(playerRequester);
            FormattedMessage.buildBasicMessage("You denied the request", ChatColor.GREEN)
                    .sendMessage(playerReceiver);
            return true;
        } else {
            return false;
        }
    }

    public boolean cancel(UUID user) {
        if(requester.equals(user)) {
            cancelTask();
            removeRequest(this);
            Player playerRequester = Bukkit.getPlayer(requester);
            Player playerReceiver = Bukkit.getPlayer(receiver);
            FormattedMessage.buildBasicMessage("You cancelled your request", ChatColor.GREEN)
                    .sendMessage(playerRequester);
            FormattedMessage.buildBasicMessage("The request was cancelled", ChatColor.GOLD)
                    .sendMessage(playerReceiver);
            return true;
        } else {
            return false;
        }
    }

    private void requestTimeout() {
        removeRequest(this);
        Player prequester = Bukkit.getPlayer(requester);
        Player preciever = Bukkit.getPlayer(receiver);
        prequester.sendMessage(ChatColor.RED + "your request timed out");
        preciever.sendMessage(ChatColor.GREEN + "the request timed out");
    }

    public abstract void commit();

    private void cancelTask() {
        Bukkit.getScheduler().cancelTask(schedulerTask);
    }
}
