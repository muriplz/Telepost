package com.kryeit.commands;


import com.kryeit.Telepost;
import com.kryeit.storage.bytes.Home;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.UUID;

import static com.kryeit.commands.PostAPI.HEIGHT;
import static com.kryeit.commands.PostAPI.WORLD_NAME;


public class SetPostCommand implements CommandExecutor {
    //  This commands aims to be /SetPost in-game
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Telepost instance = Telepost.getInstance();

        if (!(sender instanceof Player player)) {
            Bukkit.getConsoleSender().sendMessage(instance.name + PostAPI.getMessage("cant-execute-from-console"));
            return false;
        } else {
            // If the command is not /setpost ONLY then return false
            if (args.length != 0) {
                player.sendMessage(PostAPI.getMessage("setpost-usage"));
                return false;
            }

            if (!player.getWorld().getName().equals(WORLD_NAME)) {
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

            // Location of the nearest post
            Location location = new Location(player.getWorld(), postX, HEIGHT, postZ, 0, 0);

            // Moving the home if he already has one
            UUID playerID = player.getUniqueId();
            String coords = "(" + postX + " , " + postZ + ")";

            Optional<Home> home = Telepost.getDB().getHome(playerID);
            Telepost.getDB().setHome(playerID, new Home(playerID, location));

            String msgId = home.isPresent() ? "move-post-success" : "set-post-success";
            PostAPI.sendActionBarOrChat(player, PostAPI.getMessage(msgId).replace("%POST_LOCATION%", coords));
            return true;
        }
    }
}
