package net.avicus.atlas.util.external.tracker.listeners;

import net.avicus.atlas.Atlas;
import net.avicus.atlas.util.external.tracker.event.EntityDamageEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class DebugListener implements Listener {

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onEntityDamage(final EntityDamageEvent event) {
        String dbm = event.getEntity().toString() + " damaged for " + event.getDamage() + " raw half hearts at "
                + event.getLocation() + " info: " + event.getInfo() + " cancelled?" + (
                event.isCancelled() ? "yes" : "no");
        if(Atlas.get().getConfig().getBoolean("debug", false)) {
            Bukkit.broadcastMessage(dbm);
        } else {
            Bukkit.getLogger().info(dbm);
        }
    }
}
