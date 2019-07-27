package net.avicus.atlas.module.damagetrack;

import com.google.common.util.concurrent.AtomicDouble;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.avicus.atlas.match.Match;
import net.avicus.atlas.module.Module;
import net.avicus.atlas.util.Translations;
import net.avicus.compendium.locale.text.Localizable;
import net.avicus.compendium.locale.text.UnlocalizedText;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

public class DamageTrackModule implements Module {
    public static final UUID ENVIRONMENT = new UUID(0, 0);

    @Getter
    private final Match match;

    @Getter
    private List<DamageExchange> damageExchanges;

    public DamageTrackModule(Match match) {
        this.match = match;

        this.damageExchanges = new ArrayList<>();
    }

    public void storeExchange(DamageExchange exchange) {
        this.damageExchanges.add(exchange);
    }

    public List<DamageExchange> getExchanges(UUID forUser) {
        List<DamageExchange> exchanges = new ArrayList<>();
        damageExchanges.forEach(e -> {
            if(e.getMe() == forUser) {
                exchanges.add(e);
            }
        });

        return exchanges;
    }

    public List<DamageExchange> getUntrackedExchanges(UUID forUser) {
        List<DamageExchange> exchanges = this.getExchanges(forUser);

        List<DamageExchange> untracked = new ArrayList<>();

        exchanges.forEach(e -> {
            if(!e.isTracked()) {
                untracked.add(e);
            }
        });

        return untracked;
    }

    public void trackUntracked(UUID forUser) {
        damageExchanges.forEach(e -> {
            if(e.getMe() == forUser) {
                e.setTracked(true);
            }
        });
    }

    public double sumOfExchanges(DamageExchange e) {
        return this.sumOfExchanges(e.getMe(), e.getYou());
    }

    public double sumOfExchanges(UUID me, UUID you) {
        double damage = 0.0;
        for (DamageExchange e : damageExchanges) {
            if (e.getMe() == me && e.getYou() == you) {
                damage += e.getAmount();
            }
        }

        return damage;
    }

    private final String SPACER_DOUBLE = "  ";

