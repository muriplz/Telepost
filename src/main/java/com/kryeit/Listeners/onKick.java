package com.kryeit.listeners;

import com.kryeit.Telepost;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.UUID;

public class onKick implements Listener {

    @EventHandler
    public void onKickK (PlayerQuitEvent event) {
        Player p = event.getPlayer();
        HashMap<UUID,UUID> leashed = Telepost.getInstance().leashed;
        if (leashed.containsValue(p.getUniqueId())) return;
        for (UUID id : leashed.keySet()) {
            if (leashed.get(id).equals(p.getUniqueId())) {
                leashed.remove(id);
            }
        }

    }
}
