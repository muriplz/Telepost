package com.kryeit.Listeners;

import com.kryeit.Telepost;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.UUID;

public class onKick implements Listener {
    public Telepost instance = Telepost.getInstance();
    public HashMap<UUID,UUID> leashed = Telepost.getInstance().leashed;

    @EventHandler
    public void onKickK(PlayerQuitEvent event) {
        Player p = event.getPlayer();

        if(leashed.containsValue(p.getUniqueId())){
            for(UUID id : leashed.keySet()){
                if(leashed.get(id).equals(p.getUniqueId())){
                    leashed.remove(id);
                }
            }
        }

    }
}
