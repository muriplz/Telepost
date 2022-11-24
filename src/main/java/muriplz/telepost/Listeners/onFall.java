package muriplz.telepost.Listeners;

import muriplz.telepost.Telepost;
import muriplz.telepost.commands.PostAPI;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class onFall implements Listener {

    @EventHandler(ignoreCancelled = true , priority = EventPriority.HIGHEST)
    public void preventFirstFall(EntityDamageEvent event) {

        if (event.getEntityType() == EntityType.PLAYER) {
            Player player = (Player) event.getEntity();
            if (event.getCause() == EntityDamageEvent.DamageCause.FALL) {
                if(Telepost.getInstance().blockFall.contains(player.getUniqueId().toString())){
                    event.setCancelled(true);
                    Telepost.getInstance().blockFall.remove(player.getUniqueId().toString());
                    PostAPI.playSoundAfterTp(player,player.getLocation());
                }
            }
        }
    }
}


