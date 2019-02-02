package net.avicus.atlas.module.damagetrack;

import com.google.common.util.concurrent.AtomicDouble;
import lombok.Getter;
import net.avicus.atlas.match.Match;
import net.avicus.atlas.match.MatchFactory;
import net.avicus.atlas.module.Module;
import net.avicus.atlas.util.AtlasTask;
import net.avicus.atlas.util.Translations;
import net.avicus.atlas.util.xml.XmlElement;
import net.avicus.compendium.locale.text.Localizable;
import net.avicus.compendium.locale.text.UnlocalizedText;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.*;
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
            dmgs.put(defender.getUniqueId(), new AtomicDouble(dmgs.get(defender.getUniqueId()).get()
                    + damageDealt));
        } else {
            dmgs.put(defender.getUniqueId(), new AtomicDouble(damageDealt));
        }

        damagesTrack.put(attacker.getUniqueId(), dmgs);
    }

    public void reset(Player reset) {
        damagesTrack.put(reset.getUniqueId(), new ConcurrentHashMap<>());
    }

    public Map<UUID, Double> damageTo(Player to) {
        Map<UUID, Double> result = new HashMap<>();
        damagesTrack.forEach((uuid1, chm) -> {
            chm.forEach((uuid2, ad) -> {
                if(uuid2 == to.getUniqueId()) {
                    result.put(uuid1, ad.get());
                }
            });
        });
        return result;
    }

    public Map<UUID, Double> damageFrom(Player from) {
        Map<UUID, Double> result = new HashMap<>();
        damagesTrack.get(from.getUniqueId()).forEach((uuid, damage) -> {
            result.put(uuid, damage.get());
        });
        return result;
    }

    public List<Localizable> getPlayerPVPRecap(Player showTo) {
        List<Localizable> result = new ArrayList<>();

        Map<UUID, Double> damagefromviewer = damageFrom(showTo);
        Map<UUID, Double> damagetoviewer = damageTo(showTo);

        //
        result.add(new UnlocalizedText(""));

        result.add(Translations.STATS_RECAP_DAMAGE_DAMAGEGIVEN.with(ChatColor.DARK_GREEN));
        damagefromviewer.forEach((uuid, dmg) -> {
            Player resolvePlayer = Bukkit.getPlayer(uuid);
            if(resolvePlayer == null) {
                // Skip -- unresolvable
                return;
            }
            result.add(Translations.STATS_RECAP_DAMAGE_TO
                    .with(ChatColor.AQUA, "  " + ChatColor.GOLD + dmg.toString(), resolvePlayer.getDisplayName()));
        });

        //
        result.add(new UnlocalizedText(""));
        result.add(Translations.STATS_RECAP_DAMAGE_DAMAGETAKEN.with(ChatColor.DARK_RED));
        damagetoviewer.forEach((uuid, dmg) -> {
            Player resolvePlayer = Bukkit.getPlayer(uuid);
            if(resolvePlayer == null) {
                // Skip -- unresolvable
                return;
            }
            result.add(Translations.STATS_RECAP_DAMAGE_FROM
                    .with(ChatColor.AQUA, "  " + ChatColor.GOLD + dmg.toString(), resolvePlayer.getDisplayName()));
        });
        result.add(new UnlocalizedText(""));

        return result;
    }
}
