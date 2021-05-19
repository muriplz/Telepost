package muriplz.kryeittpplugin.commands;

import muriplz.kryeittpplugin.KryeitTPPlugin;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class BuildPostCommand implements CommandExecutor {

    private final KryeitTPPlugin plugin;

    public BuildPostCommand(KryeitTPPlugin plugin) {
        this.plugin = plugin;
    }

    // SetBlock function
    // Basically does the same thing as the /setblock command
    // Only you can't give nbt data
    public static void SetBlock(@NotNull Material material, @NotNull Block block) { block.setType(material); }

    public static void setSignFacing(@NotNull Block b, @NotNull BlockFace face) {
        // Checking if the block is a sign in general
        if(!(b.getState() instanceof Sign)) { return; }

        // Getting the sign
        Sign sign = (Sign) b.getState();

        // Checking if the block is a wall sign
        if (sign.getBlockData() instanceof org.bukkit.block.data.type.WallSign) {
            // Getting the wall signs data
            org.bukkit.block.data.type.WallSign signData = (org.bukkit.block.data.type.WallSign) sign.getBlockData();

            // Setting the facing direction
            signData.setFacing(face);
            sign.setBlockData(signData);

            // Setting the blocks data
            b.setBlockData(sign.getBlockData());

            // Updating the wall sign
            sign.update();
        }

        // Checking if the block is a sign
        if (sign.getBlockData() instanceof org.bukkit.block.data.type.Sign) {
            // Getting the signs data
            org.bukkit.block.data.type.Sign signData = (org.bukkit.block.data.type.Sign) sign.getBlockData();

            // Setting the rotation
            signData.setRotation(face);
            sign.setBlockData(signData);

            // Setting the blocks data
            b.setBlockData(sign.getBlockData());

            // Updating the sign
            sign.update();
        }
    }

    public static int getFirstSolidBlockHeight(int X, int Z){
        // Setting the highest height the post can be at
        int height = 251;

        // Looping down and searching for a solid block or water or lava
        while (true){
            Location l = new Location(Bukkit.getWorld("world"), X, height, Z);
            if(l.getBlock().getType().isSolid() || l.getBlock().getType().equals(Material.WATER) || l.getBlock().getType().equals(Material.LAVA)){
                break;
            }
            height--;
        }
        return height;
    }

    public static void buildPost(int X, int Y, int Z) {
        if (Y > 251) { return; }

        // Getting the world
        World world = Bukkit.getServer().getWorld("world");
        // Getting the block at the specified coords
        Location blockloc = new Location(world, X, Y, Z);
        Block block = blockloc.getBlock();

        // Loading the chunk
        // This will be replaced by loading all needed chunks for the post
        block.getChunk().load();

        // Clearing the post area
        for (int x=X-2;x<X+3;x++){
            for (int y=Y+1;y<Y+5;y++){
                for (int z=Z-2;z<Z+3;z++){
                    Location blockloc1 = new Location(world, x, y, z);
                    Block block1 = blockloc1.getBlock();
                    SetBlock(Material.AIR, block1);
                }
            }
        }

        // Setting the modded blocks
        Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "fill " + (X-2) + " " + Y + " " + (Z-2) + " " + (X+2) + " " + Y + " " + (Z+2) + " create:chiseled_gabbro");
        Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "fill " + X + " " + Y + " " + Z + " " + X + " " + (Y+2) + " " + Z + " create:brass_casing");

        // Getting and setting the block for the glowstone
        Location blockloc1 = new Location(world, X, Y + 3, Z);
        Block glowstone = blockloc1.getBlock();
        SetBlock(Material.GLOWSTONE, glowstone);

        // Getting the block where the wall signs should stay on
        Block signpoint = glowstone.getRelative(BlockFace.DOWN);

        // Setting the sign on the north side of the post
        SetBlock(Material.OAK_WALL_SIGN, signpoint.getRelative(BlockFace.NORTH));
        setSignFacing(signpoint.getRelative(BlockFace.NORTH), BlockFace.NORTH);
        Sign sign = (Sign) signpoint.getRelative(BlockFace.NORTH).getState();
        sign.setLine(0, "Do /visit");
        sign.setLine(1, "<Player> to visit");
        sign.setLine(2, "a player that");
        sign.setLine(3, "invited you");
        sign.update();

        // Setting the sign on the east side of the post
        SetBlock(Material.OAK_WALL_SIGN, signpoint.getRelative(BlockFace.EAST));
        setSignFacing(signpoint.getRelative(BlockFace.EAST), BlockFace.EAST);
        Sign sign1 = (Sign) signpoint.getRelative(BlockFace.EAST).getState();
        sign1.setLine(0, "Use /visit");
        sign1.setLine(1, "<Postname>");
        sign1.setLine(2, "to teleport to");
        sign1.setLine(3, "named posts");
        sign1.update();

        // Setting the sign on the south side of the post
        SetBlock(Material.OAK_WALL_SIGN, signpoint.getRelative(BlockFace.SOUTH));
        setSignFacing(signpoint.getRelative(BlockFace.SOUTH), BlockFace.SOUTH);
        Sign sign2 = (Sign) signpoint.getRelative(BlockFace.SOUTH).getState();
        sign2.setLine(0, "Do /setpost");
        sign2.setLine(1, "to make the");
        sign2.setLine(2, "nearest post");
        sign2.setLine(3, "your /homepost");
        sign2.update();

        // Setting the sign on the west side of the post
        SetBlock(Material.OAK_WALL_SIGN, signpoint.getRelative(BlockFace.WEST));
        setSignFacing(signpoint.getRelative(BlockFace.WEST), BlockFace.WEST);
        Sign sign3 = (Sign) signpoint.getRelative(BlockFace.WEST).getState();
        sign3.setLine(0, "Do /invite");
        sign3.setLine(1, "<Player> to invite");
        sign3.setLine(2, "players to");
        sign3.setLine(3, "your post");
        sign3.update();

        // Setting the top sign
        SetBlock(Material.OAK_SIGN, glowstone.getRelative(BlockFace.UP));
        setSignFacing(glowstone.getRelative(BlockFace.UP), BlockFace.NORTH_WEST);
        Sign sign4 = (Sign) glowstone.getRelative(BlockFace.UP).getState();
        sign4.setLine(0, "Current");
        sign4.setLine(1, "location:");
        sign4.setLine(3, "Nameless");
        sign4.update();

        // Unloading the chunk
        // This will be replaced by unloading all needed chunks for the post
        block.getChunk().unload();
    }

    //  This commands aims to be /BuildPost in-game
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (command.getLabel().equalsIgnoreCase("buildpost")) {
            // Setting the temporary coords
            int X = 0;
            int Y = 0;
            int Z = 0;

            // Setting condition triggers
            boolean xzUnknown = false;
            boolean yUnknown = false;

            if (args.length <= 1) {
                // Checking if the command is executed from console
                if( ! ( sender instanceof Player )) {
                    Bukkit.getConsoleSender().sendMessage(plugin.name + "You cant execute this command from console.");
                    return false;
                } else {
                    // Getting the player
                    Player player = (Player) sender;

                    // Permission node for /buildpost
                    if(!player.hasPermission("telepost.buildpost")){
                        player.sendMessage(ChatColor.RED+"You don't have permission to use this command.");
                    }

                    // Getting x coords
                    int originX = plugin.getConfig().getInt("post-x-location");
                    X = PostAPI.getNearPost(plugin.getConfig().getInt("distance-between-posts"), player.getLocation().getBlockX(), originX);

                    // Getting z coords
                    int originZ = plugin.getConfig().getInt("post-z-location");
                    Z = PostAPI.getNearPost(plugin.getConfig().getInt("distance-between-posts"), player.getLocation().getBlockZ(), originZ);
                }
            } else { xzUnknown = true; }
            if (args.length == 0 || args.length == 2) {
                // Get y coords
                Y = getFirstSolidBlockHeight(X, Z);
            } else { yUnknown = true; }

            // If xyz are unknown
            if (xzUnknown && yUnknown) {
                X = Integer.parseInt(args[0]);
                Y = Integer.parseInt(args[1]);
                Z = Integer.parseInt(args[2]);
            } else if (yUnknown) {
                // If y is unknown
                Y = Integer.parseInt(args[0]);
            } else if (xzUnknown) {
                // If xz are unknown
                X = Integer.parseInt(args[0]);
                Z = Integer.parseInt(args[1]);
            }

            // Building the post
            buildPost(X, Y, Z);
        }
        return true;
    }
}
