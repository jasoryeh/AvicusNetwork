package net.avicus.magma.database.model.impl;

import lombok.Getter;
import net.avicus.libraries.quest.annotation.Column;
import net.avicus.libraries.quest.annotation.Id;
import net.avicus.libraries.quest.model.Model;

import java.util.Date;

public class RankMember extends Model {

    @Getter
    @Id
    @Column
    private int id;
    @Getter
    @Column(name = "member_id")
    private int userId;
    @Getter
    @Column(name = "rank_id")
    private int rankId;
    @Getter
    @Column(name = "expires_at")
    private Date expiresAt;

    public RankMember(int userId, int rankId, Date expiresAt) {
        this.userId = userId;
        this.rankId = rankId;
        this.expiresAt = expiresAt;
    }

    public RankMember() {

    }
}
