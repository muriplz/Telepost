package com.kryeit.listeners;

import com.kryeit.Telepost;
import com.kryeit.commands.PostAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.HashMap;
import java.util.UUID;

public class onFall implements Listener {

    public HashMap<UUID,UUID> leashed = Telepost.getInstance().leashed;

    @EventHandler(priority = EventPriority.HIGHEST)
    public void preventFirstFall(EntityDamageEvent event) {
        Entity e = event.getEntity();
        if (e instanceof Player p) {
            if (!Telepost.getInstance().blockFall.contains(e.getUniqueId())) return;

            event.setCancelled(true);
            Telepost.getInstance().blockFall.remove(p.getUniqueId());
            PostAPI.playSoundAfterTp(p.getLocation());

           Entity entity;
           for (UUID id : leashed.keySet()) {
               if(!leashed.get(id).equals(p.getUniqueId())) continue;

               entity = Bukkit.getEntity(id);
               if(entity == null) continue;
               if(entity instanceof LivingEntity le) {
                   le.setLeashHolder(e);
                   Telepost.getInstance().leashed.remove(id);
               }
           }
        }else if (Telepost.getInstance().blockFall.contains(e.getUniqueId())) {
                event.setCancelled(true);
        }
    }
}


