package tc.oc.tracker.damage.base;

import com.google.common.base.Preconditions;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.inventory.ItemStack;
import tc.oc.tracker.base.AbstractDamageInfo;
import tc.oc.tracker.damage.MeleeDamageInfo;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class SimpleMeleeDamageInfo extends AbstractDamageInfo implements MeleeDamageInfo {

    private final
    @Nonnull
    Material weaponMaterial;
    private final
    @Nullable
    ItemStack weapon;

    public SimpleMeleeDamageInfo(@Nonnull LivingEntity attacker, @Nullable ItemStack weapon) {
        super(attacker);

        Preconditions.checkNotNull(attacker, "attacker");

        this.weaponMaterial = weapon == null ? Material.AIR : weapon.getType();
        this.weapon = weapon;
    }

    public
    @Nonnull
    LivingEntity getAttacker() {
        return this.resolvedDamager;
    }

    public
    @Nonnull
    Material getWeapon() {
        return this.weaponMaterial;
    }

    public
    @Nullable
    ItemStack getWeaponStack() {
        return this.weapon;
    }

    @Override
    public
    @Nonnull
    DamageCause getDamageCause() {
        return DamageCause.ENTITY_ATTACK;
    }
}
