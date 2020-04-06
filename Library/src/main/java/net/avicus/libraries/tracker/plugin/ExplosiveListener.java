package net.avicus.libraries.tracker.plugin;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.avicus.libraries.tracker.Trackers;
import net.avicus.libraries.tracker.trackers.DispenserTracker;
import net.avicus.libraries.tracker.trackers.ExplosiveTracker;
import net.avicus.libraries.tracker.trackers.OwnedMobTracker;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.ExplosionPrimeByEntityEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;

import java.util.List;
import java.util.Map;

public class ExplosiveListener implements Listener {

    private final ExplosiveTracker tracker;

    public ExplosiveListener(ExplosiveTracker tracker) {
        this.tracker = tracker;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBlockPlace(BlockPlaceEvent event) {
        if (!this.tracker.isEnabled(event.getBlock().getWorld())) {
            return;
        }

        if (event.getBlock().getType() == Material.TNT) {
            this.tracker.setPlacer(event.getBlock(), event.getPlayer());
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        if (!this.tracker.isEnabled(event.getBlock().getWorld())) {
            return;
        }

        this.tracker.setPlacer(event.getBlock(), null);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBlockPistonExtend(BlockPistonExtendEvent event) {
        if (!this.tracker.isEnabled(event.getBlock().getWorld())) {
            return;
        }

        Map<Block, Player> updated = Maps.newHashMap();
        List<Block> toremove = Lists.newLinkedList();

        for (Block block : event.getBlocks()) {
            Player placer = this.tracker.getPlacer(block);
            if (placer != null) {
                toremove.add(block);
                updated.put(block.getRelative(event.getDirection()), placer);
            }
        }

        for (Block block : toremove) {
            Player newPlacer = updated.remove(block);
            this.tracker.setPlacer(block, newPlacer);
        }

        for (Map.Entry<Block, Player> entry : updated.entrySet()) {
            this.tracker.setPlacer(entry.getKey(), entry.getValue());
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBlockPistonRetract(BlockPistonRetractEvent event) {
        if (!this.tracker.isEnabled(event.getBlock().getWorld())) {
            return;
        }

        if (event.isSticky()) {
            Block newBlock = event.getBlock().getRelative(event.getDirection());
            Block oldBlock = newBlock.getRelative(event.getDirection());
            Player player = this.tracker.getPlacer(oldBlock);
            if (player != null) {
                this.tracker.setPlacer(oldBlock, null);
                this.tracker.setPlacer(newBlock, player);
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onTNTIgnite(ExplosionPrimeEvent event) {
        if (!this.tracker.isEnabled(event.getEntity().getWorld())) {
            return;
        }

        if (event.getEntity() instanceof TNTPrimed) {
            TNTPrimed tnt = (TNTPrimed) event.getEntity();
            Player owner = null;
            if (event instanceof ExplosionPrimeByEntityEvent) {
                Entity primer = ((ExplosionPrimeByEntityEvent) event).getPrimer();
                if (primer instanceof TNTPrimed) {
                    owner = this.tracker.getOwner((TNTPrimed) primer);
                } else {
                    if (!primer.isDead()) {
                        owner = Trackers.getTracker(OwnedMobTracker.class).getOwner((LivingEntity) primer);
                    }
                }
            }

            if (owner == null) {
                Player placer = this.tracker.getPlacer(tnt.getLocation().getBlock());
                if (placer != null) {
                    owner = placer;
                }
            }

            if (owner != null) {
                this.tracker.setOwner(tnt, owner);
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onDispense(BlockDispenseEntityEvent event) {
        if (event.getEntity() instanceof TNTPrimed) {
            OfflinePlayer placer = Trackers.getTracker(DispenserTracker.class)
                    .getPlacer(event.getBlock());
            if (placer != null && placer.isOnline()) {
                this.tracker.setOwner((TNTPrimed) event.getEntity(), placer.getPlayer());
            }
        }
    }
}
