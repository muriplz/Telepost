package muriplz.telepost.leash;

import muriplz.telepost.Telepost;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerLeashEntityEvent;
import org.bukkit.event.player.PlayerUnleashEntityEvent;

import java.util.HashMap;
import java.util.UUID;

public class onLeash implements Listener {

    public HashMap<UUID,UUID> leashed = Telepost.getInstance().leashed;

    @EventHandler
    public void onPlayerLeash(PlayerLeashEntityEvent e){
        leashed.put(e.getEntity().getUniqueId(),e.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onPlayerUnleash(PlayerUnleashEntityEvent e){
        leashed.remove(e.getEntity().getUniqueId());
    }



}
