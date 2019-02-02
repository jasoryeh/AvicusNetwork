package net.avicus.atlas.module.damagetrack;

import com.google.common.util.concurrent.AtomicDouble;
import lombok.Getter;
import net.avicus.atlas.match.Match;
import net.avicus.atlas.match.MatchFactory;
import net.avicus.atlas.module.Module;
import net.avicus.atlas.util.AtlasTask;
import net.avicus.atlas.util.xml.XmlElement;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;


public class DamageTrackModule implements Module {

    @Getter
    private final Match match;
    private final MatchFactory factory;
    private final XmlElement root;

    public DamageTrackModule(Match match, MatchFactory factory, XmlElement root) {
        this.match = match;
        this.factory = factory;
        this.root = root;

        this.damagesTrack = new ConcurrentHashMap<>();
    }

    @Getter
    private ConcurrentHashMap<UUID, ConcurrentHashMap<UUID, AtomicDouble>> damagesTrack;

    @EventHandler
    public void onAttack(EntityDamageByEntityEvent damageEvent) {
        if(!(damageEvent.getEntity() instanceof Player) &&
                !(damageEvent.getDamager() instanceof Player)) {
            return;
        }

        Player attacker = ((Player) damageEvent.getDamager());
        Player defender = ((Player) damageEvent.getEntity());
        double damage = damageEvent.getDamage();

        AtlasTask.of(() -> this.trackDamage(attacker, defender, damage)).nowAsync();
    }

    public void trackDamage(Player attacker, Player defender, double damageDealt) {
        if(!damagesTrack.contains(attacker.getUniqueId())) {
            damagesTrack.put(attacker.getUniqueId(), new ConcurrentHashMap<>());
        }

        ConcurrentHashMap<UUID, AtomicDouble> dmgs = damagesTrack.get(attacker.getUniqueId());

        if(dmgs.contains(defender.getUniqueId())) {
            AtomicDouble previousDamage = dmgs.get(defender.getUniqueId());
            previousDamage.addAndGet(damageDealt);
            dmgs.put(defender.getUniqueId(), previousDamage);
        } else {
            dmgs.put(defender.getUniqueId(), new AtomicDouble(damageDealt));
        }

        damagesTrack.put(attacker.getUniqueId(), dmgs);
    }
}
