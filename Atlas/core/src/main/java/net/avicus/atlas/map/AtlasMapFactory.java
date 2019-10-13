package net.avicus.atlas.map;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Sets;
import net.avicus.atlas.Atlas;
import net.avicus.atlas.GameType;
import net.avicus.atlas.SpecificationVersionHistory;
import net.avicus.atlas.countdown.CyclingCountdown;
import net.avicus.atlas.countdown.StartingCountdown;
import net.avicus.atlas.map.author.Minecrafter;
import net.avicus.atlas.map.author.Organization;
import net.avicus.atlas.map.library.MapSource;
import net.avicus.atlas.match.Match;
import net.avicus.atlas.module.elimination.EliminationModule;
import net.avicus.atlas.module.map.CountdownConfig;
import net.avicus.atlas.module.objectives.Objective;
import net.avicus.atlas.module.objectives.ObjectivesModule;
import net.avicus.atlas.module.objectives.lcs.LastCompetitorStanding;
import net.avicus.atlas.module.objectives.lts.LastTeamStanding;
import net.avicus.atlas.module.objectives.score.ScoreObjective;
import net.avicus.atlas.util.xml.XmlElement;
import net.avicus.atlas.util.xml.XmlException;
import net.avicus.compendium.countdown.Countdown;
import net.avicus.magma.game.author.Author;
import net.avicus.magma.util.MapGenre;
import net.avicus.magma.util.Version;
import org.jdom2.Document;

import javax.annotation.Nullable;
import java.net.URL;
import java.util.*;

public class AtlasMapFactory {

    private static final Map<String, Class<? extends Countdown>> SUPPORTED_COUNTDOWNS = ImmutableMap.<String, Class<? extends Countdown>>builder()
            .put("cycle", CyclingCountdown.class)
            .put("start", StartingCountdown.class)
            .build();

    public static Set<AtlasMap.TypeDetector> TYPE_DETECTORS = Sets.newHashSet();

    static {
        TYPE_DETECTORS.add(new AtlasMap.TypeDetector() {
            @Override
            public Optional<MapGenre> detectGenre(Match match) {
                if (match.hasModule(ObjectivesModule.class)) {
                    ObjectivesModule objectives = match.getRequiredModule(ObjectivesModule.class);
                    if (objectives.getScores().size() == objectives.getObjectives().size()) {
                        return Optional.of(MapGenre.TDM);
                    }
                }
                return match.hasModule(EliminationModule.class) ? Optional.of(MapGenre.ELIMINATION)
                        : Optional.empty();
            }

            @Override
            public Set<GameType> detectGameTypes(Match match) {
                Set<GameType> types = Sets.newHashSet();

                match.getModule(ObjectivesModule.class).ifPresent(objectives -> {
                    for (final Objective objective : objectives.getObjectives()) {
                        if (objective instanceof LastTeamStanding) {
                            types.add(GameType.LTS);
                        } else if (objective instanceof LastCompetitorStanding) {
                            types.add(GameType.LCS);
                        } else if (objective instanceof ScoreObjective) {
                            types.add(GameType.SCORE);
                        }
                    }
                });

                if (match.hasModule(EliminationModule.class)) {
                    types.add(GameType.ELIMINATION);
                }

                return types;
            }
        });
    }

    public static AtlasMap parseAtlas(MapSource source, Document document) {
        final XmlElement root = new XmlElement(document.getRootElement());
        final Version specification = root.getAttribute("spec").asRequiredVersion();
        if (!SpecificationVersionHistory.CURRENT.greaterEqual(specification)) {
            throw new IllegalArgumentException("Map specification '" + specification
                    + "' is higher than current supported specification '"
                    + SpecificationVersionHistory.CURRENT + "'.");
        }

        final String name = root.getAttribute("name").asRequiredString();
        final String slug = root.getAttribute("slug").asString().orElse(AtlasMap.slugify(name));
        final Version version = root.getAttribute("version").asRequiredVersion();

        final MapGenre genre = root.getAttribute("genre").asEnum(MapGenre.class, true).orElse(null);

        List<Author> authors;
        List<Author> contributors;

        if (root.getChild("authors").isPresent()) {
            authors = parseAuthors(root.getChild("authors").get(), false);
            contributors = parseAuthors(root.getChild("authors").get(), true);
        } else {
            throw new XmlException(root, "No authors defined.");
        }

        final EnumSet<GameType> gameTypes = EnumSet.noneOf(GameType.class);
        for (XmlElement element : root.getChildren("gametype")) {
            @Nullable final GameType type = GameType.of(element.getText().asRequiredString());
            if (type != null) {
                gameTypes.add(type);
            }
        }

        CountdownConfig config = parseCountdownConfig(root.getChild("countdowns").orElse(null));

        return new AtlasMap(TYPE_DETECTORS, slug, name, specification, version, genre, authors,
                contributors, gameTypes, config, source, MapSourcePluginType.ARES);
    }

