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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;


public class VisitCommand implements CommandExecutor{

    private final KryeitTPPlugin plugin;

    public VisitCommand(KryeitTPPlugin plugin) {
        this.plugin = plugin;
    }
    //  This commands aims to be /v in-game
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            Bukkit.getConsoleSender().sendMessage(plugin.name + "You cant execute this command from console.");
            return false;
        } else {
            Player player = (Player) sender;
            if(!player.getWorld().getName().equals("world")){
                PostAPI.sendMessage(player,"&cYou have to be in the Overworld to use this command.");
                return false;
            }
            int gap = plugin.getConfig().getInt("distance-between-posts");
            int originX = plugin.getConfig().getInt("post-x-location");
            int originZ = plugin.getConfig().getInt("post-z-location");
            int playerX = player.getLocation().getBlockX();
            int playerZ = player.getLocation().getBlockZ();
            //for the X axis
            int postX = PostAPI.getNearPost(gap,playerX,originX);
            //for the Z axis
            int postZ = PostAPI.getNearPost(gap,playerZ,originZ);
            int width = (plugin.getConfig().getInt("post-width")-1)/2;
            if(postX>=0&&!player.hasPermission("telepost.v")){
                if(player.getLocation().getBlockX()<postX-width||player.getLocation().getBlockX()>postX+width){
                    PostAPI.sendMessage(player,"&cYou have to be inside a post to use this command, try /nearestpost.");
                    return false;
                }
            }
            if(postX<0&&!player.hasPermission("telepost.v")){
                if(player.getLocation().getBlockX()>postX+width||player.getLocation().getBlockX()<postX-width){
                    PostAPI.sendMessage(player,"&cYou have to be inside a post to use this command, try /nearestpost.");
                    return false;
                }
            }
            if(postZ>=0&&!player.hasPermission("telepost.v")){
                if(player.getLocation().getBlockZ()<postZ-width||player.getLocation().getBlockZ()>postZ+width){
                    PostAPI.sendMessage(player,"&cYou have to be inside a post to use this command, try /nearestpost.");
                    return false;
                }
            }
            if(postZ<0&&!player.hasPermission("telepost.v")){
                if(player.getLocation().getBlockZ()>postZ+width||player.getLocation().getBlockZ()<postZ-width){
                    PostAPI.sendMessage(player,"&cYou have to be inside a post to use this command, try /nearestpost.");
                    return false;
                }
            }
            World world = player.getWorld();
            // /v
            if (args.length == 0) {
                PostAPI.sendMessage(player,"&fUse /v <PostName/PlayerName> to visit a post.");
                return false;
            } // /v <something>
            if (args.length==1){
                // /v <Warp/Named Post>
                HashMap<String, Warp> warps = Warp.getWarps();
                Set<String> warpNames = warps.keySet();
                List<String> allWarpNames = new ArrayList<>(warpNames);
                if(allWarpNames.contains(args[0])){
                    Warp warp = Warp.getWarps().get(args[0]);
                    if(warp.getLocation().getBlockX()==postX&&warp.getLocation().getBlockZ()==postZ){
                        PostAPI.sendMessage(player,"&cYou are already in "+warp.getName()+".");
                        return false;
                    }
                    Location loc=new Location(world, warp.getLocation().getBlockX() + 0.5, 260, warp.getLocation().getBlockZ() + 0.5,player.getLocation().getYaw(),player.getLocation().getPitch());
                    if(plugin.getConfig().getBoolean("launch-feature")){
                        player.setVelocity(new Vector(0,4,0));
                        Bukkit.getScheduler().runTaskLater(plugin, () -> player.setVelocity(new Vector (0,2.5,0)), 25L);
                        Bukkit.getScheduler().runTaskLater(plugin, () -> {
                            Location newlocation = new Location(world, loc.getBlockX() + 0.5, 260, loc.getBlockZ() + 0.5,player.getLocation().getYaw(),player.getLocation().getPitch());
                            player.teleport(newlocation);
                            player.playSound(newlocation, Sound.ENTITY_DRAGON_FIREBALL_EXPLODE,1f,1f);
                            PostAPI.sendMessage(player, "&7Welcome to " + args[0] + ".");
                        }, 40L);
                    }else{
                        Location newlocation = new Location(world, loc.getBlockX() + 0.5, 260, loc.getBlockZ() + 0.5,player.getLocation().getYaw(),player.getLocation().getPitch());
                        player.teleport(newlocation);
                        player.playSound(newlocation, Sound.ENTITY_DRAGON_FIREBALL_EXPLODE,1f,1f);
                        PostAPI.sendMessage(player,"&7Welcome to " + args[0] + ".");
                    }
                    return true;
                }
                // /v <Yourself>
                if(player.getName().equals(args[0])){
                    ATPlayer atPlayer = ATPlayer.getPlayer(player);
                    if(atPlayer.hasHome("home")){
                        Location location = atPlayer.getHome("home").getLocation();
                        if(location.getBlockX()==postX&&location.getBlockZ()==postZ){
                            PostAPI.sendMessage(player,"&cYou are already at your home post.");
                            return false;
                        }
                        if(plugin.getConfig().getBoolean("launch-feature")){
                            player.setVelocity(new Vector(0,4,0));
                            Bukkit.getScheduler().runTaskLater(plugin, () -> player.setVelocity(new Vector (0,2.5,0)), 25L);
                            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                                Location newlocation = new Location(world, location.getBlockX() + 0.5, 260, location.getBlockZ() + 0.5,player.getLocation().getYaw(),player.getLocation().getPitch());
                                player.teleport(newlocation);
                                player.playSound(newlocation, Sound.ENTITY_DRAGON_FIREBALL_EXPLODE,1f,1f);
                                PostAPI.sendMessage(player,"&7Welcome to your post.");
                            }, 40L);
                        }else{
                            Location newlocation = new Location(world, location.getBlockX() + 0.5, 260, location.getBlockZ() + 0.5,player.getLocation().getYaw(),player.getLocation().getPitch());
                            player.teleport(newlocation);
                            player.playSound(newlocation, Sound.ENTITY_DRAGON_FIREBALL_EXPLODE,1f,1f);
                            PostAPI.sendMessage(player,"&7Welcome to your post.");
                        }
                        return true;
                    }else{
                        PostAPI.sendMessage(player,"&cYou do not have a home post yet, use /setpost to set the nearest post as your home post");
                        return false;
                    }
                }
                // /visit <Player post>
                if(!(Bukkit.getPlayer(args[0])==null)){
                    ATPlayer atPlayer = ATPlayer.getPlayer(player);
                    // Check if the sender has the home created named by the invitor
                    if (atPlayer.hasHome(args[0])) {
                        Location location = atPlayer.getHome(args[0]).getLocation();
                        if(location.getBlockX()==postX&&location.getBlockZ()==postZ){
                            PostAPI.sendMessage(player,"&cYou are already at "+args[0]+"'s home post.");
                            return false;
                        }
                        if(plugin.getConfig().getBoolean("launch-feature")){
                            player.setVelocity(new Vector(0,4,0));
                            Bukkit.getScheduler().runTaskLater(plugin, () -> player.setVelocity(new Vector (0,2.5,0)), 25L);
                            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                                Location newlocation = new Location(world, location.getBlockX() + 0.5, 260, location.getBlockZ() + 0.5,player.getLocation().getYaw(),player.getLocation().getPitch());
                                player.teleport(newlocation);
                                player.playSound(newlocation, Sound.ENTITY_DRAGON_FIREBALL_EXPLODE,1f,1f);
                                PostAPI.sendMessage(player,"&7Welcome to " + args[0] + "'s post.");
                            }, 40L);
                        }else{
                            Location newlocation = new Location(world, location.getBlockX() + 0.5, 260, location.getBlockZ() + 0.5,player.getLocation().getYaw(),player.getLocation().getPitch());
                            player.teleport(newlocation);
                            player.playSound(newlocation, Sound.ENTITY_DRAGON_FIREBALL_EXPLODE,1f,1f);
                            PostAPI.sendMessage(player,"&7Welcome to " + args[0] + "'s post.");
                        }
                        return true;
                    }else{
                        PostAPI.sendMessage(player,"&c"+args[0]+" has not invited you.");
                        return false;
                    }
                }else{
                    PostAPI.sendMessage(player,"&cThat is not a valid post, try another.");
                    return false;
                }
            }else{
                PostAPI.sendMessage(player,"&fUse /v <PostName/PlayerName> to visit a post.");
            }
        }return false;
    }}




