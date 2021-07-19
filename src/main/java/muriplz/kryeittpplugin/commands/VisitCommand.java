package muriplz.kryeittpplugin.commands;

import io.github.niestrat99.advancedteleport.api.ATPlayer;
import io.github.niestrat99.advancedteleport.api.Warp;
import muriplz.kryeittpplugin.KryeitTPPlugin;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
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

            // /v
            if (args.length == 0) {
                PostAPI.sendMessage(player,"&fUse /visit <PostName/PlayerName> to visit a post.");
                return false;
            }

            // Getting the World overworld
            World world = player.getWorld();

            // Checking if the player is on the Overworld, if not stop the command
            if(!world.getName().equals("world")){
                PostAPI.sendActionBarOrChat(player,ChatColor.translateAlternateColorCodes('&',"&cYou have to be in the Overworld to use this command."),plugin);
                return false;
            }

            // For the X origin
            int originX = plugin.getConfig().getInt("post-x-location");

            // For the Z origin
            int originZ = plugin.getConfig().getInt("post-z-location");

            // For the height that the player will be teleported to
            int height;

            // See if the player is inside a post
            if(!player.hasPermission("telepost.visit")){
                if(!PostAPI.isPlayerOnPost(player,plugin)){
                    PostAPI.sendActionBarOrChat(player,ChatColor.translateAlternateColorCodes('&',"&cYou have to be inside a post."),plugin);
                    return false;
                }
            }

            // Getting the Nearest post
            int postX = PostAPI.getNearPost(player.getLocation().getBlockX(),plugin,originX);
            int postZ = PostAPI.getNearPost(player.getLocation().getBlockZ(),plugin,originZ);

            // If the player is not on the ground stop the command
            if(!Objects.requireNonNull(Bukkit.getEntity(player.getUniqueId())).isOnGround()&&!player.hasPermission("telepost.visit")){
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

                    if(warp.getLocation().getBlockX()==postX&&warp.getLocation().getBlockZ()==postZ&&!player.hasPermission("telepost.visit")){
                        PostAPI.sendActionBarOrChat(player,ChatColor.translateAlternateColorCodes('&',"&cYou are already in &6"+warp.getName()+"&c."),plugin);
                        return false;
                    }

                    // Get the location of the post that the player wants to teleport to
                    Location loc = new Location(world, warp.getLocation().getBlockX() + 0.5, 260, warp.getLocation().getBlockZ() + 0.5,player.getLocation().getYaw(),player.getLocation().getPitch());

                    // See if the config has the option set to true, in that case the teleport takes the player to the air
                    if(plugin.getConfig().getBoolean("tp-in-the-air")){
                        height = 265;
                    }else{
                        // If the option is false, teleport them to the first block that is air
                        height = PostAPI.getFirstSolidBlockHeight(loc.getBlockX(),loc.getBlockZ())+2;
                    }

                    Location newlocation = new Location(world, loc.getBlockX() + 0.5, height, loc.getBlockZ() + 0.5,player.getLocation().getYaw(),player.getLocation().getPitch());
                    String message = ChatColor.translateAlternateColorCodes('&',"&fWelcome to &6" + args[0] + "&f.");
                    // Launches a player to the sky (TODO: improve this horrible thing)
                    if(plugin.getConfig().getBoolean("launch-feature")){
                        PostAPI.launchAndTp(player,newlocation,message,plugin);
                    }else{
                        // Teleport without the "launch-feature"
                        player.teleport(newlocation);
                        PostAPI.playSoundAfterTp(player,newlocation);
                        PostAPI.sendActionBarOrChat(player,message,plugin);
                    }
                    if(player.getGameMode()== GameMode.SURVIVAL||player.getGameMode()==GameMode.ADVENTURE){
                        if(plugin.getConfig().getBoolean("tp-in-the-air")){
                            plugin.blockFall.add(player.getUniqueId());
                        }
                    }
                    if(player.isGliding()){
                        player.setGliding(false);
                    }
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
                            PostAPI.sendActionBarOrChat(player,ChatColor.translateAlternateColorCodes('&',"&cYou are already at your home post."),plugin);
                            return false;
                        }

                        // See if the config has the option set to true, in that case the teleport takes the player to the air
                        if(plugin.getConfig().getBoolean("tp-in-the-air")){
                            height = 265;
                        }else{
                            // If the option is false, teleport them to the first block that is air
                            height = PostAPI.getFirstSolidBlockHeight(location.getBlockX(),location.getBlockZ())+2;
                        }

                        Location newlocation = new Location(world, location.getBlockX() + 0.5, height, location.getBlockZ() + 0.5,player.getLocation().getYaw(),player.getLocation().getPitch());
                        String message = ChatColor.translateAlternateColorCodes('&',"&fWelcome to your post.");
                        // Launches a player to the sky
                        if(plugin.getConfig().getBoolean("launch-feature")){
                            PostAPI.launchAndTp(player,newlocation,message,plugin);
                        }else{
                            // Teleport without the "launch-feature"
                            player.teleport(newlocation);
                            PostAPI.playSoundAfterTp(player,newlocation);
                            PostAPI.sendActionBarOrChat(player,message,plugin);
                        }
                        if(player.getGameMode()== GameMode.SURVIVAL||player.getGameMode()==GameMode.ADVENTURE){
                            if(plugin.getConfig().getBoolean("tp-in-the-air")){
                                plugin.blockFall.add(player.getUniqueId());
                            }
                        }
                        if(player.isGliding()){
                            player.setGliding(false);
                        }
                        return true;
                    }else{

                        // Player does not have a home post yet
                        PostAPI.sendActionBarOrChat(player,ChatColor.translateAlternateColorCodes('&',"&cYou do not have a home post yet."),plugin);
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
                            PostAPI.sendActionBarOrChat(player,ChatColor.translateAlternateColorCodes('&',"&cYou are already at &6"+args[0]+"&c's home post."),plugin);
                            return false;
                        }

                        // See if the config has the option set to true, in that case the teleport takes the player to the air
                        if(plugin.getConfig().getBoolean("tp-in-the-air")){
                            height = 265;
                        }else{
                            // If the option is false, teleport them to the first block that is air
                            height = PostAPI.getFirstSolidBlockHeight(location.getBlockX(),location.getBlockZ())+2;
                        }

                        Location newlocation = new Location(world, location.getBlockX() + 0.5, height, location.getBlockZ() + 0.5,player.getLocation().getYaw(),player.getLocation().getPitch());
                        String message = "&fWelcome to &6" + args[0] + "&f's post.";
                        // Launch the player is its true on config.yml
                        if(plugin.getConfig().getBoolean("launch-feature")){
                            PostAPI.launchAndTp(player,newlocation,message,plugin);
                        }else{

                            // "launch-feature" disabled
                            player.teleport(newlocation);
                            PostAPI.playSoundAfterTp(player,newlocation);
                            PostAPI.sendActionBarOrChat(player,message,plugin);
                        }
                        if(player.getGameMode()== GameMode.SURVIVAL||player.getGameMode()==GameMode.ADVENTURE){
                            if(plugin.getConfig().getBoolean("tp-in-the-air")){
                                plugin.blockFall.add(player.getUniqueId());
                            }
                        }
                        if(player.isGliding()){
                            player.setGliding(false);
                        }
                        return true;
                    }else{

                        // Getting the player2 and checking if is online
                        Player player2 = Bukkit.getPlayer(args[0]);
                        assert player2 != null;
                        if(player2.isOnline()) {
                            PostAPI.sendMessage(player,"&6"+player2.getName()+"&c is not online.");
                            return false;
                        }
                        // If you have the right permission node, you can visit a player's post without being invited

                        if(player.hasPermission("telepost.visit.others")) {
                            ATPlayer atPlayer2 = ATPlayer.getPlayer(Objects.requireNonNull(Bukkit.getPlayer(Objects.requireNonNull(Bukkit.getPlayer(args[0])).getUniqueId())));

                            if(atPlayer2.hasHome("home")) {
                                Location location = atPlayer2.getHome("home").getLocation();
                                // See if the config has the option set to true, in that case the teleport takes the player to the air
                                if(plugin.getConfig().getBoolean("tp-in-the-air")){
                                    height = 265;
                                }else{
                                    // If the option is false, teleport them to the first block that is air
                                    height = PostAPI.getFirstSolidBlockHeight(location.getBlockX(),location.getBlockZ())+2;
                                }

                                Location newlocation = new Location(world, location.getBlockX() + 0.5, height, location.getBlockZ() + 0.5,player.getLocation().getYaw(),player.getLocation().getPitch());
                                String message = "&fWelcome to &6" + args[0] + "&f's post.";
                                // Launch the player is its true on config.yml
                                if(plugin.getConfig().getBoolean("launch-feature")){
                                    PostAPI.launchAndTp(player,newlocation,message,plugin);
                                }else{
                                    // "launch-feature" disabled
                                    player.teleport(newlocation);
                                    PostAPI.playSoundAfterTp(player,newlocation);
                                    PostAPI.sendActionBarOrChat(player,message,plugin);
                                }
                                if(player.getGameMode()== GameMode.SURVIVAL||player.getGameMode()==GameMode.ADVENTURE){
                                    if(plugin.getConfig().getBoolean("tp-in-the-air")){
                                        plugin.blockFall.add(player.getUniqueId());
                                    }
                                }
                                if(player.isGliding()){
                                    player.setGliding(false);
                                }
                                return true;

                            }else {
                                PostAPI.sendMessage(player,"&cThe player &6"+args[0]+"&c doesn't have a home post yet.");
                                return false;
                            }
                        }
                        // Player does not have invitation from args[0] player
                        PostAPI.sendActionBarOrChat(player,ChatColor.translateAlternateColorCodes('&',"&6"+args[0]+"&c has not invited you."),plugin);
                        return false;
                    }
                }else{

                    // No post found
                    PostAPI.sendActionBarOrChat(player,ChatColor.translateAlternateColorCodes('&',"&cThat is not a valid post, try another."),plugin);
                    return false;
                }
            }else{

                // Command Usage (This command can't have usage: on plugin.yml because it relies on "return true/false;")
                PostAPI.sendMessage(player,"&fUse /v <PostName/PlayerName> to visit a post.");
            }
        }return false;
    }
}




