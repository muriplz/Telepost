package muriplz.telepost.commands;

import io.github.niestrat99.advancedteleport.api.ATPlayer;
import io.github.niestrat99.advancedteleport.api.Warp;
import muriplz.telepost.Telepost;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.*;

public class PostAPI {
// HERE COMES STATIC ABUSE :D
    public Telepost instance = Telepost.getInstance();

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
        return new Location(player.getWorld(),postX,265,postZ);
    }

    public static int getNearPost( int playerXorZ,int origin) {
        int gap = Telepost.getInstance().getConfig().getInt("distance-between-posts");

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

    public static void buildAPost(Location l){
        Material base = Material.getMaterial(Objects.requireNonNull(Telepost.getInstance().getConfig().getString("base-material")));
        Material pilar = Material.getMaterial(Objects.requireNonNull(Telepost.getInstance().getConfig().getString("pillar-material")));


        int width = (Telepost.getInstance().getConfig().getInt("post-width")-1)/2;

        int height = getFirstSolidBlockHeight(l.getBlockX(),l.getBlockZ());

        clearPostVolume(l);

        for(int i = l.getBlockX()-width ; i<= l.getBlockX()+width;i++){
            for(int j = l.getBlockZ()-width;j<=l.getBlockZ()+width;j++){
                Location loc = new Location(Bukkit.getWorld("world"),i,height,j);
                Block block = loc.getBlock();
                block.setType(base);
            }
        }
        l.setY(height+1);
        for(int i=1;i<4;i++){
            l.getBlock().setType(pilar);
            l.setY(l.getBlockY()+1);

        }
    }

    public static void buildAllPosts(){
        int i = 1;
        for(Location l : getAllPostLocations()){
            final Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    if(!l.getChunk().isLoaded()){
                        l.getChunk().load();
                    }
                    buildAPost(l);
                    l.getChunk().unload();
                    Bukkit.getConsoleSender().sendMessage("Post built on: "+"("+l.getBlockX()+" , "+l.getBlockZ()+").");
                }
            }, 300L *i);
            i+=1;


        }
    }

    public static void clearPostVolume(Location l){
        int width = (Telepost.getInstance().getConfig().getInt("post-width")-1)/2;

        int height = getFirstSolidBlockHeight(l.getBlockX(),l.getBlockZ());

        for(int i = l.getBlockX()-width ; i<= l.getBlockX()+width;i++){
            for(int j = l.getBlockZ()-width;j<=l.getBlockZ()+width;j++){
                for(int k = height; k<=265;k++){
                    Location loc = new Location(Bukkit.getWorld("world"),i,k,j);
                    Block block = loc.getBlock();
                    if(!(block.getType()==Material.AIR)){
                        block.setType(Material.AIR);
                    }
                }
            }
        }
    }

    public static void loadChunk(Location l){
        Objects.requireNonNull(Bukkit.getWorld("world")).getChunkAt(l.getBlockX(),l.getBlockZ()).load();
    }
    public static void launchAndTp( Player player , Location newlocation , String message ){

        int height;
        // See if the config has the option set to true, in that case the teleport takes the player to the air
        if(Telepost.getInstance().getConfig().getBoolean("tp-in-the-air")){
            height = 265;
        }else{
            // If the option is false, teleport them to the first block that is air
            height = PostAPI.getFirstSolidBlockHeight(newlocation.getBlockX(),newlocation.getBlockZ())+2;
        }
        newlocation.setY(height);

        if(player.getGameMode()==GameMode.CREATIVE||player.getGameMode()==GameMode.SPECTATOR){
            player.teleport(newlocation);
            PostAPI.playSoundAfterTp(player,newlocation);
            PostAPI.sendActionBarOrChat(player,message);
            if(player.isGliding()){
                player.setGliding(false);
            }
            return;
        }
        if(Telepost.getInstance().getConfig().getBoolean("launch-feature")){
            player.setVelocity(new Vector(0,10,0));
            Bukkit.getScheduler().runTaskLater(Telepost.getInstance(), () -> {
                Location location = new Location( player.getWorld() , newlocation.getBlockX() + 0.5 , newlocation.getBlockY() , newlocation.getBlockZ() + 0.5 , player.getLocation().getYaw() , player.getLocation().getPitch() );
                player.teleport(location);
                playSoundAfterTp(player,location);
                sendActionBarOrChat(player,message);
            }, 30L);
        }else{
            player.teleport(newlocation);
            PostAPI.playSoundAfterTp(player,newlocation);
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



    public static String NearestPostName ( Player player ){
        if(Telepost.getInstance().getConfig().getBoolean("multiple-names-per-post")){
            return null;
        }

        int postX = PostAPI.getNearPost(player.getLocation().getBlockX(), Telepost.getInstance().getConfig().getInt("post-x-location"));
        int postZ = PostAPI.getNearPost(player.getLocation().getBlockZ(), Telepost.getInstance().getConfig().getInt("post-z-location"));

        HashMap<String, Warp> warps = Warp.getWarps();
        Set<String> warpNames = warps.keySet();

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

    public static int getFirstSolidBlockHeight ( int X , int Z ){
        // Setting the highest height the post can be at
        int height = 251;

        // Looping down and searching for a solid block or water or lava
        while (true){
            Location l = new Location(Bukkit.getWorld("world"), X, height, Z);
            Block block = l.getBlock();
            Material material = block.getType();
            if(material.equals(Material.WATER) || material.equals(Material.LAVA)){
                break;
            }
            if(material.isSolid()){
                if ((material.equals(Material.ACACIA_LEAVES) || material.equals(Material.BIRCH_LEAVES) || material.equals(Material.OAK_LEAVES) || material.equals(Material.DARK_OAK_LEAVES) || material.equals(Material.JUNGLE_LEAVES) || material.equals(Material.SPRUCE_LEAVES))) {

                }else{
                    break;
                }
            }

            height--;

            // If height gets to be 5, that means that it cant go lower
            if(height == 5){
                break;
            }
        }
        return height;
    }
    public static int getPostAmount (){
        int originX = Telepost.getInstance().getConfig().getInt("post-x-location");
        int originZ = Telepost.getInstance().getConfig().getInt("post-z-location");
        int gap = Telepost.getInstance().getConfig().getInt("distance-between-posts");

        WorldBorder worldBorder = Objects.requireNonNull(Bukkit.getServer().getWorld("world")).getWorldBorder();
        int size = (int) worldBorder.getSize()/2;

        int postAmountX= (size - originX)/gap + (size + originX)/gap;
        int postAmountZ= (size - originZ)/gap + (size + originZ)/gap;

        double postAmount = postAmountZ*postAmountX;
        return (int) postAmount;
    }
    public static List<Location> getAllPostLocations (){
        int originX = Telepost.getInstance().getConfig().getInt("post-x-location");
        int originZ = Telepost.getInstance().getConfig().getInt("post-z-location");
        int gap = Telepost.getInstance().getConfig().getInt("distance-between-posts");

        List<Location> allPosts = new ArrayList<>();

        WorldBorder worldBorder = Objects.requireNonNull(Bukkit.getServer().getWorld("world")).getWorldBorder();
        int size = (int) worldBorder.getSize()/2;

        int startX =(-size)/gap;
        startX=startX*gap-originX;

        int startZ =(-size)/gap;
        startZ=startZ*gap-originZ;


        for(int i = startX ; i < Math.abs(startX+2*originX) ; i += gap ){
            for ( int j = startZ ; j < Math.abs(startZ+2*originZ) ; j += gap ){
                if(Math.abs(i)<size&&Math.abs(j)<size){
                    Location loc = new Location(Bukkit.getWorld("world"),i,265,j);
                    allPosts.add(loc);
                }
            }
        }
        return allPosts;
    }
    public static List<Location> getAllNamedAndHomed () {
        List<Location> allNamedAndHomed = new ArrayList<>();
        for(OfflinePlayer p : Bukkit.getServer().getOfflinePlayers()){
            ATPlayer atPlayer = ATPlayer.getPlayer(p);
            if(atPlayer.hasHome("home")){
                Location home = atPlayer.getHome("home").getLocation();
                Location loc = new Location(Bukkit.getWorld("world"),home.getBlockX(),265.0,home.getBlockZ(),0,0);
                allNamedAndHomed.add(loc);
            }
        }
        for(Player p : Bukkit.getServer().getOnlinePlayers()){
            ATPlayer atPlayer = ATPlayer.getPlayer(p);
            if(atPlayer.hasHome("home")){
                Location home = atPlayer.getHome("home").getLocation();
                Location loc = new Location(Bukkit.getWorld("world"),home.getBlockX(),265.0,home.getBlockZ(),0,0);
                allNamedAndHomed.add(loc);
            }
        }
        for(Warp namedPost: Warp.getWarps().values()){
            Location namedLoc = namedPost.getLocation();
            Location loc = new Location(Bukkit.getWorld("world"),namedLoc.getBlockX(),265.0,namedLoc.getBlockZ(),0,0);
            allNamedAndHomed.add(loc);
        }
        return allNamedAndHomed;
    }

    // Function to remove duplicates from an ArrayList with Location Objects
    public static List<Location> removeLocDuplicates ( List<Location> list ) {
        // Create a new ArrayList
        List<Location> newList = new ArrayList<>();

        // Traverse through the first list
        for (Location element : list) {

            // If this element is not present in newList
            // then add it
            if (!newList.contains(element)) {
                newList.add(element);
            }
        }

        // return the new list
        return newList;
    }
    public static String getMessage(String path){
        return colour(Telepost.getMessages().getString(path));
    }

    public static String colour(String message){
        return ChatColor.translateAlternateColorCodes('&',message);
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