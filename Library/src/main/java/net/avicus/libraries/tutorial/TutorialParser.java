package net.avicus.libraries.tutorial;

import net.avicus.libraries.tutorial.api.SimpleTutorialStep;
import net.avicus.libraries.tutorial.api.Tutorial;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.github.paperspigot.Title;

import java.util.*;

public class TutorialParser {

    public static Tutorial parseTutorial(ConfigurationSection config) {
        Optional<String> id = Optional.ofNullable(config.getString("id"));
        String name = config.getString("name");
        List<SimpleTutorialStep> steps = new ArrayList<>();

        for (Map<?, ?> stepMap : config.getMapList("steps")) {
            ConfigurationSection stepConfig = new YamlConfiguration();
            stepConfig.createSection("tmp", stepMap);
            stepConfig = stepConfig.getConfigurationSection("tmp");

            steps.add(parseTutorialStep(stepConfig));
        }

        return new Tutorial(id, name, steps);
    }

    public static SimpleTutorialStep parseTutorialStep(ConfigurationSection config) {
        boolean freeze = config.getBoolean("freeze", true);
        boolean clearInventory = config.getBoolean("clear-inventory", true);
        boolean fly = config.getBoolean("fly", false);

        Optional<Double> countdown = parseDouble(config.getString("countdown"));

        Optional<GameMode> gamemode = parseGameMode(config.getString("gamemode"));
        Optional<String> worldName = Optional.ofNullable(config.getString("world"));

        Optional<Vector> position = parseLocation(config.getString("location"));
        Optional<Float> yaw = parseFloat(config.getString("yaw"));
        Optional<Float> pitch = parseFloat(config.getString("pitch"));

        Optional<List<TextComponent>> chat = Optional.empty();
        if (config.contains("chat")) {
            chat = Optional.of(new ArrayList<>());
            for (String line : config.getStringList("chat")) {
                String colored = ChatColor.translateAlternateColorCodes('&', line);
                chat.get().add(new TextComponent(colored));
            }
        }

        Optional<Title> popup = Optional.empty();
        if (config.contains("popup")) {
            String title = colorString(config.getString("popup.title", ""));
            String subtitle = colorString(config.getString("popup.subtitle", ""));
            int fadeIn = secondsToTicks(config.getDouble("popup.fade-in", 0));
            int stay = secondsToTicks(config.getDouble("popup.stay", 3));
            int fadeOut = secondsToTicks(config.getDouble("popup.fade-out", 0));
            popup = Optional.of(new Title(title, subtitle, fadeIn, stay, fadeOut));
        }

        Optional<Map<Integer, ItemStack>> inventory = Optional.empty();
        if (config.contains("inventory")) {
            inventory = Optional.of(new HashMap<>());
            for (String key : config.getConfigurationSection("inventory").getKeys(false)) {
                int slot = Integer.parseInt(key);
                ConfigurationSection itemConfig = config.getConfigurationSection("inventory." + slot);

                Material material = Material
                        .valueOf(itemConfig.getString("type").toUpperCase().replace(" ", "_"));
                int amount = itemConfig.getInt("amount", 1);
                byte data = (byte) itemConfig.getInt("data", 0);

                inventory.get().put(slot, new ItemStack(material, amount, data));
            }
        }

        return new SimpleTutorialStep(
                freeze,
                clearInventory,
                fly,
                countdown,
                gamemode,
                worldName,
                position,
                yaw,
                pitch,
                chat,
                popup,
                inventory
        );
    }

    private static Optional<Double> parseDouble(String text) {
        return text == null ? Optional.empty() : Optional.of(Double.parseDouble(text));
    }

    private static int secondsToTicks(double seconds) {
        return (int) Math.floor(seconds * 20.0);
    }

    private static Optional<Float> parseFloat(String text) {
        return text == null ? Optional.empty() : Optional.of(Float.parseFloat(text));
    }

    private static Optional<Vector> parseLocation(String text) {
        if (text == null) {
            return Optional.empty();
        }

        text = text.replace(" ", "");
        String[] parts = text.split(",");
        if (parts.length != 3) {
            return Optional.empty();
        }

        return Optional.of(new Vector(
                Double.parseDouble(parts[0]),
                Double.parseDouble(parts[1]),
                Double.parseDouble(parts[2])
        ));
    }

    private static String colorString(String text) {
        return text == null ? null : ChatColor.translateAlternateColorCodes('&', text);
    }

    private static Optional<GameMode> parseGameMode(String text) {
        try {
            return Optional.of(GameMode.valueOf(text.toUpperCase()));
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
