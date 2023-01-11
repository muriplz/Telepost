package com.kryeit.Commands;

import com.kryeit.Telepost;
import io.github.niestrat99.advancedteleport.api.Warp;
import io.github.niestrat99.advancedteleport.sql.WarpSQLManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class NamePost implements CommandExecutor {

    public Telepost instance = Telepost.getInstance();
    public String worldName = "world";
    // This command aims to be /NamePost in-game
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if( ! (sender instanceof Player player)) {
            Bukkit.getConsoleSender().sendMessage(instance.name+PostAPI.getMessage("cant-execute-from-console"));
        }else {

            if(args.length == 0) {
                PostAPI.sendMessage(player,PostAPI.getMessage("namepost-usage"));
                return false;
            }

            if(!player.hasPermission("telepost.namepost")) {
                player.sendMessage(PostAPI.getMessage("no-permission"));
                return false;
            }

            if(!player.getWorld().getName().equals(worldName)) {
                player.sendMessage(PostAPI.getMessage("not-on-overworld"));
                return false;
            }

            Location nearestPost = PostAPI.getNearPostLocation(player);
            // For the X axis
            int postX = nearestPost.getBlockX();

            // For the Z axis
            int postZ = nearestPost.getBlockZ();

            String postName = PostAPI.getPostName(args);
            String postID = PostAPI.getPostID(args);

            HashMap<String,Warp> warps = Warp.getWarps();

            if(warps.containsKey(postID)) {
                player.sendMessage(PostAPI.getMessage("named-post-already-exists").replace("%POST_NAME%",postName));
                return false;
            }

            Location nearestpostLocation = new Location(player.getWorld(), postX , 265, postZ ,0,0);

            List<String> warpNames = new ArrayList<>(warps.keySet());

            for(String warpName : warpNames) {
                if(warps.get(warpName).getLocation().getBlockX() == postX && warps.get(warpName).getLocation().getBlockZ() == postZ){
                    player.sendMessage(PostAPI.getMessage("nearest-already-named").replace("%POST_NAME%",PostAPI.idToName(warpName)));
                    return false;
                }
            }

            WarpSQLManager.get().addWarp(new Warp(player.getUniqueId(),
                    postID,
                    nearestpostLocation,
                    System.currentTimeMillis(),
                    System.currentTimeMillis()), callback ->
                    player.sendMessage(PostAPI.getMessage("name-post").replace("%POST_NAME%",postName)));
            return true;
        }
        return false;
    }}
