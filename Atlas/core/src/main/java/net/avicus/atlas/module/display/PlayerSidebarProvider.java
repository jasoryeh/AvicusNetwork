package net.avicus.atlas.module.display;

import com.keenant.tabbed.item.PlayerTabItem.PlayerProvider;
import net.avicus.atlas.Atlas;
import net.avicus.atlas.match.Match;
import net.avicus.magma.network.user.Users;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;

public class PlayerSidebarProvider implements PlayerProvider<String> {

    private final Match match;
    private final Player viewer;

    public PlayerSidebarProvider(Match match, Player viewer) {
        this.match = match;
        this.viewer = viewer;
    }

    @Override
    public String get(Player player) {
        if (!player.isOnline()) {
            return ChatColor.STRIKETHROUGH + player.getName();
        }
        final String prefix = org.bukkit.ChatColor.translateAlternateColorCodes('&', Users.getMeta(player).getLeft());
        return prefix + org.bukkit.ChatColor.RESET + Atlas.get().getBridge().displayName(this.match, this.viewer, player);
    }
}
