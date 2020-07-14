package net.avicus.atlas.module.damage;

import lombok.ToString;
import net.avicus.atlas.match.Match;
import net.avicus.atlas.module.Module;
import net.avicus.atlas.module.checks.Check;
import net.avicus.atlas.module.checks.CheckContext;
import net.avicus.atlas.module.checks.variable.*;
import net.avicus.atlas.util.external.tracker.event.PlayerDamageEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

@ToString(exclude = "match")
public class DamageModule implements Module {

    private final Match match;
    private final Check disable;

    public DamageModule(Match match, Check disable) {
        this.match = match;
        this.disable = disable;
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerDamage(PlayerDamageEvent event) {
        VictimVariable victim = new VictimVariable(this.match);
        victim.add(new PlayerVariable(event.getEntity()));
        victim.add(new LocationVariable(event.getLocation()));

        CheckContext context = new CheckContext(this.match);
        context.add(victim);
        context.add(new LocationVariable(event.getLocation()));
        context.add(new DamageVariable(event.getInfo().getDamageCause()));

        if (event.getInfo().getResolvedDamager() instanceof Player) {
            Player cause = (Player) event.getInfo().getResolvedDamager();

            AttackerVariable attacker = new AttackerVariable(this.match);
            attacker.add(new PlayerVariable(cause));
            attacker.add(new LocationVariable(cause.getLocation()));
            context.add(attacker);
        }

        if (this.disable.test(context).passes()) {
            event.setCancelled(true);
        }
    }
}
