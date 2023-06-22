package com.kryeit.util;

import org.bukkit.Location;
import org.bukkit.World;

import java.util.Iterator;
import java.util.NoSuchElementException;

import static com.kryeit.commands.PostAPI.*;

public class GridIterator implements Iterator<Location> {

    private int x;
    private int z;
    private int endX;
    private int endZ;
    private World world;

    public GridIterator() {

        this.world = WORLD;
        int worldBorder = WORLDBORDER_RADIUS;
        this.x = ORIGIN_X - worldBorder;
        this.z = ORIGIN_Z - worldBorder;
        this.endX = ORIGIN_X + worldBorder;
        this.endZ = ORIGIN_Z + worldBorder;
    }

    @Override
    public boolean hasNext() {
        return x <= endX && z <= endZ;
    }

    @Override
    public Location next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }

        Location location = new Location(world, x, 319, z);

        x += GAP;
        if (x > endX) {
            x = -endX;
            z += GAP;
        }

        return location;
    }
}
