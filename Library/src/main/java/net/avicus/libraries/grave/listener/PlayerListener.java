package net.avicus.libraries.grave.listener;

import net.avicus.libraries.grave.GravePlugin;
import net.avicus.libraries.grave.event.*;
import net.avicus.libraries.tracker.Lifetime;
import net.avicus.libraries.tracker.Lifetimes;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.joda.time.Instant;

import java.util.ArrayList;
import java.util.List;

public class PlayerListener implements Listener {

    private final GravePlugin grave;

    public PlayerListener(GravePlugin grave) {
        this.grave = grave;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onEntityDeath(org.bukkit.event.entity.EntityDeathEvent event) {
        Bukkit.getLogger().info("Grave is handling entity death: " + event.getEntity().getName());
        LivingEntity entity = event.getEntity();
        Lifetime lifetime = Lifetimes.getLifetime(entity);
        Location location = entity.getLocation();
        Instant time = Instant.now();

        EntityDeathEvent call;

        int droppedExp = event.getDroppedExp();
        List<ItemStack> drops = new ArrayList<>(event.getDrops());

        // EntityDeathEvent or EntityDeathBy____Event??
        if (lifetime.getLastDamage() == null
                || lifetime.getLastDamage().getInfo().getResolvedDamager() == null) {
            if (entity instanceof Player) {
                call = new PlayerDeathEvent((Player) entity, location, lifetime, time, drops, droppedExp);
            } else {
                call = new EntityDeathEvent(entity, location, lifetime, time, drops, droppedExp);
            }
        } else {
            LivingEntity cause = lifetime.getLastDamage().getInfo().getResolvedDamager();

            if (entity instanceof Player) {
                if (cause instanceof Player) {
                    call = new PlayerDeathByPlayerEvent((Player) entity, location, lifetime, time, drops,
                            droppedExp, (Player) cause);
                } else {
                    call = new PlayerDeathByEntityEvent<>((Player) entity, location, lifetime, time, drops,
                            droppedExp, cause);
                }
            } else {
                if (cause instanceof Player) {
                    call = new EntityDeathByPlayerEvent(entity, location, lifetime, time, drops, droppedExp,
                            (Player) cause);
                } else {
                    call = new EntityDeathByEntityEvent<>(entity, location, lifetime, time, drops, droppedExp,
                            cause);
                }
            }
        }

        // Call event!
        grave.callEvent(call);

        // Apply changes in drops
        event.getDrops().clear();
        event.setDroppedExp(call.getDroppedExp());
        for (ItemStack itemStack : call.getDrops()) {
            location.getWorld().dropItemNaturally(location, itemStack);
        }
        Bukkit.getLogger().info("Grave has handled death event: " + event.getEntity().getName());

    }
}
