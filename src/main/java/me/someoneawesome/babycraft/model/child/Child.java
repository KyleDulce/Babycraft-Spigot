package me.someoneawesome.babycraft.model.child;

import me.someoneawesome.babycraft.config.interfaces.ChildrenConfigInterface;
import me.someoneawesome.babycraft.model.messaging.FormattedMessage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import reactor.core.publisher.Flux;
import me.someoneawesome.babycraft.PluginLogger;
import me.someoneawesome.babycraft.config.ConfigInterface;
import me.someoneawesome.babycraft.config.PluginConfig;
import me.someoneawesome.babycraft.model.Gender;

import java.util.*;

public class Child {
    private static HashMap<UUID, Child> childUIDToChild;
    private static PluginLogger LOGGER = PluginLogger.getLogger(Child.class);

    public static Optional<Child> getChildFromUID(UUID uuid) {
        if(childUIDToChild.containsKey(uuid)) {
            return Optional.of(childUIDToChild.get(uuid));
        }
        return Optional.empty();
    }

    static void removeFromRecord(UUID uuid) {
        childUIDToChild.remove(uuid);
    }

    private final UUID uuid;
    private final String childName;
    private final Gender gender;
    private final ChildEntity childEntity;

    private final UUID parent1;
    private final UUID parent2;

    private Child(UUID uuid, String name, Gender gender, UUID parent1, UUID parent2,
                  ItemStack[] armor, Location home, Villager.Profession clothing, Location spawn) {
        childUIDToChild.put(uuid, this);
        this.uuid = uuid;
        this.childName = name;
        this.gender = gender;
        this.parent1 = parent1;
        this.parent2 = parent2;
        childEntity = new ChildEntity(this, armor, home, clothing, spawn);
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getName() {
        return childName;
    }

    public ChildEntity getChildEntity() {
        return childEntity;
    }

    public Gender getGender() {
        return gender;
    }

    public void sayMessage(String message) {
        List<Entity> entities = childEntity.getNearbyEntities(PluginConfig.instance.getSpeakingRadius());
        double squaredRadius = PluginConfig.instance.getSpeakingRadius() ^ 2;

        for(Entity entity : entities) {
            //keep entities within a sphere, not a square
            if(entity.getLocation().distanceSquared(childEntity.getCurrentLocation()) > squaredRadius) {
                continue;
            }

            if(entity instanceof Player) {
                entity.sendMessage(message);
            }
        }
    }

    public boolean isParent(Player player) {
        return player.getUniqueId().equals(parent1) || player.getUniqueId().equals(parent2);
    }

    public UUID getOtherParent(Player parent) {
        if(isParent(parent)) {
            return parent.getUniqueId().equals(parent1) ? parent2 : parent1;
        }
        return null;
    }

    public static void despawnAll() {
        FormattedMessage.builder()
                .appendMessage(FormattedMessage.FormattedTextComponent.builder()
                        .setContent("DESPAWNING ALL CHILDREN you can respawn them later!")
                        .setBold()
                        .setColor(ChatColor.GREEN)
                        .build()
                ).build()
                .broadcastMessage();
        Flux.just(childUIDToChild)
                .doOnNext(collection -> LOGGER.info("Despawning All Children"))
                .flatMap(children -> Flux.fromIterable(children.values()))
                .doOnNext(child -> child.childEntity.despawn())
                .buffer()
                .doOnNext(children -> LOGGER.info("Despawned All Children"))
                .subscribe();
    }

    public static Child spawnChildFromConfig(UUID uuid, Location spawnLocation) {
        ChildrenConfigInterface configInterface = ConfigInterface.instance.children;
        String name = configInterface.getChildName(uuid);
        Gender gender = configInterface.getChildGender(uuid);
        List<UUID> parents = configInterface.getChildParents(uuid);
        ItemStack[] armor = configInterface.getChildArmor(uuid);
        Location home = configInterface.getChildHome(uuid);
        Villager.Profession profession = configInterface.getChildColor(uuid);

        return new Child(uuid, name, gender, parents.get(0), parents.size() > 1? parents.get(1) : null,
                armor, home, profession, spawnLocation);
    }

    public static void newChildToConfig(String name, List<UUID> parents, Gender gender, Location defaultHome) {
        ChildrenConfigInterface configInterface = ConfigInterface.instance.children;
        UUID uuid = UUID.randomUUID();

        configInterface.setChildName(uuid, name);
        configInterface.setChildParents(uuid, parents);
        configInterface.setChildArmor(uuid, new ItemStack[4]);
        configInterface.setChildGender(uuid, gender);
        configInterface.setChildHome(uuid, defaultHome);
        configInterface.setChildColor(uuid, Villager.Profession.LIBRARIAN);

        Flux.fromIterable(parents)
                .doOnNext(parent -> ConfigInterface.instance
                        .players
                        .addPlayerChild(parent, uuid))
                .subscribe();
    }

    public static void removeChild(UUID uuid) {
        ChildrenConfigInterface configInterface = ConfigInterface.instance.children;
        if(!configInterface.contains(uuid)) {
            return;
        }

        Flux.just(uuid)
                .flatMap(childUid -> Flux.fromIterable(configInterface.getChildParents(childUid)))
                .doOnNext(parentUid -> ConfigInterface.instance.players.removePlayerChildren(parentUid, uuid))
                .doOnComplete(() -> configInterface.removeChild(uuid))
                .subscribe();
    }

    public static void handlePlayerInteract(PlayerInteractEntityEvent event) {
        Entity clicked = event.getRightClicked();

        if(EntityType.VILLAGER.equals(clicked.getType())) {
            if(ChildEntity.mobIdToChildId.containsKey(clicked.getUniqueId())) {
                Child child = Child.childUIDToChild.get(ChildEntity.mobIdToChildId.get(clicked.getUniqueId()));
                Player player = event.getPlayer();
                if(child.isParent(player)) {
                    ChildMenu.openChildMenu(player, child);
                } else {
                    player.sendMessage(ChatColor.RED + "The child does not want you to access their menu");
                }
            }
        }
    }

    public static void handlePlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        Flux.fromIterable(childUIDToChild.values())
                .filter(child -> !child.isParent(player))
                .filter(child -> {
                    UUID parent2 = child.getOtherParent(player);
                    return parent2 != null && Bukkit.getOfflinePlayer(parent2).isOnline();
                })
                .doOnNext(child -> child.childEntity.despawn())
                .subscribe();
    }

    public static void handlePlayerAttack(EntityDamageByEntityEvent event) {
        Entity damager = event.getDamager();
        Entity damaged = event.getEntity();

        if(!(damager instanceof Player)) {
            return;
        }

        if(!(damaged instanceof Villager)) {
            return;
        }

        Player player = (Player) damager;
        Villager villager = (Villager) damaged;

        if(!ChildEntity.mobIdToChildId.containsKey(villager.getUniqueId())) {
            return;
        }

        ReactionCreator.sendMessage(ActionMessage.ATTACKED,
                childUIDToChild.get(ChildEntity.mobIdToChildId.get(villager.getUniqueId())),
                player);
    }
}
