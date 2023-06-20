package com.kryeit.commands;

import com.kryeit.Builder.SignBuilderAPI;
import com.kryeit.Telepost;
import com.kryeit.compat.CompatAddon;
import com.kryeit.compat.GriefDefenderImpl;
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
import static com.kryeit.compat.GriefDefenderImpl.NEEDED_CLAIMBLOCKS;

public class NamePostCommand implements CommandExecutor {
    // This command aims to be /NamePost in-game
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Telepost instance = Telepost.getInstance();

        if (!(sender instanceof Player player)) {
            Bukkit.getConsoleSender().sendMessage(instance.name + PostAPI.getMessage("cant-execute-from-console"));
            return false;
        }

        if (args.length == 0) {
            PostAPI.sendMessage(player, PostAPI.getMessage("namepost-usage"));
            return false;
        }

        if (!player.getWorld().getName().equals(WORLD_NAME)) {
            player.sendMessage(PostAPI.getMessage("not-on-overworld"));
            return false;
        }

        Location nearestPost = PostAPI.getNearPostLocation(player);
        int postX = nearestPost.getBlockX();
        int postZ = nearestPost.getBlockZ();

        Optional<Post> post = Telepost.getDB().getPost(PostAPI.getPostID(args));
        if (post.isPresent()) {
            player.sendMessage(PostAPI.getMessage("named-post-already-exists").replace("%POST_NAME%", post.get().name()));
            return false;
        }

        for (Post p : Telepost.getDB().getPosts()) {
            Location loc = p.location();
            if (loc.getBlockX() == postX && loc.getBlockZ() == postZ) {
                player.sendMessage(PostAPI.getMessage("nearest-already-named").replace("%POST_NAME%", p.name()));
                return false;
            }
        }

        if (!player.hasPermission("telepost.namepost")) {

            if(!CompatAddon.GRIEF_DEFENDER.isLoaded()) {
                player.sendMessage(PostAPI.getMessage("no-permission"));
                return false;
            }

            int claimBlocks = GriefDefenderImpl.getClaimBlocks(player.getUniqueId());

            if(claimBlocks < NEEDED_CLAIMBLOCKS) {
                PostAPI.sendMessage(player,PostAPI.getMessage("not-enough-claimblocks").replace("%CLAIM_BLOCKS%", String.valueOf(NEEDED_CLAIMBLOCKS)));
                return false;
            }

            if(Telepost.getInstance().playerNamedPosts.getHashMap().containsValue(player.getUniqueId())) {
                PostAPI.sendMessage(player,"&cYou have already named a post. If you wish to change the name or your named post, contact an admin.");
                return false;
            }

        }

        String postName = PostAPI.getPostName(args);
        Telepost.getDB().addPost(new Post(PostAPI.nameToId(postName), postName, new Location(player.getWorld(), postX, 0, postZ, 0, 0)));
        player.sendMessage(PostAPI.getMessage("name-post").replace("%POST_NAME%", postName));
        if(!player.hasPermission("telepost.namepost")) {
            try {
                Telepost.getInstance().playerNamedPosts.addElement(postName,player.getUniqueId());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        SignBuilderAPI.placeSignWhenNamed(player,nearestPost, postName,true);
        return true;
    }
}
