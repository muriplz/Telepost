package com.kryeit.commands;

import com.kryeit.Telepost;
import com.kryeit.leash.LeashAPI;
import com.kryeit.storage.bytes.Post;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public class PostAPI {
    // HERE COMES STATIC ABUSE :D
    public static Telepost instance = Telepost.getInstance();

    public static boolean isActionBarEnabled = instance.getConfig().getBoolean("messages-on-action-bar");

    public static int GAP = Telepost.getInstance().getConfig().getInt("distance-between-posts");
    public static int ORIGIN_X = Telepost.getInstance().getConfig().getInt("post-x-location");
    public static int ORIGIN_Z = Telepost.getInstance().getConfig().getInt("post-z-location");
    public static int HEIGHT = 319;
    public static String WORLD_NAME = "world";
    public static World WORLD = Objects.requireNonNull(Bukkit.getServer().getWorld(WORLD_NAME));
    public static int WORLDBORDER_RADIUS = (int) (WORLD.getWorldBorder().getSize() / 2);


    public static boolean isOnWorld(Player player, String world) {
        return player.getWorld().getName().equalsIgnoreCase(world);
    }

    public static Location getNearPostLocation(Player player) {
        // for the X axis
        int postX = PostAPI.getNearPost(player.getLocation().getBlockX(), ORIGIN_X);

        // for the Z axis
        int postZ = PostAPI.getNearPost(player.getLocation().getBlockZ(), ORIGIN_Z);

        return new Location(WORLD, postX, WORLD.getHighestBlockYAt(postX,postZ), postZ);
    }

    public static String getPostID(String[] args) {
        return String.join(".", args).toLowerCase();
    }

    public static String getPostName(String[] args) {
        return String.join(" ", args);
    }

    public static String nameToId(String name) {
        return name.replace(" ", ".").toLowerCase();
    }

    public static String idToName(String s) {
        return s.replace(".", " ").toLowerCase();
    }

    public static int getNearPost(int playerXorZ, int origin) {

        // Subtracting origin of posts to get correct calculation
        playerXorZ -= origin;

        // Getting the closest multiple of gap to post
        float post = (float) playerXorZ / GAP;
        post = Math.round(post);
        post = post * GAP;

        // Adding origin of posts to finish calculation
        post += origin;

        // Returning the number
        return (int) post;
    }

    public static void playSoundAfterTp(Location location) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if(!player.getWorld().equals(location.getWorld())) continue;
            if(player.getLocation().distance(location) > 50) continue;
            player.playSound(location, Sound.ENTITY_DRAGON_FIREBALL_EXPLODE, 1f, 1f);
        }
    }

    public static boolean isInsideWorldBorder(Player p) {

        Location nearestPost = PostAPI.getNearPostLocation(p);
        // For the X axis
        int postX = nearestPost.getBlockX();

        // For the Z axis
        int postZ = nearestPost.getBlockZ();

        return Math.abs(postX) > Math.abs(WORLDBORDER_RADIUS) || Math.abs(postZ) > Math.abs(WORLDBORDER_RADIUS);
    }

    public static void sendMessage(Player player, String message) {
        player.sendMessage(colour(message));
    }

    public static void sendActionBarOrChat(Player player, String message) {
        // This will send the message on the action bar, if the option is enabled on config.yml
        if (isActionBarEnabled) {
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(message));
        } else player.sendMessage(message);
    }

    public static int getFirstSolid(Location loc) {
        return WORLD.getHighestBlockAt(loc).getLocation().getBlockY() + 2;
    }

    public static boolean hasBlockAbove(Player player) {
        Location loc = player.getLocation();
        Location aux;
        Block block;

        for (int i = HEIGHT; i > loc.getBlockY() + 1; i--) {

            aux = new Location(loc.getWorld(), loc.getX(), i, loc.getZ());
            block = Objects.requireNonNull(loc.getWorld()).getBlockAt(aux);

            if (block instanceof Sign) continue;
            if (block.getType().isSolid()) return true;
        }
        return false;
    }

    public static void teleport(Player player, Location location) {
        player.teleport(location);
        player.playSound(location, Sound.ENTITY_ENDERMAN_TELEPORT, 1f, 1f);
    }

    public static void launchAndTp(Player player, Location newlocation, String message) {

        if (player.isGliding()) player.setGliding(false);

        player.getWorld().getChunkAt(newlocation).load();

        if (isGod(player)) {
            newlocation = new Location(player.getWorld(), newlocation.getBlockX() + 0.5, getFirstSolid(newlocation), newlocation.getBlockZ() + 0.5);

            teleport(player,newlocation);
            PostAPI.playSoundAfterTp(newlocation);
            PostAPI.sendActionBarOrChat(player, message);

            if (!LeashAPI.hasLeashed(player)) return;

            Entity e;

            for (UUID id : LeashAPI.getLeashed(player)) {
                e = Bukkit.getEntity(id);
                if (e == null) continue;
                e.teleport(newlocation);
            }

            return;
        }

        if (Telepost.getInstance().getConfig().getBoolean("launch-feature") && !LeashAPI.hasLeashed(player)) {
            player.setVelocity(new Vector(0, 10, 0));
            Location finalNewlocation = newlocation;
            Bukkit.getScheduler().runTaskLater(Telepost.getInstance(), () -> {
                Location location = new Location(player.getWorld(), finalNewlocation.getBlockX() + 0.5, finalNewlocation.getBlockY(), finalNewlocation.getBlockZ() + 0.5, player.getLocation().getYaw(), player.getLocation().getPitch());
                teleport(player,location);
                instance.blockFall.add(player.getUniqueId());
                sendActionBarOrChat(player, message);
            }, 40L);
        } else {
            newlocation = new Location(player.getWorld(), newlocation.getBlockX() + 0.5, getFirstSolid(newlocation), newlocation.getBlockZ() + 0.5);

            teleport(player,newlocation);
            PostAPI.sendActionBarOrChat(player, message);
        }

        if (LeashAPI.hasLeashed(player)) {
            LeashAPI.teleportLeashed(player, newlocation);
        }

    }

    public static Optional<Post> getNearestPost(Player player) {
        if (Telepost.getInstance().getConfig().getBoolean("multiple-names-per-post")) return Optional.empty();

        int postX = PostAPI.getNearPost(player.getLocation().getBlockX(), ORIGIN_X);
        int postZ = PostAPI.getNearPost(player.getLocation().getBlockZ(), ORIGIN_Z);

        for (Post post : Telepost.getInstance().database.getPosts()) {
            Location postLocation = post.location();
            if (postLocation.getBlockX() == postX && postLocation.getBlockZ() == postZ) {
                return Optional.of(post);
            }
        }
        return Optional.empty();
    }

    public static boolean isGod(Player player) {
        return player.getGameMode() == GameMode.CREATIVE || player.getGameMode() == GameMode.SPECTATOR;
    }

    public static boolean isPlayerOnPost(Player player) {
        int width = (Telepost.getInstance().getConfig().getInt("post-width") - 1) / 2;

        // Getting the cords of nearest post to the player
        int postX = PostAPI.getNearPost(player.getLocation().getBlockX(), ORIGIN_X);
        int postZ = PostAPI.getNearPost(player.getLocation().getBlockZ(), ORIGIN_Z);

        // Getting player x and z cords
        int playerX = player.getLocation().getBlockX();
        int playerZ = player.getLocation().getBlockZ();

        return !(playerX < postX - width || playerX > postX + width && playerZ < postZ - width || playerZ > postZ + width);
    }

    public static String getMessage(String path) {
        return colour(Telepost.getInstance().messages.getString(path));
    }

    public static String colour(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }
}