package net.avicus.magma.database.model.impl;

import lombok.Getter;
import net.avicus.libraries.quest.annotation.Column;
import net.avicus.libraries.quest.annotation.Id;
import net.avicus.libraries.quest.model.Model;

public class ObjectiveType extends Model {

    @Getter
    @Id
    @Column
    private int id;
    @Getter
    @Column
    private String name;

    public ObjectiveType(String name) {
        this.name = name;
    }

    public ObjectiveType() {

    }
}
