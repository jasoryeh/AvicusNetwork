package net.avicus.magma.database.model.impl;

import lombok.Getter;
import net.avicus.libraries.quest.annotation.Column;
import net.avicus.libraries.quest.annotation.Id;
import net.avicus.libraries.quest.model.Model;

import java.util.Date;

public class CreditTransaction extends Model {

    @Getter
    @Column
    @Id
    private int id;
    @Getter
    @Column(name = "user_id")
    private int userId;
    @Getter
    @Column
    private int amount;
    /**
     * If the amount was modified from the original or expect amount,
     * weight should be set to a multiplier that reflects why amount
     * is different than what it is typically.
     */
    @Getter
    @Column
    private double weight;
    @Getter
    @Column(name = "created_at")
    private Date createdAt;

    public CreditTransaction(int userId, int amount, double weight, Date createdAt) {
        this.userId = userId;
        this.amount = amount;
        this.weight = weight;
        this.createdAt = createdAt;
    }

    public CreditTransaction() {

    }
}
