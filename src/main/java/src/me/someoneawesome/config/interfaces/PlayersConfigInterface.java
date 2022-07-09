package src.me.someoneawesome.config.interfaces;

import org.bukkit.entity.Player;
import src.me.someoneawesome.config.ConfigInterface;
import src.me.someoneawesome.config.ConfigManager;
import src.me.someoneawesome.config.ConfigPath;
import src.me.someoneawesome.model.Gender;

import java.util.ArrayList;
import java.util.HashMap;
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

    public String getPlayerCallme(UUID uuid) {
        String callme = manager.getConfigObject(label).getString(ConfigPath.PLAYER_CALLME(uuid), null);

        if(callme != null) {
            return callme;
        }

        Gender gender = getPlayerGender(uuid);

        switch (gender){
            case MALE -> {
                return ConfigInterface.instance.main.getMaleCallme();
            }
            case FEMALE -> {
                return ConfigInterface.instance.main.getFemaleCallme();
            }
            case OTHER -> {
                return ConfigInterface.instance.main.getOtherCallme();
            }
            default -> {
                return ConfigInterface.instance.main.getNoneCallme();
            }
        }
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

    public void setPlayerGender(UUID uuid, Gender gender) {
        manager.getConfigObject(label).set(ConfigPath.PLAYER_GENDER(uuid), gender.toString());
    }

    public void setPlayerCallme(UUID uuid, String callme) {
        manager.getConfigObject(label).set(ConfigPath.PLAYER_CALLME(uuid), callme);
    }

    public void setPlayerPartners(UUID uuid, List<UUID> partners) {
        List<String> result = new ArrayList<>();
        for(UUID uid : partners) {
            result.add(uid.toString());
        }

        manager.getConfigObject(label).set(ConfigPath.PLAYER_PARTNER(uuid), result);
    }

    public void addPlayerPartners(UUID uuid, UUID partner) {
        List<UUID> partners = getPlayerPartners(uuid);
        if(!partners.contains(partner)) {
            partners.add(partner);
            setPlayerPartners(uuid, partners);
        }
    }

    public void removePlayerPartner(UUID uuid, UUID partner) {
        List<UUID> partners = getPlayerPartners(uuid);
        if(partners.contains(partner)) {
            partners.remove(partner);
            setPlayerPartners(uuid, partners);
        }
    }

    public void setPregnantStatus(UUID uuid, boolean status) {
        manager.getConfigObject(label).set(ConfigPath.PLAYER_PREGNANT_STATUS(uuid), status);
    }

    public void getPregnantTime(UUID uuid, double time) {
        manager.getConfigObject(label).set(ConfigPath.PLAYER_PREGNANT_TIMELEFT(uuid), time);
    }

    public void setPregnantPartner(UUID uuid, UUID partner) {
        manager.getConfigObject(label).set(ConfigPath.PLAYER_PREGNANT_PARTNER(uuid), partner.toString());
    }

    public void setPlayerChildren(UUID uuid, List<UUID> children) {
        List<String> result = new ArrayList<>();
        for(UUID uid : children) {
            result.add(uid.toString());
        }

        manager.getConfigObject(label).set(ConfigPath.PLAYER_CHILDREN(uuid), result);
    }

    public void addPlayerChild(UUID uuid, UUID child) {
        List<UUID> children = getPlayerChildren(uuid);
        if(!children.contains(child)) {
            children.add(child);
            setPlayerPartners(uuid, children);
        }
    }

    public void removePlayerChildren(UUID uuid, UUID child) {
        List<UUID> children = getPlayerChildren(uuid);
        if(children.contains(child)) {
            children.remove(child);
            setPlayerPartners(uuid, children);
        }
    }

    public boolean doesPlayerHaveChildWithName(UUID uuid, String name) {
        name = name.toLowerCase();
        List<UUID> childrenList = getPlayerChildren(uuid);
        for(UUID childUid : childrenList) {
            if(ConfigInterface.instance.children.getChildName(childUid).equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
    }
}
