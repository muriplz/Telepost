package com.kryeit.util;

import org.bukkit.Location;
import org.bukkit.World;

import java.util.Iterator;
import java.util.NoSuchElementException;

import static com.kryeit.commands.PostAPI.GAP;

public class GridIterator implements Iterator<Location> {

    private int x;
    private int z;
    private int endX;
    private int endZ;
    private World world;

    public GridIterator(World world, int startX, int startZ, int endX, int endZ) {
        this.world = world;
        this.x = startX;
        this.z = startZ;
        this.endX = endX;
        this.endZ = endZ;
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
