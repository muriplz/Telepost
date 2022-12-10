package muriplz.telepost.commands;

import io.github.niestrat99.advancedteleport.api.ATPlayer;
import io.github.niestrat99.advancedteleport.api.Warp;
import muriplz.telepost.Telepost;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class Visit implements CommandExecutor{

    public Telepost instance = Telepost.getInstance();
    public String worldName = "world";
    //  This commands aims to be /visit in-game
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            Bukkit.getConsoleSender().sendMessage(instance.name + PostAPI.getMessage("cant-execute-from-console"));
            return false;
        } else {

            // Getting the player
            Player player = (Player) sender;

            // If the player is not on the ground stop the command
            if(!Objects.requireNonNull(Bukkit.getEntity(player.getUniqueId())).isOnGround()&&player.getGameMode()!= GameMode.CREATIVE&&player.getGameMode()!=GameMode.SPECTATOR){
                return false;
            }

            // /v
            if (args.length == 0) {
                // Command Usage (This command can't have usage: on plugin.yml because it relies on "return true/false;")
                player.sendMessage(PostAPI.getMessage("visit-usage"));
                return false;
            }

            // Getting the World overworld
            World world = player.getWorld();

            // Checking if the player is on the Overworld, if not stop the command
            if(!world.getName().equals(worldName)){
                player.sendMessage(PostAPI.getMessage("not-on-overworld"));
                return false;
            }

            // See if the player is inside a post
            if(!player.hasPermission("telepost.visit")){
                if(!PostAPI.isPlayerOnPost(player)){
                    PostAPI.sendActionBarOrChat(player,PostAPI.getMessage("not-inside-post"));
                    return false;
                }
            }

            if(PostAPI.hasBlockAbove(player)){
                PostAPI.sendActionBarOrChat(player,PostAPI.getMessage("block-above"));
                return false;
            }

            Location nearestPost = PostAPI.getNearPostLocation(player);
            // For the X axis
            int postX = nearestPost.getBlockX();

            // For the Z axis
            int postZ = nearestPost.getBlockZ();

            String postName = PostAPI.getPostName(args);
            String postID = PostAPI.getPostID(args);

            // /v <something>

            // /v <Named Post>


            List<String> warpNames = new ArrayList<>(Warp.getWarps().keySet());

            int HEIGHT = Telepost.getInstance().getConfig().getInt("world-height");;

            boolean condition = false;

            for(String post : warpNames){
                if(postID.equalsIgnoreCase(post)){
                    condition = true;
                    break;
                }
            }
            // If args[0] is the Name of a post
            if(condition){

                // Get the warp/named post that the player wants to visit
                Location warp = Warp.getWarps().get(postID).getLocation();

                // See if the player want to teleport to the nearest post, only with telepost.v permission you can do this

                if(warp.getBlockX()==postX && warp.getBlockZ()==postZ && !player.hasPermission("telepost.visit")){
                    PostAPI.sendActionBarOrChat(player,PostAPI.getMessage("already-at-namedpost").replace("%POST_NAME%",postName));
                    return false;
                }

                // Get the location of the post that the player wants to teleport to
                Location loc = new Location(world, warp.getBlockX() + 0.5, HEIGHT, warp.getBlockZ() + 0.5,player.getLocation().getYaw(),player.getLocation().getPitch());

                String message = PostAPI.colour(PostAPI.getMessage("named-post-arrival").replace("%POST_NAME%",postName));
                // Launches a player to the sky
                PostAPI.launchAndTp(player,loc,message);

                return true;
            }

            if(args.length ==1){
                // Get the atPlayer
                ATPlayer atPlayer = ATPlayer.getPlayer(player);

                // /v <Yourself> which is the same as /homepost
                if(player.getName().equals(args[0])){

                    // If player already has a home
                    if(atPlayer.hasHome("home")){

                        // Get the home location
                        Location location = atPlayer.getHome("home").getLocation();

                        // See if the player is already at his home post, if he has permission he can teleport
                        if(location.getBlockX()==postX&&location.getBlockZ()==postZ&&!player.hasPermission("telepost.visit")){
                            PostAPI.sendActionBarOrChat(player,PostAPI.getMessage("already-at-homepost"));
                            return false;
                        }

                        Location newlocation = new Location(world, location.getBlockX() + 0.5,HEIGHT, location.getBlockZ() + 0.5,player.getLocation().getYaw(),player.getLocation().getPitch());
                        String message = PostAPI.getMessage("own-homepost-arrival");
                        // Launches a player to the sky
                        PostAPI.launchAndTp(player,newlocation,message);

                        return true;
                    }else{
                        // Player does not have a home post yet
                        player.sendMessage(PostAPI.getMessage("no-homepost"));
                        return false;
                    }
                }
                // /visit <Player>

                // Check if player from (/visit <player>) is actually a player
                if(Bukkit.getPlayer(args[0])==null){

                    OfflinePlayer offlinePlayer = null;
                    if(player.hasPermission("telepost.visit.others")){

                        for(OfflinePlayer p : Bukkit.getOfflinePlayers()){
                            if(Objects.equals(p.getName(), args[0])){
                                offlinePlayer = p;
                            }
                        }
                    }

                    if(offlinePlayer == null){
                        player.sendMessage(PostAPI.getMessage("unknown-post").replace("%POST_NAME%",args[0]));
                        return false;
                    }
                    ATPlayer offlineATPlayer = ATPlayer.getPlayer(offlinePlayer);
                    if(offlineATPlayer.hasHome("home")){
                        // Get the home location
                        Location location = offlineATPlayer.getHome("home").getLocation();

                        Location newlocation = new Location(world, location.getBlockX() + 0.5,HEIGHT, location.getBlockZ() + 0.5,player.getLocation().getYaw(),player.getLocation().getPitch());
                        String message = PostAPI.getMessage("invited-home-arrival").replace("%PLAYER_NAME%",args[0]);
                        // Launch the player is its true on config.yml
                        PostAPI.launchAndTp(player,newlocation,message);
                        return true;

                    }
                }else {
                    // Check if the sender has been invited
                    if (atPlayer.hasHome(args[0])) {
                        Location location = atPlayer.getHome(args[0]).getLocation();

                        // See if he wants to teleport to a post he is already in. If he has permission this has no effect

                        if(location.getBlockX()==postX&&location.getBlockZ()==postZ&&!player.hasPermission("telepost.visit")){
                            PostAPI.sendActionBarOrChat(player,PostAPI.getMessage("already-invited-post"));
                            return false;
                        }

                        Location newlocation = new Location(world, location.getBlockX() + 0.5,HEIGHT, location.getBlockZ() + 0.5,player.getLocation().getYaw(),player.getLocation().getPitch());
                        String message = PostAPI.getMessage("invited-home-arrival").replace("%PLAYER_NAME%",args[0]);
                        // Launch the player is its true on config.yml
                        PostAPI.launchAndTp(player,newlocation,message);
                        return true;
                    }else {
                        player.sendMessage(PostAPI.getMessage("visit-not-invited"));
                    }
                }
            }
        }return false;
    }
}




