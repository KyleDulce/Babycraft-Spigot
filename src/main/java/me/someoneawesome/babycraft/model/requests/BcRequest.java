package me.someoneawesome.babycraft.model.requests;

import me.someoneawesome.babycraft.model.messaging.FormattedMessage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerQuitEvent;
import me.someoneawesome.babycraft.Babycraft;
import me.someoneawesome.babycraft.PluginLogger;
import me.someoneawesome.babycraft.config.ConfigInterface;

import java.util.HashMap;
import java.util.UUID;

public abstract class BcRequest {
    private static final HashMap<UUID, BcRequest> activeRequests = new HashMap<>();
    private static final PluginLogger LOGGER = PluginLogger.getLogger(BcRequest.class);

    private static void registerRequest(UUID p1, UUID p2, BcRequest req) {
        activeRequests.put(p1, req);
        activeRequests.put(p2, req);
    }

    private static void removeRequest(BcRequest req) {
        activeRequests.remove(req.requester);
        activeRequests.remove(req.receiver);
    }

    public static boolean acceptRequest(UUID user) {
        if(activeRequests.containsKey(user)) {
            return activeRequests.get(user).accept(user);
        }
        return false;
    }

    public static boolean playerHasActiveRequest(UUID user) {
        return activeRequests.containsKey(user);
    }

    public static boolean denyRequest(UUID user) {
        if(activeRequests.containsKey(user)) {
            return activeRequests.get(user).deny(user);
        }
        return false;
    }

    public static boolean cancelRequest(UUID user) {
        if(activeRequests.containsKey(user)) {
            return activeRequests.get(user).cancel(user);
        }
        return false;
    }

    public static void onPlayerQuit(PlayerQuitEvent event) {
        UUID user = event.getPlayer().getUniqueId();
        if(activeRequests.containsKey(user)) {
            activeRequests.remove(user);
        }
    }

    protected UUID requester;
    protected UUID receiver;
    private int schedulerTask;

    public BcRequest(UUID requester, UUID receiver) {
        this.requester = requester;
        this.receiver = receiver;
    }

    protected abstract FormattedMessage.FormattedTextComponent getRequestText(Player requester);

    public void sendRequest() {
        Player playerRequester = Bukkit.getPlayer(requester);
        Player playerReceiver = Bukkit.getPlayer(receiver);

        FormattedMessage requestMessage = FormattedMessage.builder()
                .appendMessage(getRequestText(playerRequester))
                .appendMessage(
                        FormattedMessage.FormattedTextComponent.builder()
                                .setContent(" [ACCEPT] ")
                                .setColor(ChatColor.GREEN)
                                .setBold()
                                .setClickEvent_runCommand("babycraft accept")
                                .setHoverEvent(
                                        FormattedMessage.buildBasicMessage(
                                                "Click to accept request", ChatColor.GOLD
                                        )
                                ).build())
                .appendMessage(FormattedMessage.FormattedTextComponent.builder()
                        .setContent(" [DENY] ")
                        .setColor(ChatColor.RED)
                        .setBold()
                        .setClickEvent_runCommand("babycraft deny")
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
                                .setClickEvent_runCommand("babycraft cancel")
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
        registerRequest(requester, receiver, this);
        LOGGER.debug("Registered request between " + requester + " and " + receiver);
    }

    private boolean accept(UUID user) {
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

    private boolean deny(UUID user) {
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

    private boolean cancel(UUID user) {
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
        LOGGER.info("Request between " + requester + " and " + receiver + " timed out");
    }

    public abstract void commit();

    private void cancelTask() {
        Bukkit.getScheduler().cancelTask(schedulerTask);
    }
}
