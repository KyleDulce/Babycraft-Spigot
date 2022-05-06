package src.me.someoneawesome.model.child;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.entity.Villager.*;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import src.me.someoneawesome.Babycraft;
import src.me.someoneawesome.config.ConfigInterface;
import src.me.someoneawesome.config.PluginConfig;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public class ChildEntity {
    private Child child;
    private ItemStack[] armor;
    private Location home;
    private UUID mobID;
    private boolean aiActive;
    private Profession clothesColor;

    private Villager mob;
    private Iterator<Profession> professions;
    private Runnable followFunction;
    private int followTaskId;

    ChildEntity(Child child, ItemStack[] armor, Location home, Profession clothing, Location spawnloc) {
        this.child = child;
        this.armor = armor;
        this.home = home;
        this.clothesColor = clothing;
        mobID = null;
        aiActive = true;
        followTaskId = -1;
        followFunction = null;

        professions = Arrays.asList(Profession.values()).iterator();
        while(!professions.next().equals(clothesColor)) {};

        mob = spawnloc.getWorld().spawn(spawnloc, Villager.class);
        mobID = mob.getUniqueId();
        mob.setProfession(clothing);
        mob.setAI(aiActive);
        mob.setBaby();
        mob.setAgeLock(true);
        mob.setCustomName(ChatColor.translateAlternateColorCodes('&',
                String.format("%s%s", child.getGender().getCodedChatColor(), child.getName())));
        mob.setCustomNameVisible(true);
        mob.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 1000, true, true));
        if(armor[0] != null && !(armor[0].getType().equals(Material.AIR))) {
            mob.getEquipment().setHelmet(armor[0]);
        }
        if(armor[1] != null && !(armor[1].getType().equals(Material.AIR))) {
            mob.getEquipment().setChestplate(armor[1]);
        }
        if(armor[2] != null && !(armor[2].getType().equals(Material.AIR))) {
            mob.getEquipment().setLeggings(armor[2]);
        }
        if(armor[3] != null && !(armor[3].getType().equals(Material.AIR))) {
            mob.getEquipment().setBoots(armor[3]);
        }
    }

    public void teleportHome() {
        refreshMob();
        if(!mob.getLocation().getChunk().isLoaded()) {
            mob.getLocation().getChunk().load();
        }
        mob.teleport(home);
    }

    public void despawn() {
        refreshMob();
        Location originalLocation = mob.getLocation();
        originalLocation.getChunk().load();
        if(!aiActive) {
            mob.setAI(true);
            aiActive = true;
        }
        if(followTaskId > 0) {
            stopFollowManual();
        }
        mob.setHealth(0);
        Child.removeFromRecord(child.getUuid());
        originalLocation.getChunk().unload(true);
    }

    public void follow(Player player) {
        if(followTaskId > 0) {
            stopFollowManual();
        }
        setAiState(true);
        followFunction = () -> {
            refreshMob();
            if(mob.getLocation().distance(player.getLocation()) > 20) {
                mob.teleport(player);
                player.sendMessage(ChatColor.RED + "Your child is being left behind! Slow down!");
            }
            mob.getTarget();
            mob.setTarget(player);
        };
        followTaskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(Babycraft.instance, followFunction, 0, 40);
        ReactionCreator.sendMessage(ActionMessage.FOLLOW, child, player);
    }

    private void stopFollow(Player player) {
        stopFollowManual();
        ReactionCreator.sendMessage(ActionMessage.STAY, child, player);
    }

    private void roam(Player player) {
        stopFollowManual();
        ReactionCreator.sendMessage(ActionMessage.ROAM, child, player);
    }

    private void stopFollowManual() {
        Bukkit.getScheduler().cancelTask(followTaskId);
        followTaskId = -1;
        refreshMob();
        mob.getTarget();
        mob.setTarget(null);
    }

    private void setAiState(boolean state) {
        refreshMob();
        aiActive = state;
        mob.setAI(aiActive);
    }

    private void setClothing(Profession clothing) {
        refreshMob();
        mob.setProfession(clothing);
        this.clothesColor = clothing;
        ConfigInterface.instance.children.setChildColor(child.getUuid(), clothing);
    }

    public void setNextClothing() {
        Profession selected = null;
        while(selected == null) {
            if(professions.hasNext()) {
                selected = professions.next();

                if(professions.equals(Profession.NONE)) {
                    selected = null;
                }
            } else {
                professions = Arrays.asList(Profession.values()).iterator();
            }
        }

        setClothing(selected);
    }

    public void setArmor(ItemStack item, EquipmentSlot slot) {
        refreshMob();

        switch (slot) {
            case HEAD ->
                mob.getEquipment().setHelmet(item);
            case CHEST ->
                    mob.getEquipment().setChestplate(item);
            case LEGS ->
                    mob.getEquipment().setLeggings(item);
            case FEET ->
                    mob.getEquipment().setBoots(item);
        }

        ConfigInterface.instance.children.setChildArmor(child.getUuid(), slot, item);
    }

    public void setHome(Location location) {
        refreshMob();
        home = location;
        ConfigInterface.instance.children.setChildHome(child.getUuid(), location);
    }

    public Location getCurrentLocation() {
        refreshMob();
        return mob.getLocation();
    }

    List<Entity> getNearbyEntities(double radius) {
        refreshMob();
        return mob.getNearbyEntities(radius, radius, radius);
    }

    private void refreshMob() {
        if(mob.getLocation().getChunk().isLoaded()) {
            mob = (Villager) Bukkit.getServer().getEntity(mobID);
        }
    }
}
