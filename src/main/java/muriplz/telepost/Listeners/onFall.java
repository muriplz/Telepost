package muriplz.telepost.Listeners;

import muriplz.telepost.Telepost;
import muriplz.telepost.commands.PostAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
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

    @EventHandler(ignoreCancelled = true , priority = EventPriority.HIGHEST)
    public void preventFirstFall(EntityDamageEvent event) {
        if (event.getCause() == EntityDamageEvent.DamageCause.FALL) {
            Entity e = event.getEntity();

            if (Telepost.getInstance().blockFall.contains(e.getUniqueId().toString())) {

                event.setCancelled(true);

                if(e instanceof Player p){
                    PostAPI.playSoundAfterTp((Player) e, e.getLocation());
                    Entity ee;
                    for (UUID id : leashed.keySet()){
                        if(!leashed.get(id).equals(p.getName())){
                            continue;
                        }
                        ee = Bukkit.getEntity(id);
                        if(ee==null){
                            continue;
                        }
                        if(ee instanceof LivingEntity le){
                            le.setLeashHolder(e);
                            Telepost.getInstance().leashed.remove(id);

                        }
                    }
                }
            }
            if (Telepost.getInstance().leashed.containsKey(e.getUniqueId())) {
                event.setCancelled(true);
            }
        }
    }
}


