package net.avicus.magma.database.model.impl;

import lombok.Getter;
import net.avicus.libraries.quest.annotation.Column;
import net.avicus.libraries.quest.annotation.Id;
import net.avicus.libraries.quest.model.Model;

import java.util.Date;

public class PrestigeSeason extends Model {

    @Getter
    @Id
    @Column
    private int id;

    @Getter
    private String name;

    @Getter
    @Column
    private double multiplier;

    @Getter
    @Column(name = "start_at")
    private Date start;

    @Getter
    @Column(name = "end_at")
    private Date end;

    /**
     * Check if this season is ongoing given a certain time.
     */
    public boolean isOngoing(Date date) {
        // if after start time, before end time
        return date.after(this.start) && date.before(this.end);
    }

    /**
     * Check if this season is ongoing at the current time.
     */
    public boolean isOngoing() {
        return isOngoing(new Date());
    }
}