    public static AtlasMap parseAres(MapSource source, Document document) {
        final XmlElement root = new XmlElement(document.getRootElement());
        // TODO: Work on this.
        Version spec = root.getAttribute("proto").asRequiredVersion();
        if (spec.greaterEqual(new Version(1, 3, 0))) {
            spec = SpecificationVersionHistory.CURRENT;
        } else {
            throw new IllegalArgumentException("Map specification '" + spec
                    + "' is higher than current supported Project Ares specification '"
                    + new Version(1, 3, 0) + "'.");
        }

        final String name = root.getChild("name").get().getText().asRequiredString();
        final String slug = AtlasMap.slugify(name);
        final Version version = root.getChild("version").get().getText().asRequiredVersion();


        final MapGenre genre = root.getAttribute("genre").asEnum(MapGenre.class, true).orElse(null);

        List<Author> authors = new ArrayList<>();
        List<Author> contributors = new ArrayList<>();

        if (root.getChild("authors").isPresent()) {
            //authors = parsePAAuthors(root.getChild("authors").get());
            XmlElement authorsChildren = root.getChild("authors").get();
            for (XmlElement descendant : authorsChildren.getDescendants()) {
                if(descendant.getName().equalsIgnoreCase("author")) {
                    if(descendant.hasAttribute("uuid")) {
                        authors.add(new Minecrafter(descendant.getAttribute("uuid").asRequiredString(),
                                descendant.hasAttribute("contribution")
                                        ? Optional.of(descendant.getAttribute("contribution").asRequiredString()) : Optional.empty(),
                                descendant.hasAttribute("promo") // Promo URL is not part of official Project Ares spec but we add it anyways.
                                        ? Optional.of(descendant.getAttribute("promo").asRequiredURL()) : Optional.empty()));
                    } else {
                        // Project ares does not have a "organization" author type and instead just puts name inside the author/contributor element
                        authors.add(new Organization(descendant.getText().asRequiredString(),
                                descendant.hasAttribute("contribution")
                                        ? Optional.of(descendant.getAttribute("contribution").asRequiredString()) : Optional.empty(),
                                descendant.hasAttribute("promo") // Promo URL is not part of official Project Ares spec but we add it anyways.
                                        ? Optional.of(descendant.getAttribute("promo").asRequiredURL()) : Optional.empty()));
                    }
                }
            }
        } else {
            throw new XmlException(root, "No authors present!");
        }

        if (root.getChild("contributors").isPresent()) {
            //contributors = parsePAContributors(root.getChild("contributors").get());
            XmlElement authorsChildren = root.getChild("contributors").get();
            for (XmlElement descendant : authorsChildren.getDescendants()) {
                if(descendant.getName().equalsIgnoreCase("contributor")) {
                    if(descendant.hasAttribute("uuid")) {
                        contributors.add(new Minecrafter(descendant.getAttribute("uuid").asRequiredString(),
                                descendant.hasAttribute("contribution")
                                        ? Optional.of(descendant.getAttribute("contribution").asRequiredString()) : Optional.empty(),
                                descendant.hasAttribute("promo") // Promo URL is not part of official Project Ares spec but we add it anyways.
                                        ? Optional.of(descendant.getAttribute("promo").asRequiredURL()) : Optional.empty()));
                    } else {
                        // Project ares does not have a "organization" author type and instead just puts name inside the author/contributor element
                        contributors.add(new Organization(descendant.getText().asRequiredString(),
                                descendant.hasAttribute("contribution")
                                        ? Optional.of(descendant.getAttribute("contribution").asRequiredString()) : Optional.empty(),
                                descendant.hasAttribute("promo") // Promo URL is not part of official Project Ares spec but we add it anyways.
                                        ? Optional.of(descendant.getAttribute("promo").asRequiredURL()) : Optional.empty()));
                    }
                }
            }
        } // ignore no contributors

        final EnumSet<GameType> gameTypes = EnumSet.noneOf(GameType.class);
        List<XmlElement> gamemodes = root.getDescendants("gamemode");
        for (XmlElement gamemode : gamemodes) {
            String gm = gamemode.getText().asRequiredString().toLowerCase();
            switch(gm) {
                case "tdm":
                    gameTypes.add(GameType.ELIMINATION);
                case "ctf":
                    //gameTypes.add(GameType.)
                    break;
                case "dtm":
                    gameTypes.add(GameType.DTM);
                case "koth":
                    gameTypes.add(GameType.HILL);
                case "rage":
                    break;
                case "arcade":
                    break;
                case "ffa":
                    gameTypes.add(GameType.SCORE);
                case "ctw":
                    gameTypes.add(GameType.CTW);
                case "dtc":
                    gameTypes.add(GameType.DTC);
                case "ad":
                    break;
                case "blitz":
                    break;
                case "scorebox":
                    gameTypes.add(GameType.SCORE);
                case "gs":
                    break;
                case "mixed":
                    break;
            }

        }

        // not part of official ares spec, but we add this just in case.
        CountdownConfig config = parseCountdownConfig(root.getChild("countdowns").orElse(null));

        AtlasMap atlasMap = new AtlasMap(TYPE_DETECTORS, slug, name, spec, version, genre, authors,
                contributors, gameTypes, config, source, MapSourcePluginType.ATLAS);
        atlasMap.setAtlas(false);
        return atlasMap;
    }

