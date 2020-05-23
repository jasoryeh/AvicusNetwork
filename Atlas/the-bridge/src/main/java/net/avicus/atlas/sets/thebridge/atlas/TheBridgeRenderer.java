package net.avicus.atlas.sets.thebridge.atlas;

import net.avicus.atlas.match.Match;
import net.avicus.atlas.module.groups.Competitor;
import net.avicus.atlas.module.objectives.Objective;
import net.avicus.atlas.sets.thebridge.modules.capturearea.CaptureAreaObjective;
import net.avicus.atlas.util.ObjectiveRenderer;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class TheBridgeRenderer extends ObjectiveRenderer {

    @Override
    public String getDisplay(Match match, Competitor competitor, Player viewer, Objective objective,
                             boolean showName) {
        StringBuilder line = new StringBuilder();

        if(objective instanceof CaptureAreaObjective) {
            CaptureAreaObjective captureArea = (CaptureAreaObjective) objective;

            line.append(getColor(captureArea.getCompletion(competitor)));
            line.append(captureArea.getPoints(competitor));
            line.append(ChatColor.GRAY);
            line.append(" / ");
            line.append(ChatColor.WHITE);
            line.append(captureArea.getPointsToWin());

            if(showName) {
                line.append(ChatColor.WHITE);
                line.append(" - ");
                line.append(captureArea.getName(viewer));
            }
        }

        return line.toString();
    }

    public ChatColor getColor(int points, int outof) {
        return getColor( ((double) points) / ((double) outof) );
    }

    public ChatColor getColor(double percent) {
        if(percent <= 0.25) {
            return ChatColor.RED;
        } else if(percent <= 0.5) {
            return ChatColor.YELLOW;
        } else if(percent <= 0.75) {
            return ChatColor.GOLD;
        } else if(percent >= 1.0) {
            return ChatColor.GREEN;
        } else {
            return ChatColor.MAGIC;
        }
    }
}
