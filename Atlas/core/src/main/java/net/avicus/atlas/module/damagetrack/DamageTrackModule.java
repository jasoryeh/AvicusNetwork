package net.avicus.atlas.module.damagetrack;

import lombok.Getter;
import net.avicus.atlas.match.Match;
import net.avicus.atlas.module.Module;
import net.avicus.atlas.util.AtlasTask;
import net.avicus.atlas.util.Translations;
import net.avicus.compendium.locale.text.Localizable;
import net.avicus.compendium.locale.text.UnlocalizedText;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


public class DamageTrackModule implements Module {
    public static final UUID ENVIRONMENT = new UUID(0, 0);

    @Getter
    private final Match match;

    /**
     * Thread safe map of a list of players the player(key) has damaged.
     * A non-permanent, but temporary storage of damage done to other players.
     */
    @Getter
    private ConcurrentHashMap<UUID, ConcurrentHashMap<UUID, Double>> damagesToOthersTrack;
    /**
     * Thread safe map of a list of players the player(key) has received damage from.
     * A non-permanent, but temporary storage of damage received from other players.
     */
    @Getter
    private ConcurrentHashMap<UUID, ConcurrentHashMap<UUID, Double>> damagesFromOthersTrack;


    public DamageTrackModule(Match match) {
        this.match = match;

        this.damagesToOthersTrack = new ConcurrentHashMap<>();
        this.damagesFromOthersTrack = new ConcurrentHashMap<>();
    }

    @EventHandler
    public void onAttack(EntityDamageByEntityEvent damageEvent) {
        if(!(damageEvent.getEntity() instanceof Player) ||
                !(damageEvent.getDamager() instanceof Player)) {
            if(!(damageEvent.getDamager() instanceof Projectile)) {
                return;
            } else if(damageEvent.getDamager() instanceof Projectile) {
                Projectile projectile = ((Projectile) damageEvent.getDamager());
                if(projectile.getShooter() instanceof Player) {
                    Player attacker = ((Player) projectile.getShooter());
                    Player defender = ((Player) damageEvent.getEntity());
                    double damage = damageEvent.getDamage();

                    AtlasTask.of(() -> this.trackDamage(attacker, defender, damage)).nowAsync();
                    return;
                }
            } else {
                return;
            }
        }

        Player attacker = ((Player) damageEvent.getDamager());
        Player defender = ((Player) damageEvent.getEntity());
        double damage = damageEvent.getDamage();

        AtlasTask.of(() -> this.trackDamage(attacker, defender, damage)).nowAsync();
    }

    public void trackDamage(Player attacker, Player defender, double damageDealt) {
        damageDealt = Math.abs(damageDealt);

        this.ensureTracked(attacker.getUniqueId());
        this.ensureTracked(defender.getUniqueId());

        ConcurrentHashMap<UUID, Double> dmgs = damagesToOthersTrack.get(attacker.getUniqueId());
        ConcurrentHashMap<UUID, Double> rcvd = damagesFromOthersTrack.get(attacker.getUniqueId());

        if(dmgs.contains(defender.getUniqueId())) {
            dmgs.replace(defender.getUniqueId(), dmgs.get(defender.getUniqueId()) + damageDealt);
        } else {
            dmgs.put(defender.getUniqueId(), damageDealt);
        }

        if(rcvd.contains(attacker.getUniqueId())) {
            rcvd.replace(attacker.getUniqueId(), dmgs.get(attacker.getUniqueId()) + damageDealt);
        } else {
            rcvd.put(attacker.getUniqueId(), damageDealt);
        }

        damagesToOthersTrack.put(attacker.getUniqueId(), dmgs);
        damagesFromOthersTrack.put(defender.getUniqueId(), rcvd);
    }

    private void ensureTracked(UUID player) {
        damagesToOthersTrack.put(player, (damagesToOthersTrack.get(player) == null)
                ? new ConcurrentHashMap<>() : damagesToOthersTrack.get(player));
        damagesFromOthersTrack.put(player, (damagesFromOthersTrack.get(player) == null)
                ? new ConcurrentHashMap<>() : damagesFromOthersTrack.get(player));
    }

