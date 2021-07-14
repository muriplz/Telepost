package muriplz.kryeittpplugin.commands;

import muriplz.kryeittpplugin.KryeitTPPlugin;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PostAPI {
    public KryeitTPPlugin plugin;
    public PostAPI(KryeitTPPlugin plugin) {
        this.plugin = plugin;
    }

    // Vanilla values: https://minecraft.fandom.com/wiki/Transportation#Vertical_speeds
    private final static double DECELERATION_RATE = 0.98D;
    private final static double GRAVITY_CONSTANT = 0.08D;
    private final static double VANILA_ANTICHEAT_THRESHOLD = 10D;


    public static int getNearPost(int gap, int player, int origin) {
        // Subtracting origin of posts to get correct calculation
        player -= origin;

        // Setting the post variable
        float post = player;

        // Getting the closest multiple of gap to post
        post = post / gap;
        post = Math.round(post);
        post = post * gap;

        // Adding origin of posts to finish calculation
        post += origin;

        // Returning the number
        return (int) post;
    }

    public static void playSoundAfterTp(Player player,Location location){
        player.playSound(location, Sound.ENTITY_DRAGON_FIREBALL_EXPLODE,1f,1f);
    }

    public static void sendMessage(Player player,String message){
        // Sending the message with & instead of §
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
    }

    public static void launch(Player player,KryeitTPPlugin plugin,Vector speed,Location newlocation){
        new BukkitRunnable() {
            double velY = speed.getY();
            final Location locCached = new Location(null,0,0,0);
            @Override
            public void run() {
                if (velY > VANILA_ANTICHEAT_THRESHOLD) {
                    player.getLocation(locCached).setY(locCached.getY() + velY);
                    player.teleport(locCached);
                    player.setVelocity(new Vector(0,VANILA_ANTICHEAT_THRESHOLD,0));
                } else {
                    player.setVelocity(new Vector(0,velY,0));
                    this.cancel();
                }
                velY -= GRAVITY_CONSTANT;
                velY *= DECELERATION_RATE;
            }
        }.runTaskTimer(plugin,0,1);
        player.teleport(newlocation);
    }


    public static boolean isPlayerOnPost(Player player,KryeitTPPlugin plugin) {
        int gap = plugin.getConfig().getInt("distance-between-posts");
        int width = (plugin.getConfig().getInt("post-width")-1)/2;
        int originX = plugin.getConfig().getInt("post-x-location");
        int originZ = plugin.getConfig().getInt("post-z-location");
        // Getting the cords of nearest post to the player
        int postX = PostAPI.getNearPost(gap,player.getLocation().getBlockX(),originX);
        int postZ = PostAPI.getNearPost(gap,player.getLocation().getBlockZ(),originZ);

        // Getting player x and z cords
        int playerX = player.getLocation().getBlockX();
        int playerZ = player.getLocation().getBlockZ();

        return !(playerX < postX - width || playerX > postX + width && playerZ < postZ - width || playerZ > postZ + width);
    }

    public static int getFirstSolidBlockHeight(int X, int Z){
        // Setting the highest height the post can be at
        int height = 251;

        // Looping down and searching for a solid block or water or lava
        while (true){
            Location l = new Location(Bukkit.getWorld("world"), X, height, Z);
            if(l.getBlock().getType().isSolid() || l.getBlock().getType().equals(Material.WATER) || l.getBlock().getType().equals(Material.LAVA)){
                break;
            }
            height--;

            // If height gets to be 5, that means that it cant go lower
            if(height == 5){
                break;
            }
        }
        return height;
    }
    public static int getPostAmount(KryeitTPPlugin plugin){
        int originX = plugin.getConfig().getInt("post-x-location");
        int originZ = plugin.getConfig().getInt("post-z-location");
        int gap = plugin.getConfig().getInt("distance-between-posts");

        WorldBorder worldBorder = Objects.requireNonNull(Bukkit.getServer().getWorld("world")).getWorldBorder();
        int size = (int) worldBorder.getSize();

        int postAmountX= (size - originX)/gap + 1 + (size + originX)/gap;
        int postAmountZ= (size - originZ)/gap + 1 + (size + originZ)/gap;

        double postAmount = postAmountZ*postAmountX;
        return (int) postAmount;
    }
    public static List<Location> getAllPostLocations(KryeitTPPlugin plugin){
        int originX = plugin.getConfig().getInt("post-x-location");
        int originZ = plugin.getConfig().getInt("post-z-location");
        int gap = plugin.getConfig().getInt("distance-between-posts");

        List<Location> allPosts = new ArrayList<>();

        WorldBorder worldBorder = Objects.requireNonNull(Bukkit.getServer().getWorld("world")).getWorldBorder();
        int size = (int) worldBorder.getSize();

        int startX =(-size)/gap;
        startX=startX*gap-originX;

        int startZ =(-size)/gap;
        startZ=startZ*gap-originZ;

        for(int i = startX ; i < Math.abs(startX+2*originX) ; i += gap ){
            for ( int j = startZ ; j < Math.abs(startZ+2*originZ) ; j += gap ){
                Location loc = new Location(Bukkit.getWorld("world"),i,265,j,0,0);
                allPosts.add(loc);
            }
        }
        return allPosts;
    }

//    public static void unloadAllChunksToBuildThePost(Block block,int width){
//        // Getting block x and z coords
//        int x = block.getX();
//        int z = block.getZ();
//
//        // Unloading the chunks that are in the area of the base
//        for (int i=x-width;i<=x+width;i++) {
//            for (int j = z - width; j <= z + width; j++) {
//                // Getting the chunk the block is in
//                Chunk chunk = Objects.requireNonNull(Bukkit.getWorld("world")).getChunkAt(i, j);
//
//                // Checking if the chunk is loaded and if it is unload it
//                if (chunk.isLoaded()) {
//                    chunk.unload();
//                }
//            }
//        }
//    }
//    public static void loadAllChunksToBuildThePost(Block block, int width){
//        // Getting block x and z coords
//        int x = block.getX();
//        int z = block.getZ();
//
//        // Loading the chunks that are in the area of the base
//        for (int i=x-width;i<=x+width;i++) {
//            for (int j=z-width;j<=z+width;j++) {
//                // Getting the chunk the block is in
//                Chunk chunk = Objects.requireNonNull(Bukkit.getWorld("world")).getChunkAt(i,j);
//
//                // Checking if the chunk is unloaded and if it isn't load it
//                if (!chunk.isLoaded()) {
//                    chunk.load();
//                }
//            }
//        }
//    }
}