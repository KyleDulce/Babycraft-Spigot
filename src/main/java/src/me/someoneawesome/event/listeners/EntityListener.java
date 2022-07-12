package src.me.someoneawesome.event.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import src.me.someoneawesome.model.child.Child;

public class EntityListener implements Listener {
    @EventHandler
    public void onEntityDamageEntity(EntityDamageByEntityEvent event) {
        Child.handlePlayerAttack(event);
    }
}
