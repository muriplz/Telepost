package com.kryeit.compat;

import org.bukkit.Bukkit;

import java.util.Optional;
import java.util.function.Supplier;

public enum CompatAddon {
    STRUCTURE_BLOCK_LIB("StructureBlockLib"),
    GRIEF_DEFENDER("GriefDefender");

    private final String id;

    CompatAddon(String id) {
        this.id = id;
    }

    public boolean isLoaded() {
        return Bukkit.getPluginManager().getPlugin(id()) != null;
    }

    public <T> Optional<T> getIfInstalled(Supplier<Supplier<T>> executable) {
        return isLoaded() ? Optional.ofNullable(executable.get().get()) : Optional.empty();
    }

    public void runIfInstalled(Supplier<Runnable> executable) {
        if (isLoaded()) executable.get().run();
    }

    public String id() {
        return id;
    }
}
