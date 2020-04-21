package net.avicus.magma.database.model.impl;

import lombok.Getter;
import net.avicus.libraries.quest.annotation.Column;
import net.avicus.libraries.quest.annotation.Id;
import net.avicus.libraries.quest.model.Model;

import java.util.Date;

public class ObjectiveCompletion extends Model {

    @Getter
    @Id
    @Column
    private int id;
    @Getter
    @Column(name = "user_id")
    private int userId;
    @Getter
    @Column(name = "objective_id")
    private int objectiveTypeId;
    @Getter
    @Column(name = "created_at")
    private Date date;

    public ObjectiveCompletion(int userId, int objectiveTypeId, Date date) {
        this.userId = userId;
        this.objectiveTypeId = objectiveTypeId;
        this.date = date;
    }

    public ObjectiveCompletion(int userId, ObjectiveType objectiveType, Date date) {
        this(userId, objectiveType.getId(), date);
    }

    public ObjectiveCompletion() {

    }
}
