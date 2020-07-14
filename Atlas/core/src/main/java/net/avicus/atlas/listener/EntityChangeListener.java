package net.avicus.atlas.listener;

import net.avicus.atlas.event.player.PlayerCoarseMoveEvent;
import net.avicus.atlas.event.player.PlayerDeathByEntityEvent;
import net.avicus.atlas.event.player.PlayerDeathByPlayerEvent;
import net.avicus.atlas.event.player.PlayerDeathEvent;
import net.avicus.atlas.event.world.EntityChangeEvent;
import net.avicus.atlas.event.world.EntityChangeEvent.Action;
import net.avicus.atlas.event.world.EntityDeathByEntityEvent;
import net.avicus.atlas.event.world.EntityDeathByPlayerEvent;
import net.avicus.atlas.event.world.EntityDeathEvent;
import net.avicus.atlas.util.Events;
import net.avicus.atlas.util.external.tracker.Lifetime;
import net.avicus.atlas.util.external.tracker.Lifetimes;
import net.avicus.atlas.util.external.tracker.util.EventUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.*;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.hanging.HangingPlaceEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.vehicle.VehicleDestroyEvent;
import org.bukkit.inventory.ItemStack;
import org.joda.time.Instant;

import java.util.ArrayList;
import java.util.List;

public class EntityChangeListener implements Listener {

    private boolean callEntityChange(Entity whoChanged, Entity entity, Event cause, Action action) {
        return Events.call(new EntityChangeEvent<>(whoChanged, entity, cause, action)).isCancelled();
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onHangingPlace(HangingPlaceEvent event) {
        boolean cancel = callEntityChange(event.getPlayer(), event.getEntity(), event, Action.PLACE);
        event.setCancelled(cancel);
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onHangingBreak(HangingBreakByEntityEvent event) {
        boolean cancel = callEntityChange(event.getRemover(), event.getEntity(), event, Action.BREAK);
        event.setCancelled(cancel);
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        // Rotating item frame
        if (event.getRightClicked() instanceof ItemFrame || event
                .getRightClicked() instanceof ArmorStand) {
            boolean cancel = callEntityChange(event.getPlayer(), event.getRightClicked(), event,
                    Action.CHANGE);
            event.setCancelled(cancel);
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        // Get item from item frame
        if (event.getEntity() instanceof ItemFrame) {
            ItemFrame frame = (ItemFrame) event.getEntity();

            boolean cancel = callEntityChange(event.getEntity(), frame, event, Action.BREAK);
            event.setCancelled(cancel);
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onVehicleDestroy(VehicleDestroyEvent event) {
        boolean cancel = callEntityChange(event.getAttacker(), event.getVehicle(), event, Action.BREAK);
        event.setCancelled(cancel);
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
        Events.call(call);

        // Apply changes in drops
        event.getDrops().clear();
        event.setDroppedExp(call.getDroppedExp());
        for (ItemStack itemStack : call.getDrops()) {
            location.getWorld().dropItemNaturally(location, itemStack);
        }
        Bukkit.getLogger().info("Grave has handled death event: " + event.getEntity().getName());

    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerCoarseMoveCall(PlayerMoveEvent event) {
        Location from = event.getFrom();
        Location to = event.getTo();

        if (from.getBlockX() == to.getBlockX()) {
            if (from.getBlockY() == to.getBlockY()) {
                if (from.getBlockZ() == to.getBlockZ()) {
                    return;
                }
            }
        }

        PlayerCoarseMoveEvent call = new PlayerCoarseMoveEvent(event.getPlayer(), from, to);
        call.setCancelled(event.isCancelled());

        for (EventPriority priority : EventPriority.values()) {
            EventUtil.callEvent(call, PlayerCoarseMoveEvent.getHandlerList(), priority);
        }

        event.setCancelled(call.isCancelled());
        event.setFrom(call.getFrom());
        event.setTo(call.getTo());
    }
}
