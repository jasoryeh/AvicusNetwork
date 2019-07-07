package net.avicus.hook.listener;


import me.lucko.luckperms.LuckPerms;
import me.lucko.luckperms.api.Contexts;
import me.lucko.luckperms.api.LuckPermsApi;
import me.lucko.luckperms.api.caching.UserData;
import net.avicus.hook.HookConfig;
import net.avicus.magma.module.prestige.PrestigeModule;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.Objects;

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

        Pair<String, String> meta = getMeta(event.getPlayer());

        fmat = StringUtils.replace(fmat, "%3$s", ChatColor.translateAlternateColorCodes('&', meta.getLeft()));
        fmat = StringUtils.replace(fmat, "%4$s", PrestigeModule.getPrefix(event.getPlayer(), false)
                + ChatColor.RESET + ChatColor.translateAlternateColorCodes('&', meta.getRight()));
        event.setFormat(fmat);
        // LP API for prefixes end
    }

    /**
     * Return a pair of suffix and prefix
     *
     * @param p player
     * @return Pair, left - prefix, right - suffix
     */
    public static Pair<String, String> getMeta(Player p) {
        try {
            LuckPermsApi api = LuckPerms.getApi();
            UserData cache = Objects.requireNonNull(api.getUser(p.getUniqueId())).getCachedData();
            String prefix = cache.getMetaData(Contexts.global()).getPrefix();
            String suffix = cache.getMetaData(Contexts.global()).getSuffix();
            prefix = prefix != null ? prefix : "";
            suffix = suffix != null ? suffix : "";
            return Pair.of(prefix, suffix);
        } catch (Exception e) {
            e.printStackTrace();
            return Pair.of("", "");
        }
    }
}
