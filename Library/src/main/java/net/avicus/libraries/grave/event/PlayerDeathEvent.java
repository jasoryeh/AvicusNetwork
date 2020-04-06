package net.avicus.libraries.grave.event;

import lombok.Getter;
import lombok.ToString;
import net.avicus.libraries.tracker.Lifetime;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.joda.time.Instant;

import java.util.List;

@ToString(callSuper = true)
public class PlayerDeathEvent extends EntityDeathEvent {

    @Getter
    private final Player player;

    public PlayerDeathEvent(Player player, Location location, Lifetime lifetime, Instant time,
                            List<ItemStack> drops, int droppedExp) {
        super(player, location, lifetime, time, drops, droppedExp);
        this.player = player;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public HandlerList getHandlers() {
        return handlers;
    }
}
