package com.kryeit.storage.bytes;

import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

public record Post(String id, String name, Location location) implements Comparable<Post> {
    public static Post fromBytes(String id, ReadableByteArray data) {
        String name = data.readString();
        Location position = data.readLocation();
        return new Post(id, name, position);
    }

    public byte[] toBytes() {
        WritableByteArray data = new WritableByteArray();
        data.writeString(name);
        data.writeLocation(location);
        return data.toByteArray();
    }

    @Override
    public int compareTo(@NotNull Post o) {
        return name().compareTo(o.name());
    }
}
