package src.me.someoneawesome.config.interfaces;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Villager;
import src.me.someoneawesome.config.ConfigManager;
import src.me.someoneawesome.config.ConfigPath;
import src.me.someoneawesome.model.Gender;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
}
