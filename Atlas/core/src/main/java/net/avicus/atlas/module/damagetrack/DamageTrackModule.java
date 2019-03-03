package net.avicus.atlas.module.damagetrack;

import lombok.Getter;
import net.avicus.atlas.event.match.MatchStateChangeEvent;
import net.avicus.atlas.match.Match;
import net.avicus.atlas.module.Module;
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


public class DamageTrackModule implements Module {
    public static final UUID ENVIRONMENT = new UUID(0, 0);

    @Getter
    private final Match match;

    /**
     * List of players the player(key) has damaged.
     * A non-permanent, but temporary storage of damage done to other players.
     */
    @Getter
    private static Map<UUID, Map<UUID, Double>> damagesToOthersTrack = new HashMap<>();
    /**
     * List of players the player(key) has received damage from.
     * A non-permanent, but temporary storage of damage received from other players.
     */
    @Getter
    private static Map<UUID, Map<UUID, Double>> damagesFromOthersTrack = new HashMap<>();


    public DamageTrackModule(Match match) {
        this.match = match;
    }

    @EventHandler
    public void onMatchStateChange(MatchStateChangeEvent stateChangeEvent) {
        if(stateChangeEvent.isChangeToNotPlaying()|| stateChangeEvent.isChangeToPlaying()) {
            // Reset for every time where it changes
            damagesToOthersTrack = new HashMap<>();
            damagesFromOthersTrack = new HashMap<>();
        }
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

                    this.trackDamage(attacker, defender, damage);
                    return;
                }
            } else {
                return;
            }
        }

        Player attacker = ((Player) damageEvent.getDamager());
        Player defender = ((Player) damageEvent.getEntity());
        double damage = damageEvent.getDamage();

        this.trackDamage(attacker, defender, damage);
    }

    public void trackDamage(Player attacker, Player defender, double damageDealt) {
        damageDealt = Math.abs(damageDealt);

        this.ensureTracked(attacker.getUniqueId());
        this.ensureTracked(defender.getUniqueId());

        Map<UUID, Double> dmgs = damagesToOthersTrack.get(attacker.getUniqueId());
        Map<UUID, Double> rcvd = damagesFromOthersTrack.get(attacker.getUniqueId());

        if(dmgs.containsKey(defender.getUniqueId())) {
            double newDmg = dmgs.get(defender.getUniqueId()) + damageDealt;
            dmgs.replace(defender.getUniqueId(), newDmg);
        } else {
            dmgs.put(defender.getUniqueId(), damageDealt);
        }

        if(rcvd.containsKey(attacker.getUniqueId())) {
            double newDmg = rcvd.get(attacker.getUniqueId()) + damageDealt;
            rcvd.replace(attacker.getUniqueId(), newDmg);
        } else {
            rcvd.put(attacker.getUniqueId(), damageDealt);
        }

        damagesToOthersTrack.put(attacker.getUniqueId(), dmgs);
        damagesFromOthersTrack.put(defender.getUniqueId(), rcvd);
    }

    private void ensureTracked(UUID player) {
        damagesToOthersTrack.put(player, (damagesToOthersTrack.get(player) == null)
                ? new HashMap<>() : damagesToOthersTrack.get(player));
        damagesFromOthersTrack.put(player, (damagesFromOthersTrack.get(player) == null)
                ? new HashMap<>() : damagesFromOthersTrack.get(player));
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

        Map<UUID, Double> dmgs = damagesToOthersTrack.get(ENVIRONMENT);
        Map<UUID, Double> rcvd = damagesFromOthersTrack.get(defender.getUniqueId());

        if(dmgs.containsKey(defender.getUniqueId())) {
            double addedDmg = dmgs.get(defender.getUniqueId()) + damage;
            dmgs.replace(defender.getUniqueId(), addedDmg);
        } else {
            dmgs.put(defender.getUniqueId(), damage);
        }

        if(rcvd.containsKey(ENVIRONMENT)) {
            double addedDmg = rcvd.get(ENVIRONMENT) + damage;
            rcvd.replace(ENVIRONMENT, rcvd.get(ENVIRONMENT) + damage);
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
        damagesToOthersTrack.remove(reset.getUniqueId());
        damagesFromOthersTrack.remove(reset.getUniqueId());
        damagesToOthersTrack.put(reset.getUniqueId(), new HashMap<>());
        damagesFromOthersTrack.put(reset.getUniqueId(), new HashMap<>());
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

    private final String SPACER_DOUBLE = "  ";

    public List<Localizable> getPlayerPVPRecap(Player showTo) {
        List<Localizable> result = new ArrayList<>();

        Map<UUID, Double> damagefromviewer = getDamageFrom(showTo);
        Map<UUID, Double> damagetoviewer = getDamageTo(showTo);

        //

        if(!damagefromviewer.isEmpty()) {
            result.add(new UnlocalizedText(""));
            result.add(Translations.STATS_RECAP_DAMAGE_DAMAGEGIVEN.with(ChatColor.GREEN));
            damagefromviewer.forEach((uuid, dmg) -> {
                result.add(
                        Translations.STATS_RECAP_DAMAGE_TO.with(
                                ChatColor.AQUA,
                                damageDisplay(dmg),
                                (uuid == ENVIRONMENT) ?
                                        Translations.STATS_RECAP_DAMAGE_ENVIRONMENT.with(ChatColor.DARK_AQUA).translate(showTo).toLegacyText() :
                                        (Bukkit.getPlayer(uuid) != null) ? Bukkit.getPlayer(uuid).getDisplayName() : "unknown"
                        )
                );
            });
        }


        //
        if(!damagetoviewer.isEmpty()) {
            result.add(new UnlocalizedText(""));
            result.add(Translations.STATS_RECAP_DAMAGE_DAMAGETAKEN.with(ChatColor.RED));
            damagetoviewer.forEach((uuid, dmg) -> {
                result.add(
                        Translations.STATS_RECAP_DAMAGE_FROM.with(
                                ChatColor.AQUA,
                                damageDisplay(dmg),
                                (uuid == ENVIRONMENT) ?
                                        Translations.STATS_RECAP_DAMAGE_ENVIRONMENT.with(ChatColor.DARK_AQUA).translate(showTo).toLegacyText() :
                                        (Bukkit.getPlayer(uuid) != null) ? Bukkit.getPlayer(uuid).getDisplayName() : "unknown"
                        )
                );
            });
        }

        if(!result.isEmpty()) {
            result.add(new UnlocalizedText(""));
        }

        return result;
    }

    public String damageDisplay(Double dmg) {
        // Half this to make it into hearts
        dmg = dmg / 2;

        String display = (dmg).toString();

        // cleanup
        display = display.contains(".0") ? display.replace(".0", "") : "";

        // heart char
        display = ChatColor.GOLD + display + ChatColor.RED + "❤" + (dmg != 1.0 ? "s" : "") + ChatColor.RESET;

        return display;
    }
}
