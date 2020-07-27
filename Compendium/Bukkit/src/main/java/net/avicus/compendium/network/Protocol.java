package net.avicus.compendium.network;

import com.google.common.collect.Range;
import org.bukkit.entity.Player;
import us.myles.ViaVersion.api.Via;

import java.util.UUID;

public final class Protocol {

    public static final Range<Integer> V1_11 = Range.closed(315, 316);
    public static final Range<Integer> V1_10 = Range.closed(210, 210);
    public static final Range<Integer> V1_9 = Range.closed(107, 110);
    public static final Range<Integer> V1_8 = Range.closed(47, 47);
    public static final Range<Integer> V1_7 = Range.closed(4, 5);

    public static final Range<Integer> HIGHER_THAN_1_8 = Range.greaterThan(47);
    public static final Range<Integer> LOWER_THAN_1_9 = Range.lessThan(107);

    private Protocol() {
    }

    public static boolean hasVia() {
        try {
            Class.forName("us.myles.ViaVersion.api.Via");
            return true;
        } catch(Exception e) {
            return false;
        }
    }

    public static int versionOf(final UUID uuid) {
        return hasVia() ? Via.getAPI().getPlayerVersion(uuid) : -1;
    }

    public static int versionOf(final Player player) {
        return hasVia() ? Via.getAPI().getPlayerVersion(player.getUniqueId()) : player.getProtocolVersion();
    }
}
