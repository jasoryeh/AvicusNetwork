package net.avicus.hook.credits;

import com.google.common.base.Joiner;
import me.lucko.luckperms.LuckPerms;
import me.lucko.luckperms.api.Group;
import me.lucko.luckperms.api.LuckPermsApi;
import me.lucko.luckperms.api.User;
import net.avicus.compendium.locale.text.Localizable;
import net.avicus.compendium.locale.text.UnlocalizedText;
import net.avicus.magma.network.user.Users;
import net.avicus.magma.network.user.rank.BukkitRank;
import net.avicus.magma.network.user.rank.Ranks;
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

        LuckPermsApi api = LuckPerms.getApi();

        User u = api.getUser(player.getUniqueId());
        for (String rank : this.ranks) {
            Optional<Group> groupSafe = api.getGroupSafe(rank);
            if (groupSafe.isPresent() && u.inheritsGroup(groupSafe.get())) {
                return true;
            }
        }

        return false;
    }
}
