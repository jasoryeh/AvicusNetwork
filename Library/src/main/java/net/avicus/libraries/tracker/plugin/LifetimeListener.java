package net.avicus.libraries.tracker.plugin;

import net.avicus.libraries.tracker.Lifetimes;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

public class LifetimeListener implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerRespawn(final PlayerRespawnEvent event) {
        Lifetimes.getManager().newLifetime(event.getPlayer());
    }
}
