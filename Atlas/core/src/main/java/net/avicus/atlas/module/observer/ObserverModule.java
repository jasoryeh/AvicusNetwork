package net.avicus.atlas.module.observer;

import net.avicus.atlas.match.Match;
import net.avicus.atlas.module.Module;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Additional things to make observing through obs more interesting.
 */
public class ObserverModule implements Module {

    private final Match match;

    public ObserverModule(Match match) {
        this.match = match;
    }

    private static boolean isSpectatorTargeting(Player player, Player isTargeting) {
        if(player.getGameMode() == GameMode.SPECTATOR) {
            Entity spectatorTarget = player.getSpectatorTarget();
            return spectatorTarget instanceof Player && spectatorTarget.getUniqueId().equals(isTargeting.getUniqueId()) && ((Player) spectatorTarget).getGameMode() == GameMode.SPECTATOR;
        }
        return false;
    }

    private static List<Player> allObserving(Player player) {
        ArrayList<Player> observing = new ArrayList<>();
        for (Player p : Bukkit.getOnlinePlayers()) {
            Entity target = p.getSpectatorTarget();
            if(target instanceof Player && target.getUniqueId().equals(player.getUniqueId()) && ((Player) target).getGameMode() == GameMode.SPECTATOR) {
                observing.add(p);
            }
        }
        return observing;
    }

    /**
     * Events
     */

    @EventHandler
    public void logout(PlayerQuitEvent e) {
        List<Player> players = allObserving(e.getPlayer());

        for (Player player : players) {
            player.closeInventory();
        }
    }

    @EventHandler
    public void inventoryModify(InventoryMoveItemEvent e) {
        Entity actor = e.getActor();
        if(actor instanceof Player) {
            Player player = (Player) actor;
            if(player.getGameMode() == GameMode.SPECTATOR) {
                e.setCancelled(true);
            }

            for (Player player1 : allObserving(player)) {
                player1.updateInventory();
            }
        }
    }

    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent e) {
        Player player = ((Player) e.getPlayer());

        for (Player player1 : allObserving(player)) {
            player1.openInventory(e.getInventory());
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e) {
        Player player = ((Player) e.getPlayer());

        for (Player player1 : allObserving(player)) {
            player1.closeInventory();
        }
    }
}
