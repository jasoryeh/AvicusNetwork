package net.avicus.hook.listener;

import net.avicus.hook.gadgets.backpack.BackpackMenu;
import net.avicus.hook.gadgets.shop.ShopMenu;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import static net.avicus.hook.gadgets.backpack.BackpackMenu.isBackpackOpener;
import static net.avicus.hook.gadgets.shop.ShopMenu.isShopOpener;

public class BackpackShopListener implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void onBackpackOpen(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK
                && event.getAction() != Action.RIGHT_CLICK_AIR) {
            return;
        }

        if (isBackpackOpener(event.getItem())) {
            event.setCancelled(true);

            BackpackMenu menu = new BackpackMenu(event.getPlayer());
            menu.open();
        } else if (isShopOpener(event.getItem())) {
            event.setCancelled(true);

            ShopMenu menu = new ShopMenu(event.getPlayer());
            menu.open();
        }

    }
}
