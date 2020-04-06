package net.avicus.magma.database.model.impl;

import lombok.Getter;
import net.avicus.libraries.quest.annotation.Column;
import net.avicus.libraries.quest.annotation.Id;
import net.avicus.libraries.quest.model.Model;

import java.util.ArrayList;
import java.util.List;

public class Announcement extends Model {

    @Getter
    @Id
    @Column
    private int id;

    @Getter
    @Column
    private String body;

    @Getter
    @Column
    private boolean enabled;

    @Getter
    @Column
    private boolean motd;

    @Getter
    @Column(name = "motd_format")
    private boolean motdFormat;

    @Getter
    @Column
    private boolean lobby;

    @Getter
    @Column
    private boolean tips;

    @Getter
    @Column
    private boolean web;

    @Getter
    @Column
    private boolean popup;

    public Announcement(String body) {
        this.body = body;
    }

    public Announcement() {

    }

    public List<Type> getTypes() {
        List<Type> result = new ArrayList<>();
        if (this.motd) {
            result.add(Type.MOTD);
        }
        if (this.motdFormat) {
            result.add(Type.MOTD_FORMAT);
        }
        if (this.tips) {
            result.add(Type.TIPS);
        }
        if (this.lobby) {
            result.add(Type.LOBBY);
        }
        if (this.popup) {
            result.add(Type.POPUP);
        }
        return result;
    }

    public enum Type {
        MOTD_FORMAT,
        MOTD,
        TIPS,
        LOBBY,
        POPUP;

        public String columnName() {
            return name().toLowerCase();
        }
    }
}
