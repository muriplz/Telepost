package muriplz.telepost.commands;

import io.github.niestrat99.advancedteleport.api.Warp;
import muriplz.telepost.Telepost;
import muriplz.telepost.leash.LeashAPI;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PostAPI {
// HERE COMES STATIC ABUSE :D
    public Telepost instance = Telepost.getInstance();

    public static int gap = Telepost.getInstance().getConfig().getInt("distance-between-posts");

    public static int HEIGHT = Telepost.getInstance().getConfig().getInt("world-height");
    public static String worldName = "world";
    public static boolean isOnWorld(Player player, String world) {
        return player.getWorld().getName().equalsIgnoreCase(world);
    }

    public static Location getNearPostLocation ( Player player ) {
        // for the X axis
        int originX = Telepost.getInstance().getConfig().getInt("post-x-location");
        int postX = PostAPI.getNearPost(player.getLocation().getBlockX(),originX);

        // for the Z axis
        int originZ = Telepost.getInstance().getConfig().getInt("post-z-location");
        int postZ = PostAPI.getNearPost(player.getLocation().getBlockZ(),originZ);
        return new Location(player.getWorld(),postX,HEIGHT,postZ);
    }

    public static String getPostName(String[] args){
        String s ="";
        for(String word : args){
            s = s.concat(word+" ");

        }
        StringBuilder sb= new StringBuilder(s);
        sb.deleteCharAt(sb.length()-1);
        return sb.toString();
    }

    public static String getPostID(String[] args){
        return getPostName(args).replace(" ",".");
    }





    public static int getNearPost( int playerXorZ,int origin) {

        // Subtracting origin of posts to get correct calculation
        playerXorZ -= origin;

        // Setting the post variable
        float post = playerXorZ;

        // Getting the closest multiple of gap to post
        post = post / gap;
        post = Math.round(post);
        post = post * gap;

        // Adding origin of posts to finish calculation
        post += origin;

        // Returning the number
        return (int) post;
    }

    public static void playSoundAfterTp( Player player , Location location ){
        player.playSound( location , Sound.ENTITY_DRAGON_FIREBALL_EXPLODE , 1f , 1f );
    }

    public static void sendMessage( Player player , String message ){
        player.sendMessage( ChatColor.translateAlternateColorCodes( '&' , message ) );
    }
    public static void sendActionBarOrChat( Player player , String message ){
        // This will send the message on the action bar, if the option is enabled on config.yml
        if(Telepost.getInstance().getConfig().getBoolean("send-arrival-messages-on-action-bar")){
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(message));
        }else{
            player.sendMessage(message);
        }
    }

    public static int getFirstBlockHeight(Player player){
        Location loc = player.getLocation();
        Location aux;
        Block block;
        int i = HEIGHT;
        while(true){

            aux = new Location(loc.getWorld(),loc.getX(),i,loc.getZ());
            block = loc.getWorld().getBlockAt(aux);
            if( block.getType().isSolid() ){
                return i;
            }
            if(i<-60){
                return i;
            }
            i--;
        }
    }

    public static boolean hasBlockAbove(Player player){
        Location loc = player.getLocation();
        Location aux;
        Block block;

        for (int i = HEIGHT ; i>=loc.getBlockY()+1 ; i--){

            aux = new Location(loc.getWorld(),loc.getX(),i,loc.getZ());
            block = loc.getWorld().getBlockAt(aux);
            if(block.getType().isSolid() && block.getType().name().equalsIgnoreCase("oak_sign")){
                return true;
            }
        }
        return false;
    }

    public static void launchAndTp( Player player , Location newlocation , String message ){

        player.getWorld().getChunkAt(newlocation).load();


        if(player.getGameMode()==GameMode.CREATIVE||player.getGameMode()==GameMode.SPECTATOR){
            newlocation.setY(getFirstBlockHeight(player));
            player.teleport(newlocation);
            PostAPI.playSoundAfterTp(player,newlocation);
            PostAPI.sendActionBarOrChat(player,message);
            if(player.isGliding()){
                player.setGliding(false);
            }

            Entity e;

            for (UUID id : LeashAPI.getLeashed(player)){
                e = Bukkit.getEntity(id);
                if(e == null) {
                    continue;
                }
                e.teleport(newlocation);
            }

            return;
        }

        LeashAPI.teleportLeashed(player,newlocation);

        if(Telepost.getInstance().getConfig().getBoolean("launch-feature")){
            player.setVelocity(new Vector(0,10,0));
            Bukkit.getScheduler().runTaskLater(Telepost.getInstance(), () -> {
                Location location = new Location( player.getWorld() , newlocation.getBlockX() + 0.5 , newlocation.getBlockY() , newlocation.getBlockZ() + 0.5 , player.getLocation().getYaw() , player.getLocation().getPitch() );
                player.teleport(location);
                sendActionBarOrChat(player,message);
            }, 50L);
        }else{
            player.teleport(newlocation);
            PostAPI.sendActionBarOrChat(player,message);
        }
        // Launches a player to the sky
        if(player.getGameMode()== GameMode.SURVIVAL||player.getGameMode()==GameMode.ADVENTURE){
            if(Telepost.getInstance().getConfig().getBoolean("tp-in-the-air")){
                Telepost.getInstance().blockFall.add(player.getUniqueId().toString());
            }
        }
        if(player.isGliding()){
            player.setGliding(false);
        }

    }

    public static String idToName(String s){
        return s.replace("."," ");
    }


    public static String getNearestPostID(Player player ){
        if(Telepost.getInstance().getConfig().getBoolean("multiple-names-per-post")){
            return null;
        }

        int postX = PostAPI.getNearPost(player.getLocation().getBlockX(), Telepost.getInstance().getConfig().getInt("post-x-location"));
        int postZ = PostAPI.getNearPost(player.getLocation().getBlockZ(), Telepost.getInstance().getConfig().getInt("post-z-location"));

        List<String> warpNames = new ArrayList<>(Warp.getWarps().keySet());

        for(String warpName: warpNames){
            Location postLocation = Warp.getWarps().get(warpName).getLocation();
            if( postLocation.getBlockX()==postX && postLocation.getBlockZ()==postZ && !Telepost.getInstance().getConfig().getBoolean("multiple-names-per-post")){
                return warpName;
            }
        }
        return null;
    }

    public static boolean isPlayerOnPost ( Player player ) {
        int width = (Telepost.getInstance().getConfig().getInt("post-width")-1)/2;
        int originX = Telepost.getInstance().getConfig().getInt("post-x-location");
        int originZ = Telepost.getInstance().getConfig().getInt("post-z-location");
        // Getting the cords of nearest post to the player
        int postX = PostAPI.getNearPost(player.getLocation().getBlockX(),originX);
        int postZ = PostAPI.getNearPost(player.getLocation().getBlockZ(),originZ);

        // Getting player x and z cords
        int playerX = player.getLocation().getBlockX();
        int playerZ = player.getLocation().getBlockZ();

        return !(playerX < postX - width || playerX > postX + width && playerZ < postZ - width || playerZ > postZ + width);
    }

    public static String getMessage(String path){
        return colour(Telepost.getMessages().getString(path));
    }

    public static String colour(String message){
        return ChatColor.translateAlternateColorCodes('&',message);
    }
}