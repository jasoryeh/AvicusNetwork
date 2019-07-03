package net.avicus.atlas.module.gadget.map;

import com.google.gson.JsonObject;

import lombok.Getter;
import lombok.Setter;
import net.avicus.atlas.Atlas;
import net.avicus.atlas.command.RotationCommands;
import net.avicus.atlas.countdown.CyclingCountdown;
import net.avicus.atlas.event.match.MatchCompleteEvent;
import net.avicus.atlas.map.AtlasMap;
import net.avicus.atlas.map.rotation.Rotation;
import net.avicus.atlas.match.Match;
import net.avicus.atlas.util.AtlasTask;
import net.avicus.atlas.util.Messages;
import net.avicus.compendium.TextStyle;
import net.avicus.compendium.locale.text.UnlocalizedText;
import net.avicus.magma.NetworkIdentification;
import net.avicus.magma.database.model.impl.User;
import net.avicus.magma.module.gadgets.GadgetContext;
import net.avicus.magma.module.gadgets.GadgetManager;
import net.avicus.magma.module.gadgets.Gadgets;
import net.avicus.magma.util.MagmaTranslations;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;

public class MapTokenManager implements GadgetManager<MapSetNextToken, MapTokenContext> {
    private static MapTokenManager INSTANCE;

    private final Gadgets gadgets;

    public static MapTokenManager getInstance() {
        return INSTANCE == null ? new MapTokenManager() : INSTANCE;
    }

    private MapTokenManager() {
        this.gadgets = getGadgets();

        INSTANCE = this;

        // Register gadget manager
        getGadgets().registerManager(this);

        AtlasTask.of(new Runnable() {
            @Override
            public void run() {
                Match peek = MapTokenManager.getInstance().addedMatches.peek();
                if (peek != null) {
                    Atlas.get().getMatchManager().getRotation().getMatch().broadcast(
                            new UnlocalizedText(
                                    ChatColor.LIGHT_PURPLE + "User requested map will be played after this match."
                                            + ChatColor.DARK_AQUA + " Map: "
                                            + ChatColor.GOLD + peek.getMap().getName()
                                            + ChatColor.DARK_AQUA + " by "
                                            + ChatColor.YELLOW + peek.getMap().getAuthors().get(0).getName() + " | "
                                            + ChatColor.BLUE + addedMatches.size() + " more requests."));

                }
            }
        }).repeatAsync(0, 20 * 60 * 2);
    }

    private final String TYPE = "map_token";

    @Override
    public String getType() {
        return TYPE;
    }

    @Override
    public void init() {

    }

    @Override
    public void onAsyncLoad(User user, MapTokenContext context) {

    }

    @Override
    public void onAsyncUnload(User user, MapTokenContext context) {

    }

    @Override
    public void onUse(Player player, MapTokenContext mapToken) {
        player.closeInventory();

        if (mapToken == null) {
            player.sendMessage(
                    MagmaTranslations.ERROR_NO_TOKEN.with(ChatColor.RED, NetworkIdentification.URL + "/shop")
            );
            return;
        }

        get(player, mapToken, gadgets, () -> {
        });
    }

    @Override
    public MapSetNextToken deserializeGadget(JsonObject json) {
        boolean isType = json.get("type").getAsString().equalsIgnoreCase(TYPE);
        return isType ? new MapSetNextToken() : null;
    }

    public static Queue<Match> addedMatches = new ArrayBlockingQueue<Match>(5); // Maximum of 5 items at a time queued

    // Realistically only works when a Match is actually complete e.g. actually breaking all the monuments or capturing all the wools
    public void onMatchComplete(MatchCompleteEvent e) {
        if (addedMatches.isEmpty()) {
            return;
        }

        Match ol = e.getMatch();
        Match ne = addedMatches.poll();

        Rotation rotation = Atlas.get().getMatchManager().getRotation();
        rotation.next(ne, true);
        Optional<org.joda.time.Duration> duration = Optional
                .of(ol.getMap().getCountdownConfig().getDuration(CyclingCountdown.class));
        rotation.cycleMatch(new CyclingCountdown(ol, ne, duration));
    }

