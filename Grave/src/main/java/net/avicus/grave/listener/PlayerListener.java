package net.avicus.grave.listener;

import net.avicus.grave.GravePlugin;
import net.avicus.grave.event.*;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.joda.time.Instant;
import tc.oc.tracker.Lifetime;
import tc.oc.tracker.Lifetimes;

import java.util.ArrayList;
import java.util.List;

public class PlayerListener implements Listener {

    private final GravePlugin grave;

    public PlayerListener(GravePlugin grave) {
        this.grave = grave;
    }

    @EventHandler
    public void onEntityDeath(org.bukkit.event.entity.EntityDeathEvent event) {
        LivingEntity entity = event.getEntity();
        Lifetime lifetime = Lifetimes.getLifetime(entity);
        Location location = entity.getLocation();
        Instant time = Instant.now();

        EntityDeathEvent call;

        int droppedExp = event.getDroppedExp();
        List<ItemStack> drops = new ArrayList<>();
        drops.addAll(event.getDrops());

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

    }
}