    public List<Localizable> getPlayerPVPRecap(Player showTo) {
        List<Localizable> result = new ArrayList<>();

        List<DamageExchange> exchanges = getUntrackedExchanges(showTo.getUniqueId());

        // Damage from the viewer to others, left is who it is to, right is <amount, hits>
        Map<UUID, Pair<AtomicDouble, AtomicLong>> damageFromViewer = new HashMap<>();
        // Damage taken from others to viewer, left is who it is from, right is <amount, hits>
        Map<UUID, Pair<AtomicDouble, AtomicLong>> damageToViewer = new HashMap<>();

        for (DamageExchange exchange : exchanges) {
            if(exchange.getDirection() == DamageDirection.GIVE) {
                if(damageFromViewer.containsKey(exchange.getYou())) {
                    Pair<AtomicDouble, AtomicLong> exEntry = damageFromViewer.get(exchange.getYou());
                    exEntry.getLeft().addAndGet(exchange.getAmount());
                    exEntry.getRight().addAndGet(1);
                } else {
                    damageFromViewer.put(exchange.getYou(), Pair.of(new AtomicDouble(exchange.getAmount()), new AtomicLong(1)));
                }
            } else if(exchange.getDirection() == DamageDirection.RECEIVE) {
                if(damageToViewer.containsKey(exchange.getYou())) {
                    Pair<AtomicDouble, AtomicLong> exEntry = damageToViewer.get(exchange.getYou());
                    exEntry.getLeft().addAndGet(exchange.getAmount());
                    exEntry.getRight().addAndGet(1);
                } else {
                    damageToViewer.put(exchange.getYou(), Pair.of(new AtomicDouble(exchange.getAmount()), new AtomicLong(1)));
                }
            }
        }

        //

        if(!damageFromViewer.isEmpty()) {
            result.add(new UnlocalizedText(""));
            result.add(Translations.STATS_RECAP_DAMAGE_DAMAGEGIVEN.with(ChatColor.GREEN));
            damageFromViewer.forEach((uuid, dmg) -> {
                result.add(
                        Translations.STATS_RECAP_DAMAGE_TO.with(
                                ChatColor.AQUA,
                                damageDisplay(dmg.getLeft().get(), dmg.getRight().get()),
                                (uuid == ENVIRONMENT) ?
                                        Translations.STATS_RECAP_DAMAGE_ENVIRONMENT.with(ChatColor.DARK_AQUA).translate(showTo).toLegacyText() :
                                        (Bukkit.getPlayer(uuid) != null) ? Bukkit.getPlayer(uuid).getDisplayName() : "unknown"
                        )
                );
            });
        }


        //
        if(!damageToViewer.isEmpty()) {
            result.add(new UnlocalizedText(""));
            result.add(Translations.STATS_RECAP_DAMAGE_DAMAGETAKEN.with(ChatColor.RED));
            damageToViewer.forEach((uuid, dmg) -> {
                result.add(
                        Translations.STATS_RECAP_DAMAGE_FROM.with(
                                ChatColor.AQUA,
                                damageDisplay(dmg.getLeft().get(), dmg.getRight().get()),
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

    private static String damageDisplay(Double rawDmg, long hits) {
        // Half this to make it into hearts
        rawDmg = rawDmg / 2;

        String display = (rawDmg).toString();

        // cleanup
        display = display.contains(".0") ? display.replace(".0", "") : display;

        // heart char
        display = ChatColor.GOLD + display + ChatColor.RED + "❤" + (rawDmg != 1.0 ? "s" : "")
                + ChatColor.DARK_GRAY + " x" + hits + ChatColor.RESET;

        return display;
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {

        if(event instanceof EntityDamageByEntityEvent) {
            EntityDamageByEntityEvent damageEvent = ((EntityDamageByEntityEvent) event);

            Player attacker;
            Player defender;
            double damage;

            if(!(damageEvent.getEntity() instanceof Player) || !(damageEvent.getDamager() instanceof Player)) {

                if(damageEvent.getDamager() instanceof  Projectile) {

                    Projectile projectile = ((Projectile) damageEvent.getDamager());
                    if(projectile.getShooter() instanceof Player) {
                        attacker = ((Player) projectile.getShooter());
                        defender = ((Player) damageEvent.getEntity());
                        damage = damageEvent.getFinalDamage();
                    }
                } else {
                    Bukkit.getLogger().info("Untracked e<->e damage: " + damageEvent.getCause());
                }

                return;

            }

            attacker = ((Player) damageEvent.getDamager());
            defender = ((Player) damageEvent.getEntity());
            damage = damageEvent.getDamage();

            DamageExchange de = new DamageExchange(attacker.getUniqueId(), defender.getUniqueId(), damage, DamageDirection.GIVE);

            // Store both directions of damage given
            this.storeExchange(de);
            this.storeExchange(de.flip());
        } else {

            if(!(event.getEntity() instanceof Player)) {
                // Entity taking damage is not player
                return;
            }

            Player player = ((Player) event.getEntity());
            double damage = event.getFinalDamage();

            DamageExchange de = new DamageExchange(player.getUniqueId(), ENVIRONMENT, damage, DamageDirection.RECEIVE);

            this.storeExchange(de);
            this.storeExchange(de.flip());

        }
    }

    @ToString
    public class DamageExchange {
        @Getter
        private final UUID me;
        @Getter
        private final UUID you;
        @Getter
        private final double amount;
        @Getter
        private final DamageDirection direction;

        /**
         * Tracked means it's been seen already//recapped already
         */
        @Getter
        @Setter
        private boolean tracked = false;

        /**
         * Helper variable to stop being rewarded multiple times for assists
         */
        @Getter
        @Setter
        private boolean isCreditRewarded = false;
        @Getter
        @Setter
        private boolean isExperienceRewarded = false;

        public boolean isRewarded() {
            return this.isCreditRewarded && this.isExperienceRewarded;
        }

        @Getter
        private Instant time;

        public DamageExchange(UUID me, UUID you, double amount, DamageDirection direction) {
            this.me = me;
            this.you = you;
            this.amount = amount;
            this.direction = direction;

            this.time = Instant.now();
        }

        private DamageExchange flip;

        public DamageExchange(UUID me, UUID you, double amount, DamageDirection direction, DamageExchange flippedPair) {
            this(me, you, amount, direction);
            this.flip = flippedPair;
        }

        public DamageExchange flip() {
            DamageExchange flipped = flip != null ? flip : new DamageExchange(you, me, amount, direction.invert(), this);
            this.flip = flipped;
            return flipped;
        }
    }

    public enum DamageDirection {
        RECEIVE,
        GIVE;

        public DamageDirection invert() {
            return this == RECEIVE ? GIVE : RECEIVE;
        }
    }

}
