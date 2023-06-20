package com.kryeit.listeners;

import com.kryeit.Telepost;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class onPlayerMove implements Listener {
    public Telepost plugin = Telepost.getInstance();

    @EventHandler(priority = EventPriority.LOWEST)
    public void OnMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        // Prevent first fall damage after teleport
        if(plugin.blockFall.contains(player.getUniqueId())) {

            Entity entity = Bukkit.getEntity(player.getUniqueId());
            if(entity==null) return;

            Material material = player.getLocation().getBlock().getType();
            if(material== Material.WATER || material==Material.LAVA) {
                plugin.blockFall.remove(player.getUniqueId());
            }

        }
    }

}
