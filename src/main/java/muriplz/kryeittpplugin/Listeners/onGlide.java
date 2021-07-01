package muriplz.kryeittpplugin.Listeners;

import muriplz.kryeittpplugin.KryeitTPPlugin;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityToggleGlideEvent;

public class onGlide implements Listener {
    private final KryeitTPPlugin plugin;

    public onGlide(KryeitTPPlugin plugin) {
        this.plugin = plugin;
    }
    @EventHandler
    public void entityDamageEvent(EntityToggleGlideEvent event) {
        if (event.getEntityType() == EntityType.PLAYER) {
            Player player = (Player) event.getEntity();
            if(!player.isGliding()&&plugin.blockFall.contains(player)){
                event.setCancelled(true);
            }
        }
    }
}
