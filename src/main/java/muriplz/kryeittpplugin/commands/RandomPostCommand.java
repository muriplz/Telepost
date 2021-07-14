package muriplz.kryeittpplugin.commands;

import io.github.niestrat99.advancedteleport.api.ATPlayer;
import io.github.niestrat99.advancedteleport.api.Warp;
import muriplz.kryeittpplugin.KryeitTPPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.WorldBorder;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class RandomPostCommand implements CommandExecutor {

    // Function to remove duplicates from an ArrayList
    public static List<Location> removeDuplicates(List<Location> list) {
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

    private final KryeitTPPlugin plugin;

    public RandomPostCommand(KryeitTPPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (!(sender instanceof Player)) {
            Bukkit.getConsoleSender().sendMessage(plugin.name + "You can't execute this command from console.");
            return false;
        } else {
            Player player = (Player) sender;

            if(!plugin.getConfig().getBoolean("random-post")){
                PostAPI.sendMessage(player,"This feature has been disabled.");
                return false;
            }

            if(!player.hasPermission("telepost.randompost")){
                PostAPI.sendMessage(player,"&cYou don't have permission to use this command.");
                return false;
            }

            // Player has to be in the Overworld
            if (!player.getWorld().getName().equals("world")) {
                PostAPI.sendMessage(player, "&cYou have to be in the Overworld to use this command.");
                return false;
            }

            if(!PostAPI.isPlayerOnPost(player,plugin)){
                PostAPI.sendMessage(player, "&cYou have to be inside a post.");
                return false;
            }

            List<Location> allNamedAndHomed = new ArrayList<>();
            List<Location> allPosts;
            List<Location> availablePosts = new ArrayList<>();



            WorldBorder worldBorder = Objects.requireNonNull(Bukkit.getServer().getWorld("world")).getWorldBorder();
            int size = (int) worldBorder.getSize();

            if ( size > 1000000) {
                PostAPI.sendMessage(player,"The world border is too big or you have not set it right, use /worldborder set <Amount>.");
                return false;
            }



            for(OfflinePlayer p : Bukkit.getServer().getOfflinePlayers()){
                ATPlayer atPlayer = ATPlayer.getPlayer(p);
                if(atPlayer.hasHome("home")){
                    Location home = atPlayer.getHome("home").getLocation();
                    Location loc = new Location(player.getWorld(),home.getBlockX(),265.0,home.getBlockZ(),0,0);
                    allNamedAndHomed.add(loc);
                }
            }
            for(Player p : Bukkit.getServer().getOnlinePlayers()){
                ATPlayer atPlayer = ATPlayer.getPlayer(p);
                if(atPlayer.hasHome("home")){
                    Location home = atPlayer.getHome("home").getLocation();
                    Location loc = new Location(player.getWorld(),home.getBlockX(),265.0,home.getBlockZ(),0,0);
                    allNamedAndHomed.add(loc);
                }
            }
            for(Warp namedPost: Warp.getWarps().values()){
                Location namedLoc = namedPost.getLocation();
                Location loc = new Location(player.getWorld(),namedLoc.getBlockX(),265.0,namedLoc.getBlockZ(),0,0);
                allNamedAndHomed.add(loc);
            }
            allNamedAndHomed = removeDuplicates(allNamedAndHomed);
            allPosts = PostAPI.getAllPostLocations(plugin);

            for(Location l : allPosts) {
                if(!allNamedAndHomed.contains(l)){
                    availablePosts.add(l);
                }
            }
            if(availablePosts.size()==0){
                PostAPI.sendMessage(player,"&cThere are no available posts.");
                return false;
            }

            Collections.shuffle(availablePosts);

            Random r = new Random();
            int low = 0;
            int high = availablePosts.size()+1;
            int index = r.nextInt(high-low) + low;

            Location l = availablePosts.get(index);
            player.teleport(l);
            plugin.blockFall.add(player.getUniqueId());

            return true;
        }
    }
}