    public void chatIn(AsyncPlayerChatEvent e) {
        if (!waitingForRequests.containsKey(e.getPlayer().getUniqueId())) {
            return;
        }

        MapTokenAnvil mapTokenAnvil = waitingForRequests.get(e.getPlayer().getUniqueId());
        if (mapTokenAnvil.isDone()) {
            waitingForRequests.remove(e.getPlayer().getUniqueId());
            return;
        }
        e.setCancelled(true);

        String query = e.getMessage();
        e.getPlayer().sendMessage(ChatColor.YELLOW + "Input: " + ChatColor.WHITE + query);

        if (query.toUpperCase().contains("CANCEL")) {
            waitingForRequests.remove(e.getPlayer().getUniqueId());
            e.getPlayer().sendMessage(new UnlocalizedText("Successfully cancelled setting next map",
                    TextStyle.ofColor(ChatColor.RED)));
            return;
        }

        int sizeBefore = addedMatches.size();
        try {
            Optional<AtlasMap> map = Atlas.get().getMapManager().search(query);

            if (!map.isPresent()) {
                e.getPlayer().sendMessage(Messages.ERROR_MAP_NOT_FOUND.with(ChatColor.RED, query));
                get(mapTokenAnvil.player, mapTokenAnvil.context, mapTokenAnvil.gadgets, mapTokenAnvil.callback);
                return;
            }

            Optional<Match> match = RotationCommands.parse(e.getPlayer(), map.get());
            if (!match.isPresent()) {
                return;
            }

            Rotation rotation = Atlas.get().getMatchManager().getRotation();
            boolean offer = addedMatches.offer(match.get());

            e.getPlayer().sendMessage(ChatColor.YELLOW + "Queued a next map request to: "
                    + ChatColor.WHITE + map.get().getName()
                    + ChatColor.YELLOW + " by "
                    + ChatColor.WHITE + map.get().getAuthors().get(0).getName());

            if (!offer) {
                waitingForRequests.remove(e.getPlayer().getUniqueId());
                e.getPlayer().sendMessage(ChatColor.RED + "Too many map requests at this time. Try again later!");
            } else {
                mapTokenAnvil.setDone(true);

                waitingForRequests.remove(e.getPlayer().getUniqueId());

                mapTokenAnvil.callback.run();
                mapTokenAnvil.gadgets.deleteBackpackGadget(mapTokenAnvil.context);
            }
        } catch (Exception exception) {
            e.getPlayer().sendMessage(ChatColor.RED + "An unexpected error occurred while performing your request.");
            if (sizeBefore > addedMatches.size()) {
                e.getPlayer().sendMessage(ChatColor.AQUA + "We have detected that the rotation was altered, this may mean a successful map set.");
                e.getPlayer().sendMessage(ChatColor.AQUA + "If this is an error contact a developer for your token back.");
                mapTokenAnvil.gadgets.deleteBackpackGadget(mapTokenAnvil.context);
                waitingForRequests.remove(e.getPlayer().getUniqueId());
            }
            exception.printStackTrace();
        }
    }

    public MapTokenAnvil get(Player p, GadgetContext c, Gadgets g, Runnable callback) {
        p.sendMessage(new UnlocalizedText("Please type the map name you want to set. You only get one chance. Type \"cancel\" to cancel doing this."
                , TextStyle.ofColor(ChatColor.GOLD)).translate(p));
        if (waitingForRequests.containsKey(p.getUniqueId())) {
            return waitingForRequests.get(p.getUniqueId());
        }

        return new MapTokenAnvil(p, c, g, callback);
    }

    public static Map<UUID, MapTokenAnvil> waitingForRequests = new HashMap<>();

    public class MapTokenAnvil implements Listener {
        private final Player player;
        private final GadgetContext context;
        private final Gadgets gadgets;
        private final Runnable callback;

        @Getter
        @Setter
        private boolean done;

        public MapTokenAnvil(Player player, GadgetContext context, Gadgets gadgets, Runnable callback) {
            this.player = player;
            this.context = context;
            this.gadgets = gadgets;
            this.callback = callback;

            this.done = false;

            waitingForRequests.put(player.getUniqueId(), this);
        }
    }
}
