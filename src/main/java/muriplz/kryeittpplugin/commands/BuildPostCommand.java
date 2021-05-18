package muriplz.kryeittpplugin.commands;

import muriplz.kryeittpplugin.KryeitTPPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
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

    public static void SetBlock(Material material, @NotNull Block block) { block.setType(material); }

    public static boolean setSignFacing(Block b, BlockFace face) {
        if(!(b.getState() instanceof org.bukkit.block.Sign)) { return false; }
        org.bukkit.block.Sign sign = (org.bukkit.block.Sign) b.getState();
        if (sign.getBlockData() instanceof org.bukkit.block.data.type.WallSign) {
            org.bukkit.block.data.type.WallSign signData = (org.bukkit.block.data.type.WallSign) sign.getBlockData();
            signData.setFacing(face);
            sign.setBlockData(signData);
            b.setBlockData(sign.getBlockData());
            sign.update();
        }
        if (sign.getBlockData() instanceof org.bukkit.block.data.type.Sign) {
            org.bukkit.block.data.type.Sign signData = (org.bukkit.block.data.type.Sign) sign.getBlockData();
            signData.setRotation(face);
            sign.setBlockData(signData);
            b.setBlockData(sign.getBlockData());
            sign.update();
        }
        return true;
    }

    public static int getFirstSolidBlockHeight(int X, int Z){
        int height = 251;
        while (true){
            Location l = new Location(Bukkit.getWorld("world"), X, height, Z);
            if(l.getBlock().getType().isSolid() || l.getBlock().getType().equals(Material.WATER)){
                break;
            }
            height--;
        }
        return height;
    }

    public static boolean buildPost(int X, int Y, int Z) {
        if (Y > 251) { return false; }
        World world = Bukkit.getServer().getWorld("world");
        Location blockloc = new Location(world, X, Y, Z);
        Block block = blockloc.getBlock();
        block.getChunk().load();
        for (int x=X-2;x<X+3;x++){
            for (int y=Y+1;y<Y+5;y++){
                for (int z=Z-2;z<Z+3;z++){
                    Location blockloc1 = new Location(world, x, y, z);
                    Block block1 = blockloc1.getBlock();
                    SetBlock(Material.AIR, block1);
                }
            }
        }
        Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "fill " + (X-2) + " " + Y + " " + (Z-2) + " " + (X+2) + " " + Y + " " + (Z+2) + " create:chiseled_gabbro");
        Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "fill " + X + " " + Y + " " + Z + " " + X + " " + (Y+2) + " " + Z + " create:brass_casing");
        Location blockloc1 = new Location(world, X, Y + 3, Z);
        Block glowstone = blockloc1.getBlock();
        SetBlock(Material.GLOWSTONE, glowstone);

        Block signpoint = glowstone.getRelative(BlockFace.DOWN);

        SetBlock(Material.OAK_WALL_SIGN, signpoint.getRelative(BlockFace.NORTH));
        setSignFacing(signpoint.getRelative(BlockFace.NORTH), BlockFace.NORTH);
        Sign sign = (Sign) signpoint.getRelative(BlockFace.NORTH).getState();
        sign.setLine(0, "Do /visit");
        sign.setLine(1, "<Player> to visit");
        sign.setLine(2, "a player that");
        sign.setLine(3, "invited you");
        sign.update();

        SetBlock(Material.OAK_WALL_SIGN, signpoint.getRelative(BlockFace.EAST));
        setSignFacing(signpoint.getRelative(BlockFace.EAST), BlockFace.EAST);
        Sign sign1 = (Sign) signpoint.getRelative(BlockFace.EAST).getState();
        sign1.setLine(0, "Use /visit");
        sign1.setLine(1, "<Postname>");
        sign1.setLine(2, "to teleport to");
        sign1.setLine(3, "named posts");
        sign1.update();

        SetBlock(Material.OAK_WALL_SIGN, signpoint.getRelative(BlockFace.SOUTH));
        setSignFacing(signpoint.getRelative(BlockFace.SOUTH), BlockFace.SOUTH);
        Sign sign2 = (Sign) signpoint.getRelative(BlockFace.SOUTH).getState();
        sign2.setLine(0, "Do /setpost");
        sign2.setLine(1, "to make the");
        sign2.setLine(2, "nearest post");
        sign2.setLine(3, "your /homepost");
        sign2.update();

        SetBlock(Material.OAK_WALL_SIGN, signpoint.getRelative(BlockFace.WEST));
        setSignFacing(signpoint.getRelative(BlockFace.WEST), BlockFace.WEST);
        Sign sign3 = (Sign) signpoint.getRelative(BlockFace.WEST).getState();
        sign3.setLine(0, "Do /invite");
        sign3.setLine(1, "<Player> to invite");
        sign3.setLine(2, "players to");
        sign3.setLine(3, "your post");
        sign3.update();

        SetBlock(Material.OAK_SIGN, glowstone.getRelative(BlockFace.UP));
        setSignFacing(glowstone.getRelative(BlockFace.UP), BlockFace.NORTH_WEST);
        Sign sign4 = (Sign) glowstone.getRelative(BlockFace.UP).getState();
        sign4.setLine(0, "Current");
        sign4.setLine(1, "location:");
        sign4.setLine(3, "Nameless");
        sign4.update();

        block.getChunk().unload();

        return true;
    }

    //  This commands aims to be /BuildPost in-game
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (command.getLabel().equalsIgnoreCase("buildpost")) {
            Player player = (Player) sender;
            if (args.length == 0) {
                if( ! ( sender instanceof Player )) {
                    Bukkit.getConsoleSender().sendMessage(plugin.name+"You cant execute this command from console.");
                    return false;
                } else {
                    int originX = plugin.getConfig().getInt("post-x-location");
                    int nearestX = PostAPI.getNearPost(plugin.getConfig().getInt("distance-between-posts"), player.getLocation().getBlockX()-originX, originX);
                    int originZ = plugin.getConfig().getInt("post-z-location");
                    int nearestZ = PostAPI.getNearPost(plugin.getConfig().getInt("distance-between-posts"), player.getLocation().getBlockZ()-originZ, originZ);
                    buildPost(nearestX, getFirstSolidBlockHeight(nearestX, nearestZ), nearestZ);
                }
            }
            if (args.length == 1) {
                if( ! ( sender instanceof Player )) {
                    Bukkit.getConsoleSender().sendMessage(plugin.name+"You cant execute this command from console.");
                    return false;
                } else {
                    int originX = plugin.getConfig().getInt("post-x-location");
                    int nearestX = PostAPI.getNearPost(plugin.getConfig().getInt("distance-between-posts"), player.getLocation().getBlockX()-originX, originX);
                    int originZ = plugin.getConfig().getInt("post-z-location");
                    int nearestZ = PostAPI.getNearPost(plugin.getConfig().getInt("distance-between-posts"), player.getLocation().getBlockZ()-originZ, originZ);
                    buildPost(nearestX, Integer.parseInt(args[0]), nearestZ);
                }
            }
            if (args.length == 2) {
                int X = Integer.parseInt(args[0]);
                int Z = Integer.parseInt(args[1]);
                buildPost(X, getFirstSolidBlockHeight(X, Z), Z);
            }
            if (args.length == 3) {
                int X = Integer.parseInt(args[0]);
                int Z = Integer.parseInt(args[1]);
                int Y = Integer.parseInt(args[2]);
                buildPost(X, Y, Z);
            }
        }
        return true;
    }
}
