package muriplz.kryeittpplugin.commands;

import io.github.niestrat99.advancedteleport.api.ATPlayer;
import muriplz.kryeittpplugin.KryeitTPPlugin;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;


public class HomePostCommand implements CommandExecutor{

    private final KryeitTPPlugin plugin;

    public HomePostCommand(KryeitTPPlugin plugin) {
        this.plugin = plugin;
    }

    //  This commands aims to be /HomePost in-game
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if( ! ( sender instanceof Player )) {
            Bukkit.getConsoleSender().sendMessage(plugin.name+ ChatColor.WHITE+"You cant execute this command from console.");
            return false;
        }else {
            Player player = (Player) sender;
            if(!player.getWorld().getName().equals("world")){
                player.sendMessage(ChatColor.RED+"You have to be in the Overworld to use this command.");
                return false;
            }
            int gap = plugin.getConfig().getInt("distance-between-posts");
            int originX = plugin.getConfig().getInt("post-x-location");
            int originZ = plugin.getConfig().getInt("post-z-location");
            int playerX2 = player.getLocation().getBlockX()-originX;
            int playerZ2 = player.getLocation().getBlockZ()-originZ;
            //para el eje X
            int postX=0;
            while(true){
                if(playerX2>=gap && playerX2>0){
                    playerX2=playerX2-gap;
                    postX+=gap;
                }
                else if(playerX2<=-gap && playerX2<0){
                    playerX2=playerX2+gap;
                    postX-=gap;
                }
                else{break;}
            }
            if(playerX2>gap/2&&playerX2>0){
                postX+=gap;
            }
            if(playerX2<-gap/2&&playerX2<0){
                postX-=gap;
            }
            //para el eje Z
            int postZ=0;
            while(true){
                if(playerZ2>=gap && playerZ2>0){
                    playerZ2=playerZ2-gap;
                    postZ+=gap;
                }
                else if(playerZ2<=-gap && playerZ2<0){
                    playerZ2=playerZ2+gap;
                    postZ-=gap;
                }
                else{break;}
            }
            if(playerZ2>gap/2&&playerZ2>0){
                postZ+=gap;
            }
            if(playerZ2<-gap/2&&playerZ2<0){
                postZ-=gap;
            }
            postX+=originX;
            postZ+=originZ;
            if(postX>=0){
                if(player.getLocation().getBlockX()<postX-2||player.getLocation().getBlockX()>postX+2){
                    player.sendMessage(ChatColor.RED+"You have to be inside a post to use this command, try /nearestpost.");
                    return false;
                }
            }
            if(postX<0){
                if(player.getLocation().getBlockX()<postX+2||player.getLocation().getBlockX()>postX-2){
                    player.sendMessage(ChatColor.RED+"You have to be inside a post to use this command, try /nearestpost.");
                    return false;
                }
            }
            if(postZ>=0){
                if(player.getLocation().getBlockZ()<postZ-2||player.getLocation().getBlockZ()>postZ+2){
                    player.sendMessage(ChatColor.RED+"You have to be inside a post to use this command, try /nearestpost.");
                    return false;
                }
            }
            if(postZ<0){
                if(player.getLocation().getBlockZ()<postZ+2||player.getLocation().getBlockZ()>postZ-2){
                    player.sendMessage(ChatColor.RED+"You have to be inside a post to use this command, try /nearestpost.");
                    return false;
                }
            }
            double playerX, playerZ,playerY;
            ATPlayer atPlayer = ATPlayer.getPlayer(player);
            if(atPlayer.hasHome("home")) {
                Location location = atPlayer.getHome("home").getLocation();
                playerX = location.getBlockX() + 0.5;
                playerZ = location.getBlockZ() + 0.5;
                playerY = location.getBlockY();
                World world = player.getWorld();
                player.setVelocity(new Vector (0,4,0));
                Bukkit.getScheduler().runTaskLater(plugin, () -> player.setVelocity(new Vector (0,2.5,0)), 25L);
                Bukkit.getScheduler().runTaskLater(plugin, () -> {
                    Location newlocation =new Location(world,playerX,playerY,playerZ,player.getLocation().getYaw(),player.getLocation().getPitch());
                    player.teleport(newlocation);
                    player.playSound(newlocation, Sound.ENTITY_DRAGON_FIREBALL_EXPLODE,1f,1f);
                    player.sendMessage(ChatColor.GREEN+"Welcome to your home post.");
                }, 40L);
            }else{
                player.sendMessage(ChatColor.GREEN+"Please, set a post with /SetPost first.");
            }
            return true;
        }
    }

}
