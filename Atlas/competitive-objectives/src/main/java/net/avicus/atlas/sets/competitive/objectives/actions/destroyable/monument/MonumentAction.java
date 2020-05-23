package net.avicus.atlas.sets.competitive.objectives.actions.destroyable.monument;

import net.avicus.atlas.sets.competitive.objectives.actions.destroyable.base.DestroyableAction;
import net.avicus.atlas.sets.competitive.objectives.destroyable.monument.MonumentObjective;

public interface MonumentAction extends DestroyableAction {

    MonumentObjective getMonument();
}
