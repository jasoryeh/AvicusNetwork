package tc.oc.tracker.plugin;

import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import tc.oc.tracker.event.PlayerCoarseMoveEvent;
import tc.oc.tracker.util.EventUtil;

public class CustomEventListener implements Listener {

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
