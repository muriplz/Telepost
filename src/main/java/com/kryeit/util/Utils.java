package com.kryeit.util;

import org.bukkit.Location;

public class Utils {

    public static long calculateYaw(Location from, Location to) {
        double deltaX = to.getX() - from.getX();
        double deltaZ = to.getZ() - from.getZ();
        double radianAngle = Math.atan2(-deltaX, deltaZ);
        double degreeAngle = Math.toDegrees(radianAngle);

        if (degreeAngle < 0) {
            degreeAngle += 360;
        }

        return Math.round(degreeAngle);
    }

}
