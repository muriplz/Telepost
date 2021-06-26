package muriplz.kryeittpplugin.commands;

import io.github.niestrat99.advancedteleport.api.ATPlayer;
import muriplz.kryeittpplugin.KryeitTPPlugin;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;


public class HomePostCommand implements CommandExecutor{

    private final KryeitTPPlugin plugin;

    public void sendActionBarOrChat(Player player,String message){
        // This will send the message on the action bar, so it looks cooler
        if(plugin.getConfig().getBoolean("send-arrival-messages-on-action-bar")){
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(message));
        }else{
            PostAPI.sendMessage(player,message);
        }
    }

    public HomePostCommand(KryeitTPPlugin plugin) {
        this.plugin = plugin;
    }

    //  This commands aims to be /HomePost in-game
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if( ! ( sender instanceof Player )) {
            Bukkit.getConsoleSender().sendMessage(plugin.name+"You can't execute this command from console.");
            return false;
        }else {

            // Getting the player and seeing if he's in the overworld
            Player player = (Player) sender;

            // If the command is not /setpost ONLY then return false
            if(args.length!=0){
                PostAPI.sendMessage(player,"Use /SetPost.");
                return false;
            }

            // Check if the player is on the right dimension
            if(!player.getWorld().getName().equals("world")){
                sendActionBarOrChat(player,ChatColor.translateAlternateColorCodes('&',"&cYou have to be in the Overworld to use this command."));
                return false;
            }

            // If the player is not on the ground stop the command
            if(!Objects.requireNonNull(Bukkit.getEntity(player.getUniqueId())).isOnGround()&&!player.hasPermission("telepost.homepost")){
                return false;
            }

            // Get distance between posts and width from config.yml
            int height;
            int gap = plugin.getConfig().getInt("distance-between-posts");
            int width = (plugin.getConfig().getInt("post-width")-1)/2;

            // For the X axis
            int originX = plugin.getConfig().getInt("post-x-location");
            int postX = PostAPI.getNearPost(gap,player.getLocation().getBlockX(),originX);

            // For the Z axis
            int originZ = plugin.getConfig().getInt("post-z-location");
            int postZ = PostAPI.getNearPost(gap,player.getLocation().getBlockZ(),originZ);

            // If the player is not inside a post and does not have telepost.homepost permission, he won't be teleported
            if(!player.hasPermission("telepost.homepost")){
                if(PostAPI.isPlayerOnPost(player,originX,originZ,width,gap)){
                    sendActionBarOrChat(player,ChatColor.translateAlternateColorCodes('&',"&cYou have to be inside a post to use this command, try &6/nearestpost&c."));
                    return false;
                }
            }

            ATPlayer atPlayer = ATPlayer.getPlayer(player);
            if(atPlayer.hasHome("home")) {
                Location location = atPlayer.getHome("home").getLocation();

                // You can't /homepost to the same post you are in, except if you have telepost.homepost permission

                if(location.getBlockX()==postX&&location.getBlockZ()==postZ&&!player.hasPermission("telepost.homepost")){
                    sendActionBarOrChat(player,ChatColor.translateAlternateColorCodes('&',"&cYou are already at your home post."));
                    return false;
                }

                World world = player.getWorld();

                // See if the config has the option set to true, in that case the teleport takes the player to the air
                if(plugin.getConfig().getBoolean("tp-in-the-air")){
                    height = 265;
                }else{
                    // If the option is false, teleport them to the first block that is air
                    height = PostAPI.getFirstSolidBlockHeight(location.getBlockX(),location.getBlockZ())+2;
                }


                if (plugin.getConfig().getBoolean("launch-feature")) {
                    player.setVelocity(new Vector(0,4,0));
                    Bukkit.getScheduler().runTaskLater(plugin, () -> player.setVelocity(new Vector (0,2.5,0)), 25L);
                    Bukkit.getScheduler().runTaskLater(plugin, () -> {
                        Location newlocation = new Location(world, location.getBlockX() + 0.5, height, location.getBlockZ() + 0.5,player.getLocation().getYaw(),player.getLocation().getPitch());
                        player.teleport(newlocation);
                        PostAPI.playSoundAfterTp(player,newlocation);
                        String message = ChatColor.translateAlternateColorCodes('&',"&fWelcome to your post.");
                        sendActionBarOrChat(player,message);
                    }, 40L);
                } else {
                    // Teleport player to his home without launch feature
                    Location newlocation = new Location(world, location.getBlockX() + 0.5, height, location.getBlockZ() + 0.5,player.getLocation().getYaw(),player.getLocation().getPitch());
                    player.teleport(newlocation);
                    PostAPI.playSoundAfterTp(player,newlocation);
                    String message = ChatColor.translateAlternateColorCodes('&',"&fWelcome to your post.");
                    sendActionBarOrChat(player,message);
                }


                return true;
            } else {
                // Player does not have a homepost
                sendActionBarOrChat(player,ChatColor.translateAlternateColorCodes('&',"&fPlease, set a post with &6/SetPost&f first."));
            }
            return true;
        }
    }

}
