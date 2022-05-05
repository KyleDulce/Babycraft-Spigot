package src.me.someoneawesome.config.interfaces;

import org.bukkit.entity.Player;
import src.me.someoneawesome.config.ConfigManager;
import src.me.someoneawesome.config.ConfigPath;
import src.me.someoneawesome.model.Gender;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PlayersConfigInterface {
    private final String label = "players";
    private final ConfigManager manager;

    public PlayersConfigInterface(ConfigManager manager) {
        this.manager = manager;
    }

    public Gender getPlayerGender(UUID uuid) {
        return Gender.fromString(manager.getConfigObject(label).getString(ConfigPath.PLAYER_GENDER(uuid), ""));
    }

    public List<UUID> getPlayerPartners(UUID uuid) {
        List<String> raw = manager.getConfigObject(label).getStringList(ConfigPath.PLAYER_PARTNER(uuid));
        List<UUID> result = new ArrayList<>();

        for (String p : raw) {
            result.add(UUID.fromString(p));
        }
        return result;
    }

    public boolean getPregnantStatus(UUID uuid) {
        return manager.getConfigObject(label).getBoolean(ConfigPath.PLAYER_PREGNANT_STATUS(uuid), false);
    }

    public double getPregnantTime(UUID uuid) {
        return manager.getConfigObject(label).getDouble(ConfigPath.PLAYER_PREGNANT_TIMELEFT(uuid), 0);
    }

    public UUID getPregnantPartner(UUID uuid) {
        String in = manager.getConfigObject(label).getString(ConfigPath.PLAYER_PREGNANT_PARTNER(uuid), null);
        return (in != null)? UUID.fromString(in) : null;
    }

    public List<UUID> getPlayerChildren(UUID uuid) {
        List<String> raw = manager.getConfigObject(label).getStringList(ConfigPath.PLAYER_CHILDREN(uuid));
        List<UUID> result = new ArrayList<>();

        for (String p : raw) {
            result.add(UUID.fromString(p));
        }
        return result;
    }

    public boolean contains(UUID uuid) {
        return manager.getConfigObject(label).contains(ConfigPath.PLAYER_ROOT(uuid));
    }
}