    @EventHandler
    public void onEnvironmentDamage(EntityDamageEvent damageEvent) {
        if(!(damageEvent.getEntity() instanceof Player)) {
           return;
        }
        if(damageEvent.getCause() == EntityDamageEvent.DamageCause.ENTITY_ATTACK ||
                damageEvent.getCause() == EntityDamageEvent.DamageCause.PROJECTILE) {
            return;
        }

        Player defender = ((Player) damageEvent.getEntity());

        this.ensureTracked(ENVIRONMENT);
        this.ensureTracked(defender.getUniqueId());

        Double damage = Math.abs(damageEvent.getFinalDamage());

        ConcurrentHashMap<UUID, Double> dmgs = damagesToOthersTrack.get(ENVIRONMENT);
        ConcurrentHashMap<UUID, Double> rcvd = damagesFromOthersTrack.get(defender.getUniqueId());

        if(dmgs.contains(defender.getUniqueId())) {
            dmgs.replace(defender.getUniqueId(), dmgs.get(defender.getUniqueId()) + damage);
        } else {
            dmgs.put(defender.getUniqueId(), damage);
        }

        if(rcvd.contains(ENVIRONMENT)) {
            rcvd.replace(ENVIRONMENT, dmgs.get(ENVIRONMENT) + damage);
        } else {
            rcvd.put(ENVIRONMENT, damage);
        }

        damagesToOthersTrack.put(ENVIRONMENT, dmgs);
        damagesFromOthersTrack.put(defender.getUniqueId(), rcvd);
    }

    /**
     * Reset damage tracking for a player, recommended for every death/respawn,
     * will not do it automatically
     * @param reset Player to reset damage tracking
     */
    public void reset(Player reset) {
        damagesToOthersTrack.put(reset.getUniqueId(), new ConcurrentHashMap<>());
        damagesFromOthersTrack.put(reset.getUniqueId(), new ConcurrentHashMap<>());
    }

    /**
     * Get damages done to a player
     * @param to player to find damages dealt to them from others
     * @return map of all players that have attacked the player
     */
    public Map<UUID, Double> getDamageTo(Player to) {
        Map<UUID, Double> result = new HashMap<>();
        damagesFromOthersTrack.get(to.getUniqueId()).forEach((uuid, damage) -> {
            result.put(uuid, damage);
        });
        return result;
    }

    /**
     * Get damages done from a player
     * @param from player to find damages they dealt to others
     * @return map of all players that the player has attacked
     */
    public Map<UUID, Double> getDamageFrom(Player from) {
        Map<UUID, Double> result = new HashMap<>();
        damagesToOthersTrack.get(from.getUniqueId()).forEach((uuid, damage) -> {
            result.put(uuid, damage);
        });
        return result;
    }

    public List<Localizable> getPlayerPVPRecap(Player showTo) {
        List<Localizable> result = new ArrayList<>();

        Map<UUID, Double> damagefromviewer = getDamageFrom(showTo);
        Map<UUID, Double> damagetoviewer = getDamageTo(showTo);

        //

        if(!damagefromviewer.isEmpty()) {
            result.add(new UnlocalizedText(""));
            result.add(Translations.STATS_RECAP_DAMAGE_DAMAGEGIVEN.with(ChatColor.DARK_GREEN));
            damagefromviewer.forEach((uuid, dmg) -> {
                if(uuid == ENVIRONMENT) {
                    result.add(Translations.STATS_RECAP_DAMAGE_TO
                            .with(ChatColor.AQUA, "  " + ChatColor.GOLD + dmg.toString(),
                                    Translations.STATS_RECAP_DAMAGE_ENVIRONMENT.with(ChatColor.AQUA)
                                            .translate(showTo).toLegacyText()));
                }
                Player resolvePlayer = Bukkit.getPlayer(uuid);
                if(resolvePlayer == null) {
                    // Skip -- unresolvable
                    return;
                }
                result.add(Translations.STATS_RECAP_DAMAGE_TO
                        .with(ChatColor.AQUA, "  " + ChatColor.GOLD + dmg.toString(), resolvePlayer.getDisplayName()));
            });
        }


        //
        if(!damagetoviewer.isEmpty()) {
            result.add(new UnlocalizedText(""));
            result.add(Translations.STATS_RECAP_DAMAGE_DAMAGETAKEN.with(ChatColor.DARK_RED));
            damagetoviewer.forEach((uuid, dmg) -> {
                if(uuid == ENVIRONMENT) {
                    result.add(Translations.STATS_RECAP_DAMAGE_FROM
                            .with(ChatColor.AQUA, "  " + ChatColor.GOLD + dmg.toString(),
                                    Translations.STATS_RECAP_DAMAGE_ENVIRONMENT.with(ChatColor.AQUA)
                                            .translate(showTo).toLegacyText()));
                }
                Player resolvePlayer = Bukkit.getPlayer(uuid);
                if(resolvePlayer == null) {
                    // Skip -- unresolvable
                    return;
                }
                result.add(Translations.STATS_RECAP_DAMAGE_FROM
                        .with(ChatColor.AQUA, "  " + ChatColor.GOLD + dmg.toString(), resolvePlayer.getDisplayName()));
            });
        }

        if(!result.isEmpty()) {
            result.add(new UnlocalizedText(""));
        }

        return result;
    }
}
