package net.avicus.atlas.module.channels;

import com.google.common.base.Preconditions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import me.lucko.luckperms.LuckPerms;
import me.lucko.luckperms.api.Contexts;
import me.lucko.luckperms.api.LuckPermsApi;
import me.lucko.luckperms.api.caching.UserData;
import net.avicus.atlas.Atlas;
import net.avicus.atlas.match.Match;
import net.avicus.atlas.module.Module;
import net.avicus.atlas.module.groups.Competitor;
import net.avicus.atlas.module.groups.Group;
import net.avicus.atlas.module.groups.GroupsModule;
import net.avicus.magma.module.prestige.PrestigeModule;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * This module is responsible for handling team and global chat channels.
 */
public class ChannelsModule implements Module {

    /**
     * Map of players that are currently talking in global chat.
     * If the value is false, they are taking in team chat.
     */
    private final Map<Player, Boolean> globalEnabled;
    /**
     * Match that this module exists in.
     */
    private final Match match;
    /**
     * If team chat is allowed during this match.
     */
    private final boolean allowTeamChat;
    /**
     * If global chat is allowed during this match.
     */
    private final boolean allowGlobalChat;

    /**
     * Constructor.
     * <p>
     * <p>Note: Either team chat or global chat must be enabled.</p>
     *
     * @param match           match that this module exists in
     * @param allowTeamChat   if team chat is allowed in this match
     * @param allowGlobalChat if global chat is allowed during this match
     */
    public ChannelsModule(Match match, boolean allowTeamChat, boolean allowGlobalChat) {
        Preconditions.checkArgument(allowTeamChat || allowGlobalChat,
                "Team chat or global chat must be enabled");
        this.match = match;
        this.allowTeamChat = allowTeamChat;
        this.allowGlobalChat = allowGlobalChat;
        this.globalEnabled = new HashMap<>();
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        this.globalEnabled.remove(event.getPlayer());
    }

    /**
     * Check if team chat is allowed in the match.
     *
     * @return if team chat is allowed in the match
     */
    public boolean isTeamChatAllowed() {
        return this.allowTeamChat;
    }

    /**
     * Check if global chat is enabled in the match.
     *
     * @return if global chat is enabled in the match
     */
    public boolean isGlobalChatAllowed() {
        return this.allowGlobalChat;
    }

    /**
     * Set if a player is  talking in global chat.
     *
     * @param player        player to check
     * @param globalEnabled if the player is  talking in global chat
     */
    public void setGlobalEnabled(Player player, boolean globalEnabled) {
        this.globalEnabled.put(player, globalEnabled);
    }

    /**
     * Check if a player is currently talking in global chat.
     *
     * @param player player to check
     * @return if the player is currently talking in global chat
     */
    public boolean isGlobalEnabled(Player player) {
        return this.allowGlobalChat && this.globalEnabled.getOrDefault(player, true);
    }

    /**
     * Get the recipients of a message.
     * This will either return all players or the players in the same group as the sender.
     *
     * @param sender sender of the message
     * @return list of players who are allowed to see the message
     */
    private List<Player> recipients(Player sender) {
        if (isGlobalEnabled(sender)) {
            return Bukkit.getOnlinePlayers().stream().collect(Collectors.toList());
        } else {
            GroupsModule groups = this.match.getRequiredModule(GroupsModule.class);
            Group group = groups.getGroup(sender);
            Competitor competitor = groups.getCompetitorOf(sender).orElse(null);

            return competitor == null ? group.getPlayers() : competitor.getPlayers();
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onAsyncPlayerChat(AsyncPlayerChatEvent event) {
        // LP API for prefixes start
        String fmat = Atlas.get().getConfig().getString("chat", "<%3$s%1$s%4$s> %2$s");

        Pair<String, String> meta = getMeta(event.getPlayer());

        fmat = StringUtils.replace(fmat, "%3$s", ChatColor.translateAlternateColorCodes('&', meta.getLeft()));
        fmat = StringUtils.replace(fmat, "%4$s", PrestigeModule.getPrefix(event.getPlayer(), false)
                + ChatColor.RESET + ChatColor.translateAlternateColorCodes('&', meta.getRight()));
        event.setFormat(fmat);
        // LP API for prefixes end

        event.getRecipients().clear();
        event.getRecipients().addAll(recipients(event.getPlayer()));

        if (!isGlobalEnabled(event.getPlayer())) {
            GroupsModule groups = this.match.getRequiredModule(GroupsModule.class);
            ChatColor color;
            Competitor competitor = groups.getCompetitorOf(event.getPlayer()).orElse(null);
            if (competitor == null) {
                color = groups.getGroup(event.getPlayer()).getChatColor();
            } else {
                color = competitor.getChatColor();
            }

            String format = color + "[Team] " + ChatColor.RESET + event.getFormat();
            event.setFormat(format);
        }
    }

    /**
     * Return a pair of suffix and prefix
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
