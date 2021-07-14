package muriplz.kryeittpplugin.Listeners;

import muriplz.kryeittpplugin.KryeitTPPlugin;
import muriplz.kryeittpplugin.commands.NearestPostCommand;
import muriplz.kryeittpplugin.commands.PostAPI;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.Objects;

public class onPlayerMove implements Listener {
    private final KryeitTPPlugin plugin;

    public onPlayerMove(KryeitTPPlugin plugin) {
        this.plugin = plugin;
    }
    @EventHandler
    public void OnMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Material material = player.getLocation().getBlock().getType();
        if(Objects.requireNonNull(Bukkit.getEntity(player.getUniqueId())).isOnGround()){
            plugin.blockFall.remove(player.getUniqueId());
        }
        if(material== Material.WATER||material==Material.LAVA){
            plugin.blockFall.remove(player.getUniqueId());
        }
        if(plugin.showNearest.contains(player.getUniqueId())){
            int index = plugin.showNearest.indexOf(player.getUniqueId());
            if(plugin.counterNearest.isEmpty()){
                plugin.counterNearest.add(index,1);
            }else if(plugin.counterNearest.get(index)<35){
                plugin.counterNearest.add(index,plugin.counterNearest.get(index)+1);
            }else{

                // get Distance between posts from config.yml
                int gap = plugin.getConfig().getInt("distance-between-posts");
                // for the X axis
                int originX = plugin.getConfig().getInt("post-x-location");
                int postX = PostAPI.getNearPost(gap,player.getLocation().getBlockX(),originX);
                // for the Z axis
                int originZ = plugin.getConfig().getInt("post-z-location");
                int postZ = PostAPI.getNearPost(gap,player.getLocation().getBlockZ(),originZ);

                String postName = NearestPostCommand.NearestPostName(postX,postZ,plugin);
                if(postName!=null){
                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&',"The nearest post is on: &6(" + postX + " , " + postZ + ")&f, it's &6"+postName+"&f.")));
                }else{
                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&',"The nearest post is on: &6(" + postX + " , " + postZ + ")&f.")));
                }
                plugin.counterNearest.add(index,0);
            }
        }
    }
}
