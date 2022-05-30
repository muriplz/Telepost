package muriplz.telepost.commands;

import io.github.niestrat99.advancedteleport.api.ATPlayer;
import muriplz.telepost.Telepost;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;


public class HomePost implements CommandExecutor {

    Telepost plugin = Telepost.getInstance();
    public String worldName = Telepost.getInstance().getConfig().getString("world-name");

    //  This commands aims to be /HomePost in-game
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if( ! ( sender instanceof Player )) {
            Bukkit.getConsoleSender().sendMessage(plugin.name+PostAPI.getMessage("cant-execute-from-console"));
            return false;
        }else {

            // Getting the player and seeing if he's in the overworld
            Player player = (Player) sender;

            // If the command is not /homepost ONLY then return false
            if(args.length!=0){
                player.sendMessage(PostAPI.getMessage("homepost-usage"));
                return false;
            }

            // Check if the player is on the right dimension
            if(!player.getWorld().getName().equals(worldName)){
                player.sendMessage(PostAPI.getMessage("not-on-overworld"));
                return false;
            }

            // If the player is not on the ground stop the command
            if(!Objects.requireNonNull(Bukkit.getEntity(player.getUniqueId())).isOnGround()&&!player.hasPermission("telepost.homepost")){
                return false;
            }
            int height;

            Location nearestPost = PostAPI.getNearPostLocation(player);
            // For the X axis
            int postX = nearestPost.getBlockX();

            // For the Z axis
            int postZ = nearestPost.getBlockZ();

            // If the player is not inside a post and does not have telepost.homepost permission, he won't be teleported
            if(!player.hasPermission("telepost.homepost")){
                if(!PostAPI.isPlayerOnPost(player)){
                    PostAPI.sendActionBarOrChat(player,PostAPI.getMessage("not-inside-post"));
                    return false;
                }
            }

            ATPlayer atPlayer = ATPlayer.getPlayer(player);
            if(atPlayer.hasHome("home")) {
                Location location = atPlayer.getHome("home").getLocation();

                // You can't /homepost to the same post you are in, except if you have telepost.homepost permission

                if(location.getBlockX()==postX&&location.getBlockZ()==postZ&&!player.hasPermission("telepost.homepost")){
                    PostAPI.sendActionBarOrChat(player,PostAPI.getMessage("already-at-homepost"));
                    return false;
                }

                World world = player.getWorld();

                String message = PostAPI.getMessage("own-homepost-arrival");
                Location newlocation = new Location(world, location.getBlockX() + 0.5, Telepost.getInstance().getConfig().getInt("world-height"), location.getBlockZ() + 0.5,player.getLocation().getYaw(),player.getLocation().getPitch());
                PostAPI.launchAndTp(player,newlocation,message);
                return true;
            } else {
                // Player does not have a homepost
                PostAPI.sendActionBarOrChat(player,PostAPI.getMessage("homepost-without-setpost"));
            }
            return true;
        }
    }

}
