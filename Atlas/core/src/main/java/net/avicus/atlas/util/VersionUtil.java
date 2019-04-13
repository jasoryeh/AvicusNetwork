package net.avicus.atlas.util;

import com.comphenix.protocol.ProtocolLibrary;

/**
 * Helper methods for identifying multiple versions of clients connected to the server
 */
public final class VersionUtil {

    private VersionUtil() { }

    /**
     * Is the player's client greater than 1.9?
     * @return True for >1.9, false <1.9
     */
    public static boolean isCombatUpdate() {
        // TODO: Replace with .getMinecraftVersion().isAtLeast
        String version = ProtocolLibrary.getProtocolManager().getMinecraftVersion().getVersion();
        return version.contains("1.9") || version.contains("1.10") || version.contains("1.11") || version.contains("1.12") || version.contains("1.13") || version.contains("1.14");
    }
}
