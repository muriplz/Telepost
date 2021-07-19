package muriplz.kryeittpplugin.commands;

import io.github.niestrat99.advancedteleport.api.ATPlayer;
import muriplz.kryeittpplugin.KryeitTPPlugin;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;


public class HomePostCommand implements CommandExecutor {

    private final KryeitTPPlugin plugin;

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
                PostAPI.sendActionBarOrChat(player,ChatColor.translateAlternateColorCodes('&',"&cYou have to be in the Overworld to use this command."),plugin);
                return false;
            }

            // If the player is not on the ground stop the command
            if(!Objects.requireNonNull(Bukkit.getEntity(player.getUniqueId())).isOnGround()&&!player.hasPermission("telepost.homepost")){
                return false;
            }
            int height;

            // For the X axis
            int originX = plugin.getConfig().getInt("post-x-location");
            int postX = PostAPI.getNearPost(player.getLocation().getBlockX(),plugin,originX);

            // For the Z axis
            int originZ = plugin.getConfig().getInt("post-z-location");
            int postZ = PostAPI.getNearPost(player.getLocation().getBlockZ(),plugin,originZ);

            // If the player is not inside a post and does not have telepost.homepost permission, he won't be teleported
            if(!player.hasPermission("telepost.homepost")){
                if(!PostAPI.isPlayerOnPost(player,plugin)){
                    PostAPI.sendActionBarOrChat(player,ChatColor.translateAlternateColorCodes('&',"&cYou have to be inside a post."),plugin);
                    return false;
                }
            }


            ATPlayer atPlayer = ATPlayer.getPlayer(player);
            if(atPlayer.hasHome("home")) {
                Location location = atPlayer.getHome("home").getLocation();

                // You can't /homepost to the same post you are in, except if you have telepost.homepost permission

                if(location.getBlockX()==postX&&location.getBlockZ()==postZ&&!player.hasPermission("telepost.homepost")){
                    PostAPI.sendActionBarOrChat(player,ChatColor.translateAlternateColorCodes('&',"&cYou are already at your home post."),plugin);
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
                String message = "&fWelcome to your home post.";
                Location newlocation = new Location(world, location.getBlockX() + 0.5, height, location.getBlockZ() + 0.5,player.getLocation().getYaw(),player.getLocation().getPitch());
                PostAPI.launchAndTp(player,newlocation,message,plugin);

                if(player.getGameMode()== GameMode.SURVIVAL||player.getGameMode()==GameMode.ADVENTURE){
                    if(plugin.getConfig().getBoolean("tp-in-the-air")){
                        plugin.blockFall.add(player.getUniqueId());
                    }
                }
                if(player.isGliding()){
                    player.setGliding(false);
                }

                return true;
            } else {
                // Player does not have a homepost
                PostAPI.sendActionBarOrChat(player,ChatColor.translateAlternateColorCodes('&',"&fPlease, set a post with &6/SetPost&f first."),plugin);
            }
            return true;
        }
    }

}
