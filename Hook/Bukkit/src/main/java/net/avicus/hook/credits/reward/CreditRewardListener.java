package net.avicus.hook.credits.reward;
import com.google.common.collect.Lists;
import org.bukkit.event.Listener;

import java.util.List;

public abstract class CreditRewardListener implements Listener {

    public static List<Class<? extends CreditRewardListener>> CREDIT_LISTENERS = Lists.newArrayList();

    private final CreditRewarder creditRewarder;

    public CreditRewardListener(CreditRewarder creditRewarder) {
        this.creditRewarder = creditRewarder;
    }

}
