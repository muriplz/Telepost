package com.kryeit.listeners;

import com.kryeit.Telepost;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityToggleGlideEvent;

public class onGlide implements Listener {
    public Telepost instance = Telepost.getInstance();

    @EventHandler(priority = EventPriority.LOWEST)
    public void preventGliding(EntityToggleGlideEvent event) {
        instance.blockFall.remove(event.getEntity().getUniqueId());
    }
}
