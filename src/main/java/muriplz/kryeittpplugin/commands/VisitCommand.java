package muriplz.kryeittpplugin.commands;

import io.github.niestrat99.advancedteleport.api.ATPlayer;
import io.github.niestrat99.advancedteleport.api.Warp;
import muriplz.kryeittpplugin.KryeitTPPlugin;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;


public class VisitCommand implements CommandExecutor{

    private final KryeitTPPlugin plugin;

    public VisitCommand(KryeitTPPlugin plugin) {
        this.plugin = plugin;
    }
    //  This commands aims to be /v in-game
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            Bukkit.getConsoleSender().sendMessage(plugin.name + ChatColor.WHITE + "You cant execute this command from console.");
            return false;
        } else {
            Player player = (Player) sender;
            if(!player.getWorld().getName().equals("world")){
                player.sendMessage(ChatColor.RED+"You have to be in the Overworld to use this command.");
                return false;
            }
            int gap = plugin.getConfig().getInt("distance-between-posts");
            int originX = plugin.getConfig().getInt("post-x-location");
            int originZ = plugin.getConfig().getInt("post-z-location");
            int playerX = player.getLocation().getBlockX()-originX;
            int playerZ = player.getLocation().getBlockZ()-originZ;
            //para el eje X
            int postX=0;
            while(true){
                if(playerX>=gap && playerX>0){
                    playerX=playerX-gap;
                    postX+=gap;
                }
                else if(playerX<=-gap && playerX<0){
                    playerX=playerX+gap;
                    postX-=gap;
                }
                else{break;}
            }
            if(playerX>gap/2&&playerX>0){
                postX+=gap;
            }
            if(playerX<-gap/2&&playerX<0){
                postX-=gap;
            }
            //para el eje Z
            int postZ=0;
            while(true){
                if(playerZ>=gap && playerZ>0){
                    playerZ=playerZ-gap;
                    postZ+=gap;
                }
                else if(playerZ<=-gap && playerZ<0){
                    playerZ=playerZ+gap;
                    postZ-=gap;
                }
                else{break;}
            }
            if(playerZ>gap/2&&playerZ>0){
                postZ+=gap;
            }
            if(playerZ<-gap/2&&playerZ<0){
                postZ-=gap;
            }
            postX+=originX;
            postZ+=originZ;
            int width = (plugin.getConfig().getInt("post-width")-1)/2;
            if(postX>=0){
                if(player.getLocation().getBlockX()<postX-width||player.getLocation().getBlockX()>postX+width){
                    player.sendMessage(ChatColor.RED+"You have to be inside a post to use this command, try /nearestpost.");
                    return false;
                }
            }
            if(postX<0){
                if(player.getLocation().getBlockX()>postX+width||player.getLocation().getBlockX()<postX-width){
                    player.sendMessage(ChatColor.RED+"You have to be inside a post to use this command, try /nearestpost.");
                    return false;
                }
            }
            if(postZ>=0){
                if(player.getLocation().getBlockZ()<postZ-width||player.getLocation().getBlockZ()>postZ+width){
                    player.sendMessage(ChatColor.RED+"You have to be inside a post to use this command, try /nearestpost.");
                    return false;
                }
            }
            if(postZ<0){
                if(player.getLocation().getBlockZ()>postZ+width||player.getLocation().getBlockZ()<postZ-width){
                    player.sendMessage(ChatColor.RED+"You have to be inside a post to use this command, try /nearestpost.");
                    return false;
                }
            }
            World world = player.getWorld();
            // /v
            if (args.length == 0) {
                player.sendMessage("Use /v <PostName/PlayerName> to visit a post.");
                return false;
            } // /v <something>
            if (args.length==1){
                // /v <Warp/Named Post>
                if( args[0].equals("Gaja") || args[0].equals("Pangea") || args[0].equals("Fossil") || args[0].equals("Agua") || args[0].equals("Magma") || args[0].equals("Trident") || args[0].equals("Seahorse") || args[0].equals("Extremadura") || args[0].equals("Rock") || args[0].equals("Bee")){
                    Warp warp = Warp.getWarps().get(args[0]);
                    Location loc=new Location(world, warp.getLocation().getBlockX() + 0.5, 260, warp.getLocation().getBlockZ() + 0.5,player.getLocation().getYaw(),player.getLocation().getPitch());
                    player.setVelocity(new Vector(0,4,0));
                    Bukkit.getScheduler().runTaskLater(plugin, () -> player.setVelocity(new Vector (0,2.5,0)), 25L);
                    Bukkit.getScheduler().runTaskLater(plugin, () -> {
                        Location newlocation = new Location(world, loc.getBlockX() + 0.5, 260, loc.getBlockZ() + 0.5,player.getLocation().getYaw(),player.getLocation().getPitch());
                        player.teleport(newlocation);
                        player.playSound(newlocation, Sound.ENTITY_DRAGON_FIREBALL_EXPLODE,1f,1f);
                        player.sendMessage(ChatColor.GREEN+"Welcome to your home post.");
                    }, 40L);
                    player.sendMessage(ChatColor.GRAY + "Welcome to " + args[0] + ".");
                    return true;
                }
                // /v <Yourself>
                if(player.getName().equals(args[0])){
                    ATPlayer atPlayer = ATPlayer.getPlayer(player);
                    if(atPlayer.hasHome("home")){
                        Location location = atPlayer.getHome("home").getLocation();
                        player.setVelocity(new Vector(0,4,0));
                        Bukkit.getScheduler().runTaskLater(plugin, () -> player.setVelocity(new Vector (0,2.5,0)), 25L);
                        Bukkit.getScheduler().runTaskLater(plugin, () -> {
                            Location newlocation = new Location(world, location.getBlockX() + 0.5, 260, location.getBlockZ() + 0.5,player.getLocation().getYaw(),player.getLocation().getPitch());
                            player.teleport(newlocation);
                            player.playSound(newlocation, Sound.ENTITY_DRAGON_FIREBALL_EXPLODE,1f,1f);
                            player.sendMessage(ChatColor.GREEN+"Welcome to your home post.");
                        }, 40L);
                        player.sendMessage(ChatColor.GREEN + "Welcome to your home post.");
                        return true;
                    }else{
                        player.sendMessage(ChatColor.RED+"You do not have a home post yet, use /setpost to set the nearest post as your home post");
                        return false;
                    }
                }
                // /visit <Player post>
                if(!(Bukkit.getPlayer(args[0])==null)){
                    ATPlayer atPlayer = ATPlayer.getPlayer(player);
                    // Check if the sender has the home created named by the invitor
                    if (atPlayer.hasHome(args[0])) {
                        Location location = atPlayer.getHome(args[0]).getLocation();
                        player.setVelocity(new Vector(0,4,0));
                        Bukkit.getScheduler().runTaskLater(plugin, () -> player.setVelocity(new Vector (0,2.5,0)), 25L);
                        Bukkit.getScheduler().runTaskLater(plugin, () -> {
                            Location newlocation = new Location(world, location.getBlockX() + 0.5, 260, location.getBlockZ() + 0.5,player.getLocation().getYaw(),player.getLocation().getPitch());
                            player.teleport(newlocation);
                            player.playSound(newlocation, Sound.ENTITY_DRAGON_FIREBALL_EXPLODE,1f,1f);
                            player.sendMessage(ChatColor.GREEN + "Welcome to " + args[0] + "'s home post.");
                        }, 40L);
                        return true;
                    }else{
                        player.sendMessage(ChatColor.RED+args[0]+" has not invited you.");
                        return false;
                    }
                }else{
                    player.sendMessage(ChatColor.RED+"That is not a valid post, try another.");
                    return false;
                }
            }else{
                player.sendMessage("Use /v <PostName/PlayerName> to visit a post.");
            }
        }return false;
    }}




