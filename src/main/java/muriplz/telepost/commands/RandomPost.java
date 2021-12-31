package muriplz.telepost.commands;

import muriplz.telepost.Telepost;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.WorldBorder;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class RandomPost implements CommandExecutor {

    public Telepost instance = Telepost.getInstance();


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (!(sender instanceof Player)) {
            Bukkit.getConsoleSender().sendMessage(instance.name + PostAPI.getMessage("cant-execute-from-console"));
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
                player.sendMessage(PostAPI.getMessage("worldborder-too-big"));
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
                player.sendMessage(PostAPI.getMessage("random-no-posts"));
                return false;
            }else if(availablePosts.size()<5){
                player.sendMessage(PostAPI.getMessage("random-not-enough-posts"));
            }

            Collections.shuffle(availablePosts);

            Random r = new Random();
            int index = r.nextInt(availablePosts.size()+1);


            Location l = availablePosts.get(index);
            l.setX(l.getBlockX()+0.5);
            l.setZ(l.getBlockZ()+0.5);
            PostAPI.launchAndTp(player,l,PostAPI.getMessage("random-tp"));
            instance.blockFall.add(player.getUniqueId());

            return true;
        }
    }
}
