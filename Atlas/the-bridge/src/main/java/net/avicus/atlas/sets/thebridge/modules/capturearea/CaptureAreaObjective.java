package net.avicus.atlas.sets.thebridge.modules.capturearea;

import lombok.Getter;
import net.avicus.atlas.match.Match;
import net.avicus.atlas.module.groups.Competitor;
import net.avicus.atlas.module.groups.teams.Team;
import net.avicus.atlas.module.locales.LocalizedXmlString;
import net.avicus.atlas.module.objectives.IntegerObjective;
import net.avicus.atlas.util.Worlds;
import net.avicus.compendium.number.NumberAction;
import net.avicus.magma.util.region.Region;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.Random;
import java.util.Set;

/**
 * Represents an area that people jump through to score points.
 */
public class CaptureAreaObjective implements IntegerObjective, Listener {

    private final Match match;
    private final Optional<String> name;
    private final boolean shouldFill;
    private final Material fillWith;
    private final Optional<Team> capturer;
    private final Region captureArea;
    @Getter
    private final IllegalCaptureBehavior ifWrongPlayer;

    @Getter
    private int pointsCaptured;
    @Getter
    private int pointsToWin;

    public CaptureAreaObjective(Match match, Optional<String> name, int pointsToWin, Material fillWith, boolean shouldFill,
                                Optional<Team> capturer, Region captureArea, IllegalCaptureBehavior ifWrongPlayer) {
        this.match = match;
        this.name = name;
        this.shouldFill = shouldFill;
        this.fillWith = fillWith;
        this.capturer = capturer;
        this.captureArea = captureArea;
        this.ifWrongPlayer = ifWrongPlayer;
        this.pointsCaptured = 0;
        this.pointsToWin = pointsToWin;
    }

    @Override
    public void initialize() {
        if(this.shouldFill) {
            // If we should fill, we replace it now.
            for (Chunk chunk : this.captureArea.getChunks(this.match.getWorld())) {
                Set<Block> blocks = Worlds.getBlocks(chunk);
                for (Block block : blocks) {
                    if(block.getType() == Material.AIR
                            && captureArea.contains(block)) {
                        block.setType(this.fillWith); // fill only blocks in the capture region with blocks that can be fell in through such as portals.
                    }
                }
            }
        }
    }

    @Override
    public LocalizedXmlString getName() {
        if(this.capturer.isPresent()) {
            return this.capturer.get().getName();
        } else {
            Random random = new Random();
            random.setSeed(((long) "Input is awesome".length()));
            return new LocalizedXmlString(this.captureArea.getRandomPosition(random).toString());
        }
    }

    @Override
    public int getPoints(Competitor competitor) {
        if(this.canComplete(competitor)) {
            return this.pointsCaptured;
        }
        return -1;
    }

    @Override
    public void modify(Competitor competitor, int amount, NumberAction action, @Nullable Player actor) {
        if(this.canComplete(competitor)) {
            this.pointsCaptured = action.perform(this.pointsCaptured, amount);
        }
        // silent fail.
    }

    @Override
    public boolean canComplete(Competitor competitor) {
        return this.capturer.isPresent() && this.capturer.get().equals(competitor.getGroup());
    }

    @Override
    public boolean isCompleted(Competitor competitor) {
        return this.canComplete(competitor) && (this.getPoints(competitor) >= this.pointsToWin);
    }

    @Override
    public double getCompletion(Competitor competitor) {
        return this.canComplete(competitor) ? (((double) this.getPoints(competitor)) / ((double) this.pointsToWin)) : 0.0;
    }

    @Override
    public boolean isIncremental() {
        return true;
    }

    public boolean isInside(Block block) {
        return this.captureArea.contains(block);
    }
}
