package net.avicus.magma.database.model.impl;

import lombok.Getter;
import net.avicus.libraries.quest.annotation.Column;
import net.avicus.libraries.quest.annotation.Id;
import net.avicus.libraries.quest.model.Model;

import java.util.Date;

@Getter
public class Username extends Model {

    @Id
    @Column
    private int id;

    @Column(name = "user_id")
    private int userId;

    @Column(name = "username")
    private String name;

    @Column(name = "created_at")
    private Date createdAt;

    public Username() {

    }

    /**
     * Creates a new username.
     */
    public Username(int user, String name) {
        this.userId = user;
        this.name = name;
        this.createdAt = new Date();
    }

    @Override
    public String toString() {
        return this.name;
    }
}
