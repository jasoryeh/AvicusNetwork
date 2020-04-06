package net.avicus.magma.database.model.impl;

import lombok.Getter;
import lombok.ToString;
import net.avicus.libraries.quest.annotation.Column;
import net.avicus.libraries.quest.annotation.Id;
import net.avicus.libraries.quest.model.Model;

import java.util.Date;

@ToString
public class Death extends Model {

    @Getter
    @Id
    @Column
    int id;
    @Getter
    @Column(name = "user_id")
    private int userId;
    @Getter
    @Column(name = "cause")
    private int causeId;
    @Getter
    @Column(name = "created_at")
    private Date date;

    public Death() {

    }

    public Death(int userId, int causeId, Date date) {
        this.userId = userId;
        this.causeId = causeId;
        this.date = date;
    }
}
