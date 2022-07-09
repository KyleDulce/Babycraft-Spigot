package src.me.someoneawesome.config.interfaces;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Item;
import org.bukkit.entity.Villager;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import src.me.someoneawesome.config.ConfigManager;
import src.me.someoneawesome.config.ConfigPath;
import src.me.someoneawesome.model.Gender;

import java.util.*;

import static org.bukkit.entity.Villager.Profession;

public class ChildrenConfigInterface {
    private final String label = "children";
    private final ConfigManager manager;

    public ChildrenConfigInterface(ConfigManager manager) {
        this.manager = manager;
    }

    public String getChildName(UUID uuid) {
        return manager.getConfigObject(label).getString(ConfigPath.CHILD_NAME(uuid), null);
    }

    public List<UUID> getChildParents(UUID uuid) {
        List<String> raw = manager.getConfigObject(label).getStringList(ConfigPath.CHILD_PARENTS(uuid));
        List<UUID> result = new ArrayList<>();

        for (String p : raw) {
            result.add(UUID.fromString(p));
        }
        return result;
    }

    public Gender getChildGender(UUID uuid) {
        return Gender.fromString(manager.getConfigObject(label).getString(ConfigPath.CHILD_GENDER(uuid), ""));
    }

    public Location getChildHome(UUID uuid) {
        World w = Bukkit.getWorld(UUID.fromString(manager.getConfigObject(label).getString(ConfigPath.CHILD_HOME_WORLD(uuid),
                Bukkit.getWorlds().get(0).getUID().toString())));
        double x = manager.getConfigObject(label).getDouble(ConfigPath.CHILD_HOME_X(uuid), 0);
        double y = manager.getConfigObject(label).getDouble(ConfigPath.CHILD_HOME_Y(uuid), 0);
        double z = manager.getConfigObject(label).getDouble(ConfigPath.CHILD_HOME_Z(uuid), 0);

        return new Location(w, x, y, z);
    }

    public Profession getChildColor(UUID uuid) {
        return Villager.Profession.valueOf(manager.getConfigObject(label).getString(ConfigPath.CHILD_COLOR(uuid),
                Profession.LIBRARIAN.toString()));
    }

    public ItemStack[] getChildArmor(UUID uuid) {
        ItemStack[] armor = new ItemStack[4];
        armor[0] = manager.getConfigObject(label).getItemStack(ConfigPath.CHILD_ARMOR_HEAD(uuid));
        armor[1] = manager.getConfigObject(label).getItemStack(ConfigPath.CHILD_ARMOR_BODY(uuid));
        armor[2] = manager.getConfigObject(label).getItemStack(ConfigPath.CHILD_ARMOR_LEGS(uuid));
        armor[3] = manager.getConfigObject(label).getItemStack(ConfigPath.CHILD_ARMOR_FEET(uuid));
        return armor;
    }

    public boolean contains(UUID uuid) {
        return manager.getConfigObject(label).contains(ConfigPath.CHILD_ROOT(uuid));
    }

    public void setChildName(UUID uuid, String name) {
        manager.getConfigObject(label).set(ConfigPath.CHILD_NAME(uuid), name.toLowerCase());
    }

    public void setChildParents(UUID uuid, List<UUID> parents) {
        List<String> result = new ArrayList<>();
        for(UUID uid: parents) {
            result.add(uid.toString());
        }
        manager.getConfigObject(label).set(ConfigPath.CHILD_PARENTS(uuid), result);
    }

    public void setChildGender(UUID uuid, Gender gender) {
        manager.getConfigObject(label).set(ConfigPath.PLAYER_GENDER(uuid), gender.toString());
    }

    public void setChildHome(UUID uuid, Location location) {
        manager.getConfigObject(label).set(ConfigPath.CHILD_HOME_WORLD(uuid), location.getWorld().getUID().toString());
        manager.getConfigObject(label).set(ConfigPath.CHILD_HOME_X(uuid), location.getX());
        manager.getConfigObject(label).set(ConfigPath.CHILD_HOME_Y(uuid), location.getY());
        manager.getConfigObject(label).set(ConfigPath.CHILD_HOME_Z(uuid), location.getZ());
    }

    public void setChildColor(UUID uuid, Profession profession) {
        manager.getConfigObject(label).set(ConfigPath.CHILD_COLOR(uuid), profession.toString());
    }

    public void setChildArmor(UUID uuid, ItemStack[] armor) {
        manager.getConfigObject(label).set(ConfigPath.CHILD_ARMOR_HEAD(uuid), armor[0]);
        manager.getConfigObject(label).set(ConfigPath.CHILD_ARMOR_BODY(uuid), armor[1]);
        manager.getConfigObject(label).set(ConfigPath.CHILD_ARMOR_LEGS(uuid), armor[2]);
        manager.getConfigObject(label).set(ConfigPath.CHILD_ARMOR_FEET(uuid), armor[3]);
    }

    public void setChildArmor(UUID uuid, EquipmentSlot slot, ItemStack item) {
        switch (slot) {
            case HEAD ->
                    manager.getConfigObject(label).set(ConfigPath.CHILD_ARMOR_HEAD(uuid), item);
            case CHEST ->
                    manager.getConfigObject(label).set(ConfigPath.CHILD_ARMOR_BODY(uuid), item);
            case LEGS ->
                    manager.getConfigObject(label).set(ConfigPath.CHILD_ARMOR_LEGS(uuid), item);
            case FEET ->
                    manager.getConfigObject(label).set(ConfigPath.CHILD_ARMOR_FEET(uuid), item);
        }
    }

    public void removeChild(UUID uuid) {
        manager.getConfigObject(label).set(ConfigPath.CHILD_ROOT(uuid), null);
    }

    public HashMap<String, UUID> childUIDListToNameUidMap(List<UUID> childUids) {
        HashMap<String, UUID> names = new HashMap<>();
        for(UUID uid : childUids) {
            names.put(getChildName(uid), uid);
        }
        return names;
    }

    public HashMap<UUID, String> childUIDListToUidNameMap(List<UUID> childUids) {
        HashMap<UUID, String> names = new HashMap<>();
        for(UUID uid : childUids) {
            names.put(uid, getChildName(uid));
        }
        return names;
    }
}
