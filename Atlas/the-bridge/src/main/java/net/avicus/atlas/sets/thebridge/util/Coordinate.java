package net.avicus.atlas.sets.thebridge.util;

import lombok.Getter;
import lombok.Setter;

public class Coordinate {

    @Getter
    @Setter
    private double x;
    @Getter
    @Setter
    private double y;
    @Getter
    @Setter
    private double z;

    @Getter
    @Setter
    private double yaw;
    @Getter
    @Setter
    private double pitch;

    public Coordinate(double x, double y, double z) {
        this(x, y, z, 0, 0);
    }

    public Coordinate(double x, double y, double z, double yaw, double pitch) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
    }

}
