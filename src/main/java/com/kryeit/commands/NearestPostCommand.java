package com.kryeit.commands;

import com.kryeit.Telepost;
import com.kryeit.storage.bytes.Post;
import com.kryeit.util.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

import static com.kryeit.commands.PostAPI.WORLD_NAME;

public class NearestPostCommand implements CommandExecutor {
    Telepost plugin = Telepost.getInstance();

    //  This commands aims to be /NearestPost in-game
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            Bukkit.getConsoleSender().sendMessage(plugin.name + "You can't execute this command from console.");
            return false;
        }

        if (args.length != 0) {
            player.sendMessage("/nearestpost");
            return false;
        }

        // Player has to be in the Overworld
        if (!PostAPI.isOnWorld(player, WORLD_NAME)) {
            player.sendMessage(PostAPI.getMessage("not-on-overworld"));
            return false;
        }

        Location nearestPost = PostAPI.getNearPostLocation(player);
        // For the X axis
        int postX = nearestPost.getBlockX();

        // For the Z axis
        int postZ = nearestPost.getBlockZ();

        if (PostAPI.isInsideWorldBorder(player)) {
            player.sendMessage(PostAPI.colour("&cThe nearest post is outside the world border, try somewhere else"));
            return false;
        }

        Optional<Post> post = PostAPI.getNearestPost(player);
        String coords = "(" + postX + ", " + postZ + ")";
        nearestPost = new Location(nearestPost.getWorld(),nearestPost.getX(),player.getLocation().getY(),nearestPost.getZ());
        String distance = Math.round(player.getLocation().distance(nearestPost)) + "";
        Location location = new Location(player.getWorld(),player.getLocation().getX(),player.getLocation().getY(),player.getLocation().getZ(),Utils.calculateYaw(player.getLocation(),nearestPost),player.getLocation().getPitch());
        player.teleport(location);

        if (post.isPresent()) {
            player.sendMessage(PostAPI.getMessage("nearest-message-named").replace("%POST_LOCATION%", coords).replace("%POST_NAME%", post.get().name()).replace("%DISTANCE%",distance));
            return true;
        }


        player.sendMessage(PostAPI.getMessage("nearest-message").replace("%POST_LOCATION%", coords).replace("%DISTANCE%",distance));
        return true;
    }
}
