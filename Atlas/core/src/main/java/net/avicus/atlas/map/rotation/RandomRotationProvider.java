package net.avicus.atlas.map.rotation;

import com.google.common.collect.Lists;
import net.avicus.atlas.map.AtlasMap;
import net.avicus.atlas.map.MapManager;
import net.avicus.atlas.match.Match;
import net.avicus.atlas.match.MatchFactory;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class RandomRotationProvider extends AbstractRotationProvider {

    private final MapManager manager;

    public RandomRotationProvider(MapManager mm,
                                  MatchFactory factory) {
        super(mm, factory);
        this.manager = mm;
    }

    @Override
    public Rotation provideRotation() {
        List<Match> matches = Lists.newArrayList();

        LinkedList<? extends AtlasMap> maps = new LinkedList<>(this.manager.getMaps());

        Collections.shuffle(maps);
        int end = maps.size();
        end = end >= 10 ? 10 : end;
        maps.subList(0, end).forEach(m -> matches.add(this.createMatch(m)));

        return new Rotation(matches);
    }
}
