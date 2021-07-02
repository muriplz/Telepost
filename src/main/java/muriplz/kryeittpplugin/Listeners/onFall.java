package muriplz.kryeittpplugin.Listeners;

import muriplz.kryeittpplugin.KryeitTPPlugin;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class onFall implements Listener {

    private final KryeitTPPlugin plugin;

    public onFall(KryeitTPPlugin plugin) {
        this.plugin = plugin;
    }
    @EventHandler
    public void preventFirstFall(EntityDamageEvent event) {
        if (event.getEntityType() == EntityType.PLAYER) {
            Player player = (Player) event.getEntity();
            if (event.getCause() == EntityDamageEvent.DamageCause.FALL) {
                if (plugin.blockFall.contains(player.getUniqueId())) {
                    event.setCancelled(true);
                    plugin.blockFall.remove(player.getUniqueId());
                }
            }
        }
    }
}


