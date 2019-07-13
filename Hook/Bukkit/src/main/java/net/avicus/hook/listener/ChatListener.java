package net.avicus.hook.listener;


import net.avicus.hook.HookConfig;
import net.avicus.magma.module.prestige.PrestigeModule;
import net.avicus.magma.network.user.Users;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatListener implements Listener {
    @EventHandler
    public void onAsyncPlayerChat(AsyncPlayerChatEvent event) {
        if (!HookConfig.Chat.isEnableFormat()) {
            return;
        }

        try {
            Class.forName("net.avicus.atlas.Atlas");

            // let atlas's channels module handle this.

            return;
        } catch (ClassNotFoundException e) {
            // atlas not found, no check needed
        }

        // LP API for prefixes start
        String fmat = HookConfig.Chat.getFormat();

        Pair<String, String> meta = Users.getMeta(event.getPlayer());

        fmat = StringUtils.replace(fmat, "%3$s",
                ChatColor.translateAlternateColorCodes('&', meta.getLeft()) +
                        PrestigeModule.getPrefix(event.getPlayer(), false));
        fmat = StringUtils.replace(fmat, "%4$s", ChatColor.translateAlternateColorCodes('&', meta.getRight()));
        event.setFormat(fmat);
        // LP API for prefixes end
    }
}
