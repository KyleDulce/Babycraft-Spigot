package src.me.someoneawesome.util;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import src.me.someoneawesome.Babycraft;
import src.me.someoneawesome.config.PluginConfig;

import java.io.InputStream;
import java.util.*;

public class BabycraftUtils {

    public static Optional<InputStream> getResourceAsOptional(String filename) {
        InputStream stream = Babycraft.instance.getResource(filename);
        if(stream != null) {
            return Optional.of(stream);
        } else {
            return Optional.empty();
        }
    }

    public static boolean isStringNullOrEmpty(String str) {
        return str == null || str.isEmpty() || str.isBlank();
    }

    public static <T> T[] removeFirstItemFromArray(T[] array) {
        if(array == null || array.length == 0) {
            return null;
        }
        LinkedList<T> list = new LinkedList<>(Arrays.asList(array));
        list.removeFirst();
        T[] resultArray = (T[]) java.lang.reflect.Array.newInstance(list.get(0)
                .getClass(), list.size());
        return list.toArray(resultArray);
    }

    public static ItemStack createItemStack(Material material, int amount, String name, String lore) {
        ItemStack itemStack = new ItemStack(material, amount);
        ItemMeta itemMeta = itemStack.getItemMeta();

        if(name != null) {
            itemMeta.setDisplayName(name);
        }

        if(lore != null) {
            ArrayList<String> list = new ArrayList<>();
            list.add(lore);
            itemMeta.setLore(list);
        }

        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public static ItemStack createItemStack(Material material, int amount, String name) {
        return createItemStack(material, amount, name, null);
    }

    public static ItemStack createItemStack(Material material, int amount) {
        return createItemStack(material, amount, null, null);
    }

    public static boolean materialIsChestplate(Material mat) {
        return PluginConfig.instance.getChestplates().contains(mat);
    }

    public static boolean materialIsLeggings(Material mat) {
        return PluginConfig.instance.getLeggings().contains(mat);
    }

    public static boolean materialIsBoots(Material mat) {
        return PluginConfig.instance.getBoots().contains(mat);
    }
}
