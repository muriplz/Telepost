package muriplz.telepost.Listeners;

import muriplz.telepost.Telepost;
import muriplz.telepost.commands.PostAPI;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.ArrayList;
import java.util.UUID;

public class onPlayerMove implements Listener {
    public Telepost plugin = Telepost.getInstance();

    @EventHandler(priority = EventPriority.LOWEST)

    public void OnMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Material material = player.getLocation().getBlock().getType();

        ArrayList<UUID> blockFall = plugin.blockFall;

        // Prevent first fall damage after teleport
        if(blockFall.contains(player.getUniqueId())){
            Entity entity = Bukkit.getEntity(player.getUniqueId());
            if(entity==null){
                return;
            }
            if(entity.isOnGround()){
                blockFall.remove(player.getUniqueId());
            }else if(material== Material.WATER||material==Material.LAVA){
                blockFall.remove(player.getUniqueId());
            }
        }
    }
}
