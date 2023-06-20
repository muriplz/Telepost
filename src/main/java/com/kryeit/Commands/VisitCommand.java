package com.kryeit.commands;

import com.kryeit.Telepost;
import com.kryeit.storage.bytes.Home;
import com.kryeit.storage.bytes.Post;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import static com.kryeit.commands.PostAPI.HEIGHT;
import static com.kryeit.commands.PostAPI.WORLD_NAME;


public class VisitCommand implements CommandExecutor {
    //  This commands aims to be /visit in-game
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Telepost instance = Telepost.getInstance();

        if (!(sender instanceof Player player)) {
            Bukkit.getConsoleSender().sendMessage(instance.name + PostAPI.getMessage("cant-execute-from-console"));
            return false;
        }

        // If the player is not on the ground stop the command
        if (!((Entity) player).isOnGround() && !PostAPI.isGod(player)) return false;

        // /v
        if (args.length == 0) {
            // Command Usage (This command can't have usage: on plugin.yml because it relies on "return true/false;")
            player.sendMessage(PostAPI.getMessage("visit-usage"));
            return false;
        }

        if (!player.getWorld().getName().equals(WORLD_NAME)) {
            player.sendMessage(PostAPI.getMessage("not-on-overworld"));
            return false;
        }

        // See if the player is inside a post
        if (!player.hasPermission("telepost.visit")) {
            if (!PostAPI.isPlayerOnPost(player)) {
                PostAPI.sendActionBarOrChat(player, PostAPI.getMessage("not-inside-post"));
                return false;
            }
        }

        if (PostAPI.hasBlockAbove(player) && !PostAPI.isGod(player)) {
            PostAPI.sendActionBarOrChat(player, PostAPI.getMessage("block-above"));
            return false;
        }

        Location nearestPost = PostAPI.getNearPostLocation(player);
        int postX = nearestPost.getBlockX();
        int postZ = nearestPost.getBlockZ();

        String postName = PostAPI.getPostName(args);
        String postID = PostAPI.getPostID(args);

        // If args[0] is the Name of a post
        Optional<Post> post = Telepost.getDB().getPost(postID);
        if (post.isPresent()) {
            Location pos = post.get().location();

            // See if the player want to teleport to the nearest post, only with telepost.v permission you can do this
            if (pos.getBlockX() == postX && pos.getBlockZ() == postZ && !player.hasPermission("telepost.visit")) {
                PostAPI.sendActionBarOrChat(player, PostAPI.getMessage("already-at-namedpost").replace("%POST_NAME%", post.get().name()));
                return false;
            }

            int height = HEIGHT;

            if (PostAPI.isGod(player)) height = PostAPI.getFirstSolid(player.getLocation()) + 3;

            // Get the location of the post that the player wants to teleport to
            Location loc = new Location(player.getWorld(), pos.getBlockX() + 0.5, height, pos.getBlockZ() + 0.5, player.getLocation().getYaw(), player.getLocation().getPitch());
            String message = PostAPI.getMessage("named-post-arrival").replace("%POST_NAME%", post.get().name());
            PostAPI.launchAndTp(player, loc, message);
            return true;
        }

        if (args.length != 1) return false;

        // /v <Yourself> which is the same as /homepost
        if (player.getName().equals(args[0])) {
            // If player already has a home
            Optional<Home> home = instance.database.getHome(player.getUniqueId());
            if (home.isEmpty()) {
                // Player does not have a home post yet
                player.sendMessage(PostAPI.getMessage("no-homepost"));
                return false;
            }

            Location homeLoc = home.get().location();

            // See if the player is already at his home post, if he has permission he can teleport
            if (homeLoc.getBlockX() == postX && homeLoc.getBlockZ() == postZ && !player.hasPermission("telepost.visit")) {
                PostAPI.sendActionBarOrChat(player, PostAPI.getMessage("already-at-homepost"));
                return false;
            }

            Location newlocation = new Location(player.getWorld(), homeLoc.getBlockX() + 0.5, HEIGHT, homeLoc.getBlockZ() + 0.5, player.getLocation().getYaw(), player.getLocation().getPitch());
            String message = PostAPI.getMessage("own-homepost-arrival");
            PostAPI.launchAndTp(player, newlocation, message);
            return true;
        }
        // /visit <Player>

        // Check if player from (/visit <player>) is actually a player
        return handleVisitPlayer(player, args[0], HEIGHT, postX, postZ);
    }

    private static boolean handleVisitPlayer(Player player, String arg, int height, int postX, int postZ) {
        Player targetPlayer = Bukkit.getPlayer(arg);
        if (targetPlayer == null && player.hasPermission("telepost.visit.others")) {
            OfflinePlayer offlinePlayer = null;
            for (OfflinePlayer p : Bukkit.getOfflinePlayers()) {
                if (Objects.equals(p.getName(), arg)) {
                    offlinePlayer = p;
                    break;
                }
            }

            if (offlinePlayer == null) {
                player.sendMessage(PostAPI.getMessage("unknown-post").replace("%POST_NAME%", arg));
                return false;
            }

            Optional<Home> offlineHome = Telepost.getDB().getHome(offlinePlayer.getUniqueId());
            if (offlineHome.isPresent()) {
                launchPlayer(arg, player, height, offlineHome.get().location());
                return true;
            }
        } else {

            if (targetPlayer == null) {
                player.sendMessage(PostAPI.getMessage("unknown-post").replace("%POST_NAME%", arg));
                return false;
            }

            UUID playerID = targetPlayer.getUniqueId();

            // Check if the sender has been invited
            boolean isInvited = Telepost.getInstance().invites.getValues(playerID).contains(player.getUniqueId());

            if (player.hasPermission("telepost.visit.others")) {
                Optional<Home> home = Telepost.getDB().getHome(playerID);
                if (home.isPresent()) {
                    String message = PostAPI.getMessage("invited-home-arrival").replace("%PLAYER_NAME%", arg);
                    PostAPI.launchAndTp(player, home.get().location(), message);
                    return true;
                }
            } else if (isInvited) {
                Optional<Home> invitedHome = Telepost.getDB().getHome(playerID);
                if (invitedHome.isPresent()) {
                    Location loc = invitedHome.get().location();
                    // See if he wants to teleport to a post he is already in. If he has permission this has no effect
                    if (loc.getBlockX() == postX && loc.getBlockZ() == postZ && !player.hasPermission("telepost.visit")) {
                        PostAPI.sendActionBarOrChat(player, PostAPI.getMessage("already-invited-post"));
                        return false;
                    }
                    launchPlayer(arg, player, height, loc);
                    return true;
                }
            }
            player.sendMessage(PostAPI.getMessage("visit-not-invited"));
        }
        return false;
    }

    private static void launchPlayer(@NotNull String arg, Player player, int height, Location loc) {
        Location newlocation = new Location(player.getWorld(),
                loc.getBlockX() + 0.5,
                height,
                loc.getBlockZ() + 0.5,
                player.getLocation().getYaw(),
                player.getLocation().getPitch());
        String message = PostAPI.getMessage("invited-home-arrival").replace("%PLAYER_NAME%", arg);
        PostAPI.launchAndTp(player, newlocation, message);
    }
}




