package com.kryeit.commands;

import com.kryeit.Telepost;
import com.kryeit.storage.bytes.Home;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

import static com.kryeit.commands.PostAPI.HEIGHT;
import static com.kryeit.commands.PostAPI.WORLD_NAME;

public class HomePostCommand implements CommandExecutor {
    //  This commands aims to be /HomePost in-game
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Telepost instance = Telepost.getInstance();

        if (!(sender instanceof Player player)) {
            Bukkit.getConsoleSender().sendMessage(instance.name + PostAPI.getMessage("cant-execute-from-console"));
            return false;
        }

        // If the command is not /homepost ONLY then return false
        if (args.length != 0) {
            player.sendMessage(PostAPI.getMessage("homepost-usage"));
            return false;
        }

        // Check if the player is on the right dimension
        if (!player.getWorld().getName().equals(WORLD_NAME)) {
            player.sendMessage(PostAPI.getMessage("not-on-overworld"));
            return false;
        }

        // If the player is not on the ground stop the command
        if (!((Entity) player).isOnGround() && !player.hasPermission("telepost.homepost")) {
            return false;
        }

        Location nearestPost = PostAPI.getNearPostLocation(player);
        int postX = nearestPost.getBlockX();
        int postZ = nearestPost.getBlockZ();

        // If the player is not inside a post and does not have telepost.homepost permission, he won't be teleported
        if (!PostAPI.isPlayerOnPost(player) && !player.hasPermission("telepost.homepost")) {
            PostAPI.sendActionBarOrChat(player, PostAPI.getMessage("not-inside-post"));
            return false;
        }

        if (PostAPI.hasBlockAbove(player)) {
            PostAPI.sendActionBarOrChat(player, PostAPI.getMessage("block-above"));
            return false;
        }

        Optional<Home> home = Telepost.getDB().getHome(player);
        if (home.isEmpty()) {
            // Player does not have a homepost
            PostAPI.sendActionBarOrChat(player, PostAPI.getMessage("homepost-without-setpost"));
            return true;
        }

        Location location = home.get().location();
        // You can't /homepost to the same post you are in, except if you have telepost.homepost permission
        if (location.getBlockX() == postX && location.getBlockZ() == postZ && !player.hasPermission("telepost.homepost")) {
            PostAPI.sendActionBarOrChat(player, PostAPI.getMessage("already-at-homepost"));
            return false;
        }

        Location newlocation = new Location(player.getWorld(),
                location.getBlockX() + 0.5,
                HEIGHT,
                location.getBlockZ() + 0.5,
                player.getLocation().getYaw(),
                player.getLocation().getPitch());

        PostAPI.launchAndTp(player, newlocation, PostAPI.getMessage("own-homepost-arrival"));
        return true;
    }
}