    public static AtlasMap parse(MapSource source, Document document) {
        final XmlElement root = new XmlElement(document.getRootElement());
        /*
         * VectorMC ProjectAres Map compatibility parser start
         */

        // Here we detect map xml type by checking for spec(atlas) and proto(ares)
        Version spec;
        String name;
        MapSourcePluginType type = MapSourcePluginType.detect(document);

        if(type == MapSourcePluginType.ATLAS) {
            // ^^^ Check for main required attributes that are associated with Atlas maps
            spec = root.getAttribute("spec").asRequiredVersion();
            name = root.getAttribute("name").asRequiredString();

            Atlas.get().getLogger().info("Parsing " + name + " as Atlas map.");
            return parseAtlas(source, document);
        } else if(type == MapSourcePluginType.ARES) {
            // ^^ Check for main required attributes of Project Ares maps
            spec = root.getAttribute("proto").asRequiredVersion();
            name = root.getChild("name").get().getText().asRequiredString();

            Atlas.get().getLogger().info("Parsing " + name + " as Ares map.");
            return parseAres(source, document);
        } else { // UNKNOWN, we try regular Atlas type anyways
            Atlas.get().getLogger().info("Unknown map type " + document.getBaseURI() + " will be parsed as Atlas map.");
            return parseAtlas(source, document);
        }

        /*
         * Pagoda ProjectAres Map compatibility parser end
         */
    }

    private static List<Author> parseAuthors(XmlElement element, boolean contributors) {
        List<Author> result = new ArrayList<>();
        for (XmlElement child : element.getDescendants()) {
            boolean contributor = child.getAttribute("contributor").asBoolean().orElse(false);

            if (contributor != contributors) {
                continue;
            }

            if (child.getName().equals("author")) {
                String uuid = child.getAttribute("uuid").asRequiredString().replace("-", "");
                Optional<String> role = Optional.empty();
                if (child.hasAttribute("role")) {
                    role = Optional.of(child.getAttribute("role").asRequiredString());
                }
                Optional<URL> promo = child.getAttribute("promo").asURL();

                result.add(new Minecrafter(uuid, role, promo));
            } else if (child.getName().equals("organization")) {
                String name = child.getAttribute("name").asRequiredString();
                Optional<String> role = Optional.empty();
                if (child.hasAttribute("role")) {
                    role = Optional.of(child.getAttribute("role").asRequiredString());
                }
                Optional<URL> promo = child.getAttribute("promo").asURL();

                result.add(new Organization(name, role, promo));
            } else {
                throw new IllegalArgumentException("Unknown author type.");
            }
        }

        return result;
    }

    private static CountdownConfig parseCountdownConfig(@Nullable final XmlElement element) {
        final CountdownConfig config = new CountdownConfig();
        if (element != null) {
            for (final XmlElement child : element.getChildren()) {
                if (SUPPORTED_COUNTDOWNS.containsKey(child.getName())) {
                    config.addCountdown(SUPPORTED_COUNTDOWNS.get(child.getName()),
                            child.getText().asRequiredDuration());
                }
            }
        }

        return config;
    }
}