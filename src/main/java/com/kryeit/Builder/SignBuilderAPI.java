package com.kryeit.Builder;

import com.kryeit.Telepost;
import com.kryeit.commands.PostAPI;
import com.kryeit.storage.bytes.Post;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Rotatable;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;

import java.util.Random;

import static com.kryeit.commands.PostAPI.WORLD;

public class SignBuilderAPI {

    public static boolean isPostBuilt(Location location) {
        Sign sign = getPostSign(location);
        if(sign != null) {
            Bukkit.getConsoleSender().sendMessage("jajaja: "+sign);

            return sign.getLine(1).contains(PostAPI.getMessage("unnamed-post"));
        }
        Bukkit.getConsoleSender().sendMessage("jajaja: vaya");
        return false;
    }

    public static int getSignHeight(Location location) {
        Block b = WORLD.getHighestBlockAt(location);
        if(b.getState() instanceof Sign) return b.getLocation().getBlockY();
        return b.getLocation().getBlockY() + 1;
    }

    public static boolean isPostNamed(Location location) {
        for(Post post : Telepost.getDB().getPosts()) {
            if(location.getBlockX() == post.location().getBlockX() && location.getBlockZ() == post.location().getBlockZ()) return true;
        }
        return false;
    }

    public static Sign getPostSign(Location location){

        Block signBlock = WORLD.getBlockAt(location.getBlockX(), getSignHeight(location), location.getBlockZ());
        if(signBlock.getState() instanceof Sign sign) {
            return sign;
        }
        return null;
    }

    public static Block getPostSignBlock(Location location){

        Block signBlock = WORLD.getBlockAt(location.getBlockX(), getSignHeight(location), location.getBlockZ());
        if(signBlock.getState() instanceof Sign sign) {
            return signBlock;
        }
        return null;
    }

    public static void placeSignWhenNamed(Player player, Location location, String name, boolean glowText){

        Sign sign = getPostSign(location);
        if(sign == null) {
            Block block = WORLD.getBlockAt(location.getBlockX(), getSignHeight(location), location.getBlockZ());
            block.setType(Material.OAK_SIGN);
            sign = (Sign) block.getState();
        }
        sign = setSignDirection(player,sign.getBlock());
        spawnRandomRocket(sign.getLocation());
        sign.setLine(1, name);
        if(glowText) sign.setGlowingText(true);
        sign.update();
    }

    public static Sign setSignDirection(Player player, Block signBlock) {
        Sign sign;
        if(player != null) {
            sign = rotateFloorSign(signBlock,yawToFace(player.getLocation().getYaw(),true));
        } else {
            Random random = new Random();
            float yaw = random.nextFloat() * 360;
            sign = rotateFloorSign(signBlock,yawToFace(yaw,true));
        }
        return sign;
    }

    public static Sign rotateFloorSign(Block block, BlockFace newDirection) {
        BlockData blockData = block.getState().getBlockData();

        if (blockData instanceof Rotatable rotatableData) {
            rotatableData.setRotation(newDirection);
            block.setBlockData(rotatableData);
        }
        return (Sign) block.getState();
    }

    public static float normalizeAngle(float angle) {
        while (angle > 180.0) {
            angle -= 360.0;
        }
        while (angle <= -180.0) {
            angle += 360.0;
        }
        return angle;
    }

    public static BlockFace yawToFace(float yaw, boolean useSubCardinalDirections) {
        yaw = normalizeAngle(yaw);
        if (useSubCardinalDirections) {
            switch ((int) yaw) {
                case 0:
                    return BlockFace.NORTH;
                case 45:
                    return BlockFace.NORTH_EAST;
                case 90:
                    return BlockFace.EAST;
                case 135:
                    return BlockFace.SOUTH_EAST;
                case 180:
                    return BlockFace.SOUTH;
                case 225:
                    return BlockFace.SOUTH_WEST;
                case 270:
                    return BlockFace.WEST;
                case 315:
                    return BlockFace.NORTH_WEST;
            }
            //Let's apply angle differences
            if (yaw >= -22.5 && yaw < 22.5) {
                return BlockFace.NORTH;
            } else if (yaw >= 22.5 && yaw < 67.5) {
                return BlockFace.NORTH_EAST;
            } else if (yaw >= 67.5 && yaw < 112.5) {
                return BlockFace.EAST;
            } else if (yaw >= 112.5 && yaw < 157.5) {
                return BlockFace.SOUTH_EAST;
            } else if (yaw >= -67.5 && yaw < -22.5) {
                return BlockFace.NORTH_WEST;
            } else if (yaw >= -112.5 && yaw < -67.5) {
                return BlockFace.WEST;
            } else if (yaw >= -157.5 && yaw < -112.5) {
                return BlockFace.SOUTH_WEST;
            } else {
                return BlockFace.SOUTH;
            }
        } else {
            switch ((int) yaw) {
                case 0:
                    return BlockFace.NORTH;
                case 90:
                    return BlockFace.EAST;
                case 180:
                    return BlockFace.SOUTH;
                case 270:
                    return BlockFace.WEST;
            }
            //Let's apply angle differences
            if (yaw >= -45 && yaw < 45) {
                return BlockFace.NORTH;
            } else if (yaw >= 45 && yaw < 135) {
                return BlockFace.EAST;
            } else if (yaw >= -135 && yaw < -45) {
                return BlockFace.WEST;
            } else {
                return BlockFace.SOUTH;
            }
        }
    }
    public static void spawnRandomRocket(Location location) {
        World world = location.getWorld();
        if (world == null) {
            return;
        }

        // Generate random colors for the rocket
        Color baseColor = Color.fromRGB((int) (Math.random() * 255), (int) (Math.random() * 255), (int) (Math.random() * 255));
        Color fadeColor = Color.fromRGB((int) (Math.random() * 255), (int) (Math.random() * 255), (int) (Math.random() * 255));

        // Create the rocket firework
        Firework firework = (Firework) world.spawnEntity(location, EntityType.FIREWORK);
        FireworkMeta fireworkMeta = firework.getFireworkMeta();
        fireworkMeta.setPower(1);
        fireworkMeta.addEffect(FireworkEffect.builder()
                .with(FireworkEffect.Type.BALL_LARGE)
                .withColor(baseColor)
                .withFade(fadeColor)
                .withFlicker()
                .withTrail()
                .build());
        firework.setFireworkMeta(fireworkMeta);
    }
}
