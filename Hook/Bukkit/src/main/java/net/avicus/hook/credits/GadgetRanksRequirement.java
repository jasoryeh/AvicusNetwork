package net.avicus.hook.credits;

import com.google.common.base.Joiner;
import net.avicus.compendium.locale.text.Localizable;
import net.avicus.compendium.locale.text.UnlocalizedText;
import net.avicus.magma.Magma;
import net.avicus.magma.network.user.Users;
import net.avicus.magma.network.user.rank.BukkitRank;
import net.avicus.magma.network.user.rank.Ranks;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class GadgetRanksRequirement implements GadgetPurchaseRequirement {

    private final List<String> ranks;

    public GadgetRanksRequirement(List<String> ranks) {
        this.ranks = ranks;
    }

    public GadgetRanksRequirement(String... ranks) {
        this(Arrays.asList(ranks));
    }

    @Override
    public Localizable getText() {
        return new UnlocalizedText("Rank: " + Joiner.on(" or ").join(this.ranks));
    }

    @Override
    public boolean meetsRequirement(Player player) {
        List<BukkitRank> ranks = Ranks.get(Users.user(player));

        for (String require : this.ranks) {
            for (BukkitRank rank : ranks) {
                if (rank.getRank().getName().equalsIgnoreCase(require)) {
                    return true;
                }
            }
        }

        Optional<Permission> permHook = Magma.get().getVaultHook().getPermissions();
        if(permHook.isPresent()) {
            Permission perm = permHook.get();
            String[] playerGroups = perm.getPlayerGroups(player);
            for(String require : this.ranks) {
                for(String group : playerGroups) {
                    if(group.equalsIgnoreCase(require)) {
                        return true;
                    }
                }
            }
        }

        // fallback permission based groups
        for(String require : this.ranks) {
            // all fallback stuff uses the permission 'atlas.fallback' prefix to avoid any potential clashing/duplicates.
            if(player.hasPermission("atlas.fallback.group." + require)) {
                return true;
            }
        }

        return false;
    }
}
