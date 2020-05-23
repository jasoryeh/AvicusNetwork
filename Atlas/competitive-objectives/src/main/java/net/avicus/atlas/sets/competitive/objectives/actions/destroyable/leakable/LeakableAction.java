package net.avicus.atlas.sets.competitive.objectives.actions.destroyable.leakable;

import net.avicus.atlas.sets.competitive.objectives.destroyable.leakable.LeakableObjective;
import net.avicus.atlas.sets.competitive.objectives.actions.destroyable.base.DestroyableAction;

public interface LeakableAction extends DestroyableAction {

    LeakableObjective getLeakable();
}
