package net.avicus.magma.network.server;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class ServerMenuListener implements Listener {

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_AIR
                && event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        // TODO: Fix the server gui, but for now send a message

        if (!Servers.isMenuOpener(event.getItem())) {
            return;
        }

        if(true) {
            event.getPlayer().sendMessage("");
            event.getPlayer().sendMessage(ChatColor.GREEN + "Please use /server or /servers.");
            event.getPlayer().sendMessage("");
            return;
        }

        event.setCancelled(true);

        Player player = event.getPlayer();

        ServerMenu menu = ServerMenu.fromConfig(player);
        menu.open();
    }
}
