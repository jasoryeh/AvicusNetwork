package net.avicus.atlas.sets.thebridge.modules.capturearea;

import net.avicus.atlas.match.Match;
import net.avicus.atlas.module.groups.Competitor;
import net.avicus.atlas.module.groups.teams.Team;
import net.avicus.atlas.module.locales.LocalizedXmlString;
import net.avicus.atlas.module.objectives.Objective;
import net.avicus.atlas.util.Worlds;
import net.avicus.magma.util.region.Region;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.block.Block;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class CaptureAreaModule implements Objective {

    private final Match match;
    private final Optional<String> name;
    private final boolean shouldFill;
    private final Material fillWith;
    private final Optional<Team> captureDefender;
    private final Region captureArea;
    private final IllegalCaptureBehavior ifWrongPlayer;

    public CaptureAreaModule(Match match, Optional<String> name, boolean shouldFill, Material fillWith,
                             Optional<Team> captureDefender, Region captureArea, IllegalCaptureBehavior ifWrongPlayer) {
        this.match = match;
        this.name = name;
        this.shouldFill = shouldFill;
        this.fillWith = fillWith;
        this.captureDefender = captureDefender;
        this.captureArea = captureArea;
        this.ifWrongPlayer = ifWrongPlayer;
    }

    @Override
    public void initialize() {
        if(this.shouldFill) {
            // If we should fill, we replace it now.
            for (Chunk chunk : this.captureArea.getChunks(this.match.getWorld())) {
                Set<Block> blocks = Worlds.getBlocks(chunk);
                for (Block block : blocks) {
                    if(block.getType() == Material.AIR) {
                        block.setType(this.fillWith);
                    }
                }
            }
        }
    }

    @Override
    public LocalizedXmlString getName() {
        return null;
    }

    @Override
    public boolean canComplete(Competitor competitor) {
        //
        return false;
    }

    @Override
    public boolean isCompleted(Competitor competitor) {
        return false;
    }

    @Override
    public double getCompletion(Competitor competitor) {
        return 0;
    }

    @Override
    public boolean isIncremental() {
        return false;
    }
}
