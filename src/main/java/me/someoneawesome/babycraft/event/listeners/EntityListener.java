package me.someoneawesome.babycraft.event.listeners;

import me.someoneawesome.babycraft.model.child.Child;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class EntityListener implements Listener {
    @EventHandler
    public void onEntityDamageEntity(EntityDamageByEntityEvent event) {
        Child.handlePlayerAttack(event);
    }
}
