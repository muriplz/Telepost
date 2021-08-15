package muriplz.kryeittpplugin.commands;

import muriplz.kryeittpplugin.KryeitTPPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.WorldBorder;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class RandomPostCommand implements CommandExecutor {

    public KryeitTPPlugin instance = KryeitTPPlugin.getInstance();


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (!(sender instanceof Player)) {
            Bukkit.getConsoleSender().sendMessage(instance.name + "You can't execute this command from console.");
            return false;
        } else {
            Player player = (Player) sender;

            if(!instance.getConfig().getBoolean("random-post")){
                PostAPI.sendMessage(player,"This feature has been disabled.");
                return false;
            }

            if(!player.hasPermission("telepost.randompost")){
                player.sendMessage(PostAPI.getMessage("no-permission"));
                return false;
            }

            // Player has to be in the Overworld
            if (!player.getWorld().getName().equals("world")) {
                player.sendMessage(PostAPI.getMessage("not-on-overworld"));
                return false;
            }

            if(!PostAPI.isPlayerOnPost(player)){
                player.sendMessage(PostAPI.getMessage("not-inside-post"));
                return false;
            }

            WorldBorder worldBorder = Objects.requireNonNull(Bukkit.getServer().getWorld("world")).getWorldBorder();
            int size = (int) worldBorder.getSize()/2;

            if ( size > 1000000) {
                PostAPI.sendMessage(player,"The world border is too big or you have not set it right, use /worldborder set <Amount>.");
                return false;
            }

            List<Location> allNamedAndHomed = PostAPI.getAllNamedAndHomed();
            List<Location> allPosts;
            List<Location> availablePosts = new ArrayList<>();
            int height;

            allNamedAndHomed = PostAPI.removeLocDuplicates(allNamedAndHomed);
            allPosts = PostAPI.getAllPostLocations();

            for(Location l : allPosts) {
                if(!allNamedAndHomed.contains(l)){
                    availablePosts.add(l);
                }
            }
            if(availablePosts.size()==0){
                PostAPI.sendMessage(player,"&cThere are no available posts.");
                return false;
            }else if(availablePosts.size()<5){
                PostAPI.sendMessage(player,"&cThere is not enough posts to choose from.");
            }

            Collections.shuffle(availablePosts);

            Random r = new Random();
            int index = r.nextInt(availablePosts.size()+1);


            Location l = availablePosts.get(index);
            // See if the config has the option set to true, in that case the teleport takes the player to the air
            if(instance.getConfig().getBoolean("tp-in-the-air")){
                height = 265;
            }else{
                // If the option is false, teleport them to the first block that is air
                height = PostAPI.getFirstSolidBlockHeight(l.getBlockX(),l.getBlockZ())+2;
            }
            l.setX(l.getBlockX()+0.5);
            l.setZ(l.getBlockZ()+0.5);
            l.setY(height);
            PostAPI.launchAndTp(player,l,"&fYou have been teleported to a random post.");
            instance.blockFall.add(player.getUniqueId());

            return true;
        }
    }
}
