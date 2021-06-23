package muriplz.kryeittpplugin.commands;

import io.github.niestrat99.advancedteleport.api.ATPlayer;
import io.github.niestrat99.advancedteleport.api.Warp;
import muriplz.kryeittpplugin.KryeitTPPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.*;


public class VisitCommand implements CommandExecutor{

    private final KryeitTPPlugin plugin;

    public VisitCommand(KryeitTPPlugin plugin) {
        this.plugin = plugin;
    }
    //  This commands aims to be /visit in-game
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            Bukkit.getConsoleSender().sendMessage(plugin.name + "You can't execute this command from console.");
            return false;
        } else {

            // Getting the player
            Player player = (Player) sender;

            // Checking if the player is on the Overworld, if not stop the command
            if(!player.getWorld().getName().equals("world")){
                PostAPI.sendMessage(player,"&cYou have to be in the Overworld to use this command.");
                return false;
            }
            // Get distance between posts and the width from config.yml
            int gap = plugin.getConfig().getInt("distance-between-posts");
            int width = (plugin.getConfig().getInt("post-width")-1)/2;

            // For the X axis
            int originX = plugin.getConfig().getInt("post-x-location");
            int postX = PostAPI.getNearPost(gap,player.getLocation().getBlockX(),originX);

            // For the Z axis
            int originZ = plugin.getConfig().getInt("post-z-location");
            int postZ = PostAPI.getNearPost(gap,player.getLocation().getBlockZ(),originZ);

            // For the height that the player will be teleported to
            int height;

            // See if the player is inside a post
            if(!player.hasPermission("telepost.v")){
                if(PostAPI.isPlayerOnPost(player,originX,originZ,width,gap)){
                    PostAPI.sendMessage(player,"&cYou have to be inside a post to use this command, try /nearestpost.");
                    return false;
                }
            }

            // Getting the World overworld
            World world = player.getWorld();

            // /v
            if (args.length == 0) {
                PostAPI.sendMessage(player,"&fUse /v <PostName/PlayerName> to visit a post.");
                return false;
            }

            // If the player is not on the ground stop the command
            if(!Objects.requireNonNull(Bukkit.getEntity(player.getUniqueId())).isOnGround()&&!player.hasPermission("telepost.v")){
                return false;
            }

            // /v <something>
            if (args.length==1){

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

                    if(warp.getLocation().getBlockX()==postX&&warp.getLocation().getBlockZ()==postZ&&!player.hasPermission("telepost.v")){
                        PostAPI.sendMessage(player,"&cYou are already in "+warp.getName()+".");
                        return false;
                    }

                    // Get the location of the post that the player wants to teleport to
                    Location loc = new Location(world, warp.getLocation().getBlockX() + 0.5, 260, warp.getLocation().getBlockZ() + 0.5,player.getLocation().getYaw(),player.getLocation().getPitch());

                    // See if the config has the option set to true, in that case the teleport takes the player to the air
                    if(plugin.getConfig().getBoolean("tp-in-the-air")){
                        height = 265;
                    }else{
                        // If the option is false, teleport them to the first block that is air
                        height = PostAPI.getFirstSolidBlockHeight(loc.getBlockX(),loc.getBlockZ())+1;
                    }

                    // Launches a player to the sky (TODO: improve this horrible thing)
                    if(plugin.getConfig().getBoolean("launch-feature")){
                        player.setVelocity(new Vector(0,4,0));
                        Bukkit.getScheduler().runTaskLater(plugin, () -> player.setVelocity(new Vector (0,2.5,0)), 25L);
                        Bukkit.getScheduler().runTaskLater(plugin, () -> {
                            Location newlocation = new Location(world, loc.getBlockX() + 0.5, height, loc.getBlockZ() + 0.5,player.getLocation().getYaw(),player.getLocation().getPitch());
                            player.teleport(newlocation);
                            player.playSound(newlocation, Sound.ENTITY_DRAGON_FIREBALL_EXPLODE,1f,1f);
                            PostAPI.sendMessage(player, "&7Welcome to " + args[0] + ".");
                        }, 40L);
                    }else{

                        // Teleport without the "launch-feature"
                        Location newlocation = new Location(world, loc.getBlockX() + 0.5, height, loc.getBlockZ() + 0.5,player.getLocation().getYaw(),player.getLocation().getPitch());
                        player.teleport(newlocation);
                        player.playSound(newlocation, Sound.ENTITY_DRAGON_FIREBALL_EXPLODE,1f,1f);
                        PostAPI.sendMessage(player,"&7Welcome to " + args[0] + ".");
                    }
                    player.setFallDistance(-300.0F);
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

                        if(location.getBlockX()==postX&&location.getBlockZ()==postZ&&!player.hasPermission("telepost.v")){
                            PostAPI.sendMessage(player,"&cYou are already at your home post.");
                            return false;
                        }

                        // See if the config has the option set to true, in that case the teleport takes the player to the air
                        if(plugin.getConfig().getBoolean("tp-in-the-air")){
                            height = 265;
                        }else{
                            // If the option is false, teleport them to the first block that is air
                            height = PostAPI.getFirstSolidBlockHeight(location.getBlockX(),location.getBlockZ())+1;
                        }

                        // Launches a player to the sky (TODO: improve this horrible thing)
                        if(plugin.getConfig().getBoolean("launch-feature")){
                            player.setVelocity(new Vector(0,4,0));
                            Bukkit.getScheduler().runTaskLater(plugin, () -> player.setVelocity(new Vector (0,2.5,0)), 25L);
                            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                                Location newlocation = new Location(world, location.getBlockX() + 0.5, height, location.getBlockZ() + 0.5,player.getLocation().getYaw(),player.getLocation().getPitch());
                                player.teleport(newlocation);
                                player.playSound(newlocation, Sound.ENTITY_DRAGON_FIREBALL_EXPLODE,1f,1f);
                                PostAPI.sendMessage(player,"&7Welcome to your post.");
                            }, 40L);
                        }else{

                            // Teleport without the "launch-feature"
                            Location newlocation = new Location(world, location.getBlockX() + 0.5, height, location.getBlockZ() + 0.5,player.getLocation().getYaw(),player.getLocation().getPitch());
                            player.teleport(newlocation);
                            player.playSound(newlocation, Sound.ENTITY_DRAGON_FIREBALL_EXPLODE,1f,1f);
                            PostAPI.sendMessage(player,"&7Welcome to your post.");
                        }
                        player.setFallDistance(-300.0F);
                        return true;
                    }else{

                        // Player does not have a home post yet
                        PostAPI.sendMessage(player,"&cYou do not have a home post yet, use /setpost to set the nearest post as your home post");
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

                        if(location.getBlockX()==postX&&location.getBlockZ()==postZ&&!player.hasPermission("telepost.v")){
                            PostAPI.sendMessage(player,"&cYou are already at "+args[0]+"'s home post.");
                            return false;
                        }

                        // See if the config has the option set to true, in that case the teleport takes the player to the air
                        if(plugin.getConfig().getBoolean("tp-in-the-air")){
                            height = 265;
                        }else{
                            // If the option is false, teleport them to the first block that is air
                            height = PostAPI.getFirstSolidBlockHeight(location.getBlockX(),location.getBlockZ())+1;
                        }

                        // Launch the player is its true on config.yml
                        if(plugin.getConfig().getBoolean("launch-feature")){
                            player.setVelocity(new Vector(0,4,0));
                            Bukkit.getScheduler().runTaskLater(plugin, () -> player.setVelocity(new Vector (0,2.5,0)), 25L);
                            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                                Location newlocation = new Location(world, location.getBlockX() + 0.5, height, location.getBlockZ() + 0.5,player.getLocation().getYaw(),player.getLocation().getPitch());
                                player.teleport(newlocation);
                                player.playSound(newlocation, Sound.ENTITY_DRAGON_FIREBALL_EXPLODE,1f,1f);
                                PostAPI.sendMessage(player,"&7Welcome to " + args[0] + "'s post.");
                            }, 40L);
                        }else{

                            // "launch-feature" disabled
                            Location newlocation = new Location(world, location.getBlockX() + 0.5, height, location.getBlockZ() + 0.5,player.getLocation().getYaw(),player.getLocation().getPitch());
                            player.teleport(newlocation);
                            player.playSound(newlocation, Sound.ENTITY_DRAGON_FIREBALL_EXPLODE,1f,1f);
                            PostAPI.sendMessage(player,"&7Welcome to " + args[0] + "'s post.");
                        }
                        player.setFallDistance(-300.0F);
                        return true;
                    }else{
                        // Player does not have invitation from args[0] player
                        PostAPI.sendMessage(player,"&c"+args[0]+" has not invited you.");
                        return false;
                    }
                }else{

                    // No post found
                    PostAPI.sendMessage(player,"&cThat is not a valid post, try another.");
                    return false;
                }
            }else{

                // Command Usage (This command can't have usage: on plugin.yml because it relies on "return true/false;")
                PostAPI.sendMessage(player,"&fUse /v <PostName/PlayerName> to visit a post.");
            }
        }return false;
    }}




