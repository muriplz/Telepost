package com.kryeit.commands;

import com.kryeit.Builder.SignBuilderAPI;
import com.kryeit.Telepost;
import com.kryeit.storage.bytes.Post;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Optional;

import static com.kryeit.commands.PostAPI.WORLD_NAME;

public class UnnamePostCommand implements CommandExecutor {
    // This command aims to be /UnnamePost in-game
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Telepost instance = Telepost.getInstance();

        if (!(sender instanceof Player player)) {
            Bukkit.getConsoleSender().sendMessage(instance.name + PostAPI.getMessage("cant-execute-from-console"));
        } else {

            // Permission node
            if (!player.hasPermission("telepost.unnamepost")) {
                player.sendMessage(PostAPI.getMessage("no-permission"));
                return false;
            }

            // /UnnamePost (this looks for the nearest post)
            if (args.length == 0) {
                if (!player.getWorld().getName().equals(WORLD_NAME)) {
                    player.sendMessage(PostAPI.getMessage("not-on-overworld"));
                    return false;
                }

                Location nearestPost = PostAPI.getNearPostLocation(player);
                int postX = nearestPost.getBlockX();
                int postZ = nearestPost.getBlockZ();

                for (Post post : Telepost.getDB().getPosts()) {
                    Location loc = post.location();
                    if (loc.getBlockX() == postX && loc.getBlockZ() == postZ) {
                        Telepost.getDB().deletePost(post.id());
                        player.sendMessage(PostAPI.getMessage("unname-named-post").replace("%POST_NAME%", post.name()));
                        SignBuilderAPI.placeSignWhenNamed(null,nearestPost, PostAPI.getMessage("unnamed-post"),false);
                        try {
                            Telepost.getInstance().playerNamedPosts.deleteElement(post.name());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        return true;
                    }
                }
                player.sendMessage(PostAPI.getMessage("unnamed-post"));
            }

            // /unnamepost <something> , that something must be a warp name or it won't do anything.
            if (args.length >= 1) {
                String postName = PostAPI.getPostName(args);
                String postID = PostAPI.getPostID(args);

                Optional<Post> post = Telepost.getDB().getPost(postID);
                if (post.isPresent()) {
                    Telepost.getDB().deletePost(postID);
                    player.sendMessage(PostAPI.getMessage("unname-named-post").replace("%POST_NAME%", postName));
                    SignBuilderAPI.placeSignWhenNamed(null,post.get().location(), PostAPI.getMessage("unnamed-post"),false);
                    try {
                        Telepost.getInstance().playerNamedPosts.deleteElement(postName);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    return true;
                } else {
                    player.sendMessage(PostAPI.getMessage("no-such-post"));
                }
            }

        }
        return false;
    }
}
