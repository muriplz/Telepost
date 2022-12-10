package muriplz.telepost.Listeners;

import muriplz.telepost.Telepost;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.ArrayList;

public class onPlayerMove implements Listener {
    public Telepost plugin = Telepost.getInstance();

    @EventHandler(priority = EventPriority.LOWEST)

    public void OnMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();


        ArrayList<String> blockFall = plugin.blockFall;
        // Prevent first fall damage after teleport
        if(blockFall.contains(player.getUniqueId().toString())){



            Entity entity = Bukkit.getEntity(player.getUniqueId());
            if(entity==null){
                return;
            }
            Material material = player.getLocation().getBlock().getType();
            if(material== Material.WATER||material==Material.LAVA){
                blockFall.remove(player.getUniqueId().toString());
            }
        }
    }
}
