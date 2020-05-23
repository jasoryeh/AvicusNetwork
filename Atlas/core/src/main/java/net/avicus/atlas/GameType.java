package net.avicus.atlas;

import javax.annotation.Nullable;

import lombok.Getter;
import net.avicus.atlas.util.Translations;
import net.avicus.compendium.locale.text.LocalizedFormat;

/**
 * Represents each gametype and the translation of the names
 * of each gametype.
 */
public enum GameType {
    CTW(Translations.GAMETYPE_CTW_NAME),
    DTC(Translations.GAMETYPE_DTC_NAME),
    DTM(Translations.GAMETYPE_DTM_NAME),
    ELIMINATION(Translations.GAMETYPE_ELIMINATION_NAME),
    HILL(Translations.GAMETYPE_HILL_NAME),
    LCS(Translations.GAMETYPE_LCS_NAME),
    LTS(Translations.GAMETYPE_LTS_NAME),
    SCORE(Translations.GAMETYPE_SCORE_NAME),
    THE_BRIDGE(Translations.GAMETYPE_THEBRIDGE_NAME),
    WALLS(Translations.GAMETYPE_WALLS_NAME);

    @Getter
    private final LocalizedFormat name;

    GameType(final LocalizedFormat name) {
        this.name = name;
    }

    @Nullable
    public static GameType of(final String string) {
        for (final GameType type : values()) {
            if (type.name().equalsIgnoreCase(string)) {
                return type;
            }
        }
        return null;
    }
}
