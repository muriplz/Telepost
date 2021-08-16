package muriplz.kryeittpplugin.commands;

import io.github.niestrat99.advancedteleport.api.ATPlayer;
import io.github.niestrat99.advancedteleport.api.Warp;
import muriplz.kryeittpplugin.KryeitTPPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.*;


public class VisitCommand implements CommandExecutor{

    public KryeitTPPlugin instance = KryeitTPPlugin.getInstance();

    //  This commands aims to be /visit in-game
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            Bukkit.getConsoleSender().sendMessage(instance.name + PostAPI.getMessage("cant-execute-from-console"));
            return false;
        } else {

            // Getting the player
            Player player = (Player) sender;

            // /v
            if (args.length != 1) {
                // Command Usage (This command can't have usage: on plugin.yml because it relies on "return true/false;")
                player.sendMessage(PostAPI.getMessage("visit-usage"));
                return false;
            }

            // Getting the World overworld
            World world = player.getWorld();

            // Checking if the player is on the Overworld, if not stop the command
            if(!player.getWorld().getName().equals("world")){
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

            Location nearestPost = PostAPI.getNearPostLocation(player);
            // For the X axis
            int postX = nearestPost.getBlockX();

            // For the Z axis
            int postZ = nearestPost.getBlockZ();

            // If the player is not on the ground stop the command
            if(!Objects.requireNonNull(Bukkit.getEntity(player.getUniqueId())).isOnGround()&&!player.hasPermission("telepost.visit")){
                return false;
            }

            // /v <something>

            // /v <Named Post>

            // Get all warps (named posts)
            HashMap<String, Warp> warps = Warp.getWarps();

            // Get all names of named posts and made it into a List
            Set<String> warpNames = warps.keySet();
            List<String> allWarpNames = new ArrayList<>(warpNames);

            // If args[0] is the Name of a post
            if(allWarpNames.contains(args[0])){

                // Get the warp/named post that the player wants to visit
                Warp warp = Warp.getWarps().get(args[0]);

                // See if the player want to teleport to the nearest post, only with telepost.v permission you can do this

                if(warp.getLocation().getBlockX()==postX&&warp.getLocation().getBlockZ()==postZ&&!player.hasPermission("telepost.visit")){
                    PostAPI.sendActionBarOrChat(player,PostAPI.getMessage("already-at-namedpost").replace("%NAMED_POST%",warp.getName()));
                    return false;
                }

                // Get the location of the post that the player wants to teleport to
                Location loc = new Location(world, warp.getLocation().getBlockX() + 0.5, 260, warp.getLocation().getBlockZ() + 0.5,player.getLocation().getYaw(),player.getLocation().getPitch());

                String message = PostAPI.colour(PostAPI.getMessage("named-post-arrival").replace("%NAMED_POST%",args[0]));
                // Launches a player to the sky
                PostAPI.launchAndTp(player,loc,message);

                return true;
            }
            // /v <Yourself> which is the same as /homepost
            if(player.getName().equals(args[0])){

                // Get the atPlayer
                ATPlayer atPlayer = ATPlayer.getPlayer(player);

                // If player already has a home
                if(atPlayer.hasHome("home")){

                    // Get the home location
                    Location location = atPlayer.getHome("home").getLocation();

                    // See if the player is already at his home post, if he has permission he can teleport
                    if(location.getBlockX()==postX&&location.getBlockZ()==postZ&&!player.hasPermission("telepost.visit")){
                        PostAPI.sendActionBarOrChat(player,PostAPI.getMessage("already-at-homepost"));
                        return false;
                    }

                    Location newlocation = new Location(world, location.getBlockX() + 0.5,265, location.getBlockZ() + 0.5,player.getLocation().getYaw(),player.getLocation().getPitch());
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
            // /visit <Player post>

            // Check if player from (/visit <player>) is actually a player
            if(!(Bukkit.getPlayer(args[0])==null)){

                // Get atPlayer for player
                ATPlayer atPlayer = ATPlayer.getPlayer(player);

                // Check if the sender has been invited
                if (atPlayer.hasHome(args[0])) {
                    Location location = atPlayer.getHome(args[0]).getLocation();

                    // See if he wants to teleport to a post he is already in, if he has permission this has no effect

                    if(location.getBlockX()==postX&&location.getBlockZ()==postZ&&!player.hasPermission("telepost.visit")){
                        PostAPI.sendActionBarOrChat(player,PostAPI.getMessage("already-invited-post"));
                        return false;
                    }

                    Location newlocation = new Location(world, location.getBlockX() + 0.5,265, location.getBlockZ() + 0.5,player.getLocation().getYaw(),player.getLocation().getPitch());
                    String message = PostAPI.getMessage("invited-home-arrival").replace("%PLAYER_NAME%",args[0]);
                    // Launch the player is its true on config.yml
                    PostAPI.launchAndTp(player,newlocation,message);
                    return true;
                }else {
                    player.sendMessage(PostAPI.getMessage("visit-not-invited"));
                    return false;

                }
            }else {



            }
        }return false;
    }
}




