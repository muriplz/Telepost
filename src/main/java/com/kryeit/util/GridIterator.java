package com.kryeit.util;

import org.bukkit.Location;
import org.bukkit.World;

import java.util.Iterator;
import java.util.NoSuchElementException;

import static com.kryeit.commands.PostAPI.*;

public class GridIterator implements Iterator<Location> {
    private final World world;
    private final int startX;
    private final int startZ;
    private final int endX;
    private final int endZ;
    private int currentX;
    private int currentZ;

    public GridIterator() {
        this.world = WORLD;
        this.startX = (ORIGIN_X - WORLDBORDER_RADIUS / GAP) * GAP;
        this.startZ = (ORIGIN_Z - WORLDBORDER_RADIUS / GAP) * GAP;
        this.endX = (ORIGIN_X + WORLDBORDER_RADIUS / GAP) * GAP;
        this.endZ = (ORIGIN_Z + WORLDBORDER_RADIUS / GAP) * GAP;
        this.currentX = this.startX;
        this.currentZ = this.startZ;
    }

    @Override
    public boolean hasNext() {
        return currentZ <= endZ;
    }

    @Override
    public Location next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }

        Location location = new Location(world, currentX, BlockFinder.getFirstSolidBlockY(currentX,currentZ), currentZ);

        // Move to the next location in the grid.
        currentX += GAP;
        if (currentX > endX) {
            currentX = startX;
            currentZ += GAP;
        }

        return location;
    }
}