package muriplz.telepost.Listeners;

import muriplz.telepost.Telepost;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityToggleGlideEvent;

public class onGlide implements Listener {
    public Telepost instance = Telepost.getInstance();

    @EventHandler
    public void preventGliding(EntityToggleGlideEvent event) {
        if (event.getEntityType() == EntityType.PLAYER) {
            Player player = (Player) event.getEntity();
            if(instance.blockFall.contains(player.getUniqueId())){
                event.setCancelled(true);
            }
        }
    }
}
