package com.kryeit.storage.bytes;

import org.bukkit.Location;

import java.util.UUID;

public record Home(UUID playerID, Location location) {
    public static Home fromBytes(ReadableByteArray data) {
        return new Home(data.readUUID(), data.readLocation());
    }

    public byte[] toBytes() {
        WritableByteArray data = new WritableByteArray();
        data.writeUUID(playerID());
        data.writeLocation(location());
        return data.toByteArray();
    }
}
