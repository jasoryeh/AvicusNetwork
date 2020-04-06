package net.avicus.magma.database.model.impl;

import lombok.Getter;
import lombok.ToString;
import net.avicus.libraries.quest.annotation.Column;
import net.avicus.libraries.quest.annotation.Id;
import net.avicus.libraries.quest.model.Model;

import java.util.Date;

@ToString(exclude = "about")
public class Team extends Model {

    @Getter
    @Id
    @Column
    private int id;

    @Getter
    @Column(name = "title")
    private String name;

    @Getter
    @Column
    private String tag;

    @Getter
    @Column
    private String tagline;

    @Getter
    @Column
    private String about;

    @Getter
    @Column(name = "created_at")
    private Date createdAt;
}
