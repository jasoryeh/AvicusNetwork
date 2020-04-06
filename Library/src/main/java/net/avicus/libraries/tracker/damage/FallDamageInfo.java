package net.avicus.libraries.tracker.damage;

import net.avicus.libraries.tracker.DamageInfo;

/**
 * Represents a damage where the entity hit the ground.
 */
public interface FallDamageInfo extends DamageInfo {

    /**
     * Gets the distance that the entity fell in order to earn this damage.
     * <p>
     * Contract states that distance will always be greater than or equal to 0.
     *
     * @return Distance fallen in blocks
     */
    float getFallDistance();
}
