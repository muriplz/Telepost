//package muriplz.kryeittpplugin.commands;
//
//import muriplz.kryeittpplugin.KryeitTPPlugin;
//import org.bukkit.Bukkit;
//import org.bukkit.Location;
//import org.bukkit.Material;
//import org.bukkit.World;
//import org.bukkit.block.Block;
//import org.bukkit.block.BlockFace;
//import org.bukkit.block.Sign;
//import org.bukkit.command.Command;
//import org.bukkit.command.CommandExecutor;
//import org.bukkit.command.CommandSender;
//import org.bukkit.entity.Player;
//import org.jetbrains.annotations.NotNull;
//
//public class BuildPostCommand implements CommandExecutor {
//
//    private final KryeitTPPlugin plugin;
//
//    public BuildPostCommand(KryeitTPPlugin plugin) {
//        this.plugin = plugin;
//    }
//
//    // SetBlock function
//    // Basically does the same thing as the /setblock command
//    // Only you can't give nbt data
//    public static void SetBlock(@NotNull Material material, @NotNull Block block) {
//        block.setType(material);
//    }
//
//    public static void setSignFacing(@NotNull Block b, @NotNull BlockFace face) {
//        // Checking if the block is a sign in general
//        if(!(b.getState() instanceof Sign)) { return; }
//
//        // Getting the sign
//        Sign sign = (Sign) b.getState();
//
//        // Checking if the block is a wall sign
//        if (sign.getBlockData() instanceof org.bukkit.block.data.type.WallSign) {
//            // Getting the wall signs data
//            org.bukkit.block.data.type.WallSign signData = (org.bukkit.block.data.type.WallSign) sign.getBlockData();
//
//            // Setting the facing direction
//            signData.setFacing(face);
//            sign.setBlockData(signData);
//
//            // Setting the blocks data
//            b.setBlockData(sign.getBlockData());
//
//            // Updating the wall sign
//            sign.update();
//        }
//
//        // Checking if the block is a sign
//        if (sign.getBlockData() instanceof org.bukkit.block.data.type.Sign) {
//            // Getting the signs data
//            org.bukkit.block.data.type.Sign signData = (org.bukkit.block.data.type.Sign) sign.getBlockData();
//
//            // Setting the rotation
//            signData.setRotation(face);
//            sign.setBlockData(signData);
//
//            // Setting the blocks data
//            b.setBlockData(sign.getBlockData());
//
//            // Updating the sign
//            sign.update();
//        }
//    }
//
//    public boolean buildPost(int X, int Y, int Z, int width) {
//        // Returning if the Y coord is too big
//        if (Y > 251) {
//            Bukkit.getConsoleSender().sendMessage(plugin.name+"A post has been tried to be built way too high!");
//            return false; }
//
//        // Getting the world
//        World world = Bukkit.getServer().getWorld("world");
//        // Getting the block at the specified coords
//        Location blockloc = new Location(world, X, Y, Z);
//        Block block = blockloc.getBlock();
//
//        // Creating a variable to not repeat getting the length
//        // From the center of the post
//        int lengthFromCenter = (width-1)/2;
//
//        // Loading the necessary chunks
//        PostAPI.loadAllChunksToBuildThePost(block, width);
//
//        // Clearing the post area
//        for (int x = X-lengthFromCenter; x < X+lengthFromCenter+1; x++){
//            for (int y = Y+1; y < Y+5; y++){
//                for (int z = Z-lengthFromCenter; z < Z+lengthFromCenter+1; z++){
//                    Location blockloc1 = new Location(world, x, y, z);
//                    Block block1 = blockloc1.getBlock();
//                    SetBlock(Material.AIR, block1);
//                }
//            }
//        }
//
//        // Setting the base and pillar blocks
//        Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "fill " + (X-lengthFromCenter) + " " + Y + " " + (Z-lengthFromCenter) + " " + (X+lengthFromCenter) + " " + Y + " " + (Z+lengthFromCenter) + " minecraft:stone_bricks");
//        Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "fill " + X + " " + Y + " " + Z + " " + X + " " + (Y+2) + " " + Z + " minecraft:glowstone");
//
//        // Getting and setting the block for the top (light source)
//        Location blockloc1 = new Location(world, X, Y + 3, Z);
//        Block light = blockloc1.getBlock();
//        SetBlock(Material.GLOWSTONE, light);
//
//        // Getting the block where the wall signs should stay on
//        Block signpoint = light.getRelative(BlockFace.DOWN);
//
//        // Setting the sign on the north side of the post
//        SetBlock(Material.OAK_WALL_SIGN, signpoint.getRelative(BlockFace.NORTH));
//        setSignFacing(signpoint.getRelative(BlockFace.NORTH), BlockFace.NORTH);
//        Sign sign = (Sign) signpoint.getRelative(BlockFace.NORTH).getState();
//        sign.setLine(0, "Use /posthelp");
//        sign.setLine(1, "to get more");
//        sign.setLine(2, "info about");
//        sign.setLine(3, "the posts.");
//        sign.update();
//
//        // Setting the sign on the east side of the post
//        SetBlock(Material.OAK_WALL_SIGN, signpoint.getRelative(BlockFace.EAST));
//        setSignFacing(signpoint.getRelative(BlockFace.EAST), BlockFace.EAST);
//        Sign sign1 = (Sign) signpoint.getRelative(BlockFace.EAST).getState();
//        sign1.setLine(0, "Use /visit");
//        sign1.setLine(1, "<Postname>");
//        sign1.setLine(2, "to teleport to");
//        sign1.setLine(3, "named posts");
//        sign1.update();
//
//        // Setting the sign on the south side of the post
//        SetBlock(Material.OAK_WALL_SIGN, signpoint.getRelative(BlockFace.SOUTH));
//        setSignFacing(signpoint.getRelative(BlockFace.SOUTH), BlockFace.SOUTH);
//        Sign sign2 = (Sign) signpoint.getRelative(BlockFace.SOUTH).getState();
//        sign2.setLine(0, "Do /setpost");
//        sign2.setLine(1, "to make the");
//        sign2.setLine(2, "nearest post");
//        sign2.setLine(3, "your /homepost");
//        sign2.update();
//
//        // Setting the sign on the west side of the post
//        SetBlock(Material.OAK_WALL_SIGN, signpoint.getRelative(BlockFace.WEST));
//        setSignFacing(signpoint.getRelative(BlockFace.WEST), BlockFace.WEST);
//        Sign sign3 = (Sign) signpoint.getRelative(BlockFace.WEST).getState();
//        sign3.setLine(0, "Do /invite");
//        sign3.setLine(1, "<Player> to invite");
//        sign3.setLine(2, "players to");
//        sign3.setLine(3, "your post");
//        sign3.update();
//
//        // Setting the top sign
//        SetBlock(Material.OAK_SIGN, light.getRelative(BlockFace.UP));
//        setSignFacing(light.getRelative(BlockFace.UP), BlockFace.NORTH_WEST);
//        Sign sign4 = (Sign) light.getRelative(BlockFace.UP).getState();
//        sign4.setLine(0, "Current");
//        sign4.setLine(1, "location:");
//        sign4.setLine(3, "Nameless");
//        sign4.update();
//
//        // Unloading the necessary chunks
//        PostAPI.unloadAllChunksToBuildThePost(block, width);
//
//        return true;
//    }
//
//    //  This commands aims to be /BuildPost in-game
//    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
//
//        if (command.getLabel().equalsIgnoreCase("buildpost")) {
//
//            // Checking if the command is executed from console
//            if( ! ( sender instanceof Player )) {
//                Bukkit.getConsoleSender().sendMessage(plugin.name + "You can't execute this command from console.");
//                return false;
//            }
//
//            // Getting the Player
//            Player player = (Player) sender;
//
//            // Don't build posts on other worlds, TP system only works for one dimension (TODO= change "world" for a variable from config "world-folder-name")
//            if(!player.getWorld().getName().equals("world")){
//                PostAPI.sendMessage(player,"&cYou have to be in the Overworld to use this command.");
//                return false;
//            }
//
//            // Setting the temporary coords
//            int X = 0;
//            int Y = 0;
//            int Z = 0;
//
//            // Setting condition triggers
//            boolean xzUnknown = false;
//            boolean yUnknown = false;
//
//            if (args.length <= 1) {
//
//                // Permission node for /buildpost
//                if(!player.hasPermission("telepost.buildpost")){
//                    PostAPI.sendMessage(player,"&cYou don't have permission to use this command.");
//                    return false;
//                }
//
//                // Getting x coords
//                int originX = plugin.getConfig().getInt("post-x-location");
//                X = PostAPI.getNearPost(plugin.getConfig().getInt("distance-between-posts"), player.getLocation().getBlockX(), originX);
//
//                // Getting z coords
//                int originZ = plugin.getConfig().getInt("post-z-location");
//                Z = PostAPI.getNearPost(plugin.getConfig().getInt("distance-between-posts"), player.getLocation().getBlockZ(), originZ);
//            } else { xzUnknown = true; }
//            if (args.length == 0 || args.length == 2) {
//                // Get y coords
//                Y = PostAPI.getFirstSolidBlockHeight(X, Z);
//            } else { yUnknown = true; }
//
//            // If xyz are unknown
//            if (xzUnknown && yUnknown) {
//                X = Integer.parseInt(args[0]);
//                Y = Integer.parseInt(args[1]);
//                Z = Integer.parseInt(args[2]);
//            } else if (yUnknown) {
//                // If y is unknown
//                Y = Integer.parseInt(args[0]);
//            } else if (xzUnknown) {
//                // If xz are unknown
//                X = Integer.parseInt(args[0]);
//                Z = Integer.parseInt(args[1]);
//            }
//
//            // Building the post and getting success boolean
//            boolean success = buildPost(X, Y, Z, plugin.getConfig().getInt("post-width"));
//
//            // Sending a message that the post has successfully been built.
//            if (success) {
//                PostAPI.sendMessage(player, "&fYou have built the post at &6(" + X + " , " + Z + ")&f.");
//            } else {
//                PostAPI.sendMessage(player, "&cYou have tried to built the post too high! Max height is 251");
//            }
//        }
//        return true;
//    }
//}
//