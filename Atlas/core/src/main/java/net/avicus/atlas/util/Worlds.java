package net.avicus.atlas.util;

import org.bukkit.Chunk;
import org.bukkit.Color;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

import java.util.HashSet;
import java.util.Set;

/**
 * Provides helper methods for World related operations such as particle spawning and conversion
 * from degrees to BlockFaces
 */
public class Worlds {

    // Simplistic yaw/blockface code from https://bukkit.org/threads/banner-rotation.374049/#post-3171315
    // Four axis only
    private static final BlockFace[] axis = {BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH,
            BlockFace.WEST};
    // Four axis and between
    private static final BlockFace[] radial = {BlockFace.NORTH, BlockFace.NORTH_EAST, BlockFace.EAST,
            BlockFace.SOUTH_EAST, BlockFace.SOUTH, BlockFace.SOUTH_WEST, BlockFace.WEST,
            BlockFace.NORTH_WEST};

    /**
     * Converts a yaw value into a BlockFace, assumes you want to use all available block faces
     *
     * @param yaw Degrees yaw or rotation
     * @return BlockFace equivalent
     */
    public static BlockFace toBlockFace(float yaw) {
        return toBlockFace(yaw, true);
    }

    /**
     * Converts a yaw into a BlockFace. Using sub-cardinal directions allows for partial direction
     * such as NE, SE, NW and SW.
     *
     * @param yaw Degrees yaw or rotation
     * @param useSubCardinalDirections Be more precise?
     * @return BlockFace equivalent
     */
    public static BlockFace toBlockFace(float yaw, boolean useSubCardinalDirections) {
        if (useSubCardinalDirections) {
            return radial[Math.round(yaw / 45f) & 0x7];
        } else {
            return axis[Math.round(yaw / 90f) & 0x3];
        }
    }

    /**
     * Create a colored particle at a location. Minecraft allows us to play almost any color
     * that's out there.
     *
     * @param location Where to play the particles.
     * @param viewRadius How far away you can be to view it.
     * @param color The color of the particles
     */
    public static void playColoredParticle(Location location, int viewRadius, Color color) {
        playColoredParticle(location, viewRadius, color.getRed(), color.getGreen(), color.getBlue());
    }

    /**
     * Create a colored particle at a location. Minecraft allows us to play any color given a RGB
     * value to define the color of your choice.
     *
     * @param location Where to play the particles.
     * @param viewRadius How far away you can be to view it.
     * @param red Red color value
     * @param green Green color value
     * @param blue Blue color value
     */
    public static void playColoredParticle(Location location, int viewRadius, int red, int green,
                                           int blue) {
        float r = (float) red / 255.0F;
        float g = (float) green / 255.0F;
        float b = (float) blue / 255.0F;
        location.getWorld().spigot()
                .playEffect(location, Effect.COLOURED_DUST, 0, 0, r, g, b, 1, 0, viewRadius);
    }

    public static Set<Block> getBlocks(Chunk chunk) {
        Set<org.bukkit.block.Block> blocks = new HashSet<org.bukkit.block.Block>();

        org.bukkit.craftbukkit.v1_8_R3.CraftChunk craftChunk = (org.bukkit.craftbukkit.v1_8_R3.CraftChunk) chunk;
        net.minecraft.server.v1_8_R3.Chunk handle = ((org.bukkit.craftbukkit.v1_8_R3.CraftChunk) chunk).getHandle();
        // Partial from getBlocks in chunk
        for (net.minecraft.server.v1_8_R3.ChunkSection section : handle.getSections()) {
            if(section == null || section.a()) continue;

            char[] blockIds = section.getIdArray();
            for (int i = 0; i < blockIds.length; i++) {
                // lookup in block registry
                net.minecraft.server.v1_8_R3.IBlockData blockData = net.minecraft.server.v1_8_R3.Block.d.a(blockIds[i]);
                if (blockData != null) { // normally there's a block check for materials here, but not today.
                    blocks.add(craftChunk.getBlock(
                            i & 0xf,
                            section.getYPosition() | (i >> 8),
                            (i >> 4) & 0xf)
                    );
                }
            }
        }

        return blocks;
    }
}
