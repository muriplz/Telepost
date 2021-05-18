package muriplz.kryeittpplugin.commands;

import muriplz.kryeittpplugin.KryeitTPPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.material.Sign;
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

//    public static boolean getFirstSolidBlockHeight(int X,int Z,int height){
//        if (height > 251) { return false; }
//        height = 251;
//        while (true){
//            Location l = new Location(Bukkit.getWorld("world"), X, height, Z);
//            if(l.getBlock().getType().isSolid()){
//                break;
//            }
//            height--;
//        }
//        return true;
//    }

    //  This commands aims to be /BuildPost in-game
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (command.getLabel().equalsIgnoreCase("buildpost")) {
            if (args.length > 2) {
                Player player = (Player) sender;
                int X = Integer.parseInt(args[0]);
                int Z = Integer.parseInt(args[1]);
                int Y = Integer.parseInt(args[2]);
                World world = Bukkit.getServer().getWorld("world");
                Location blockloc = new Location(world, X, Y, Z);
                Block block = blockloc.getBlock();
                block.getChunk().load();
                for (int x=X-2;x<X+3;x++){
                    for (int y=Y;y<Y+5;y++){
                        for (int z=Z-2;z<Z+3;z++){
                            Location blockloc1 = new Location(world, x, y, z);
                            Block block1 = blockloc1.getBlock();
                            SetBlock(Material.AIR, block1);
                        }
                    }
                }
                Location blockloc1 = new Location(world, X, Y + 3, Z);
                Block glowstone = blockloc1.getBlock();
                SetBlock(Material.GLOWSTONE, glowstone);

                SetBlock(Material.OAK_WALL_SIGN, glowstone.getRelative(BlockFace.NORTH));
                ((Sign)glowstone.getRelative(BlockFace.NORTH).getState().getData()).setFacingDirection(BlockFace.NORTH);
                glowstone.getRelative(BlockFace.NORTH).getState().update();

                SetBlock(Material.OAK_WALL_SIGN, glowstone.getRelative(BlockFace.EAST));
                ((Sign)glowstone.getRelative(BlockFace.EAST).getState().getData()).setFacingDirection(BlockFace.EAST);
                glowstone.getRelative(BlockFace.EAST).getState().update();

                SetBlock(Material.OAK_WALL_SIGN, glowstone.getRelative(BlockFace.SOUTH));
                ((Sign)glowstone.getRelative(BlockFace.SOUTH).getState().getData()).setFacingDirection(BlockFace.SOUTH);
                glowstone.getRelative(BlockFace.SOUTH).getState().update();

                SetBlock(Material.OAK_WALL_SIGN, glowstone.getRelative(BlockFace.WEST));
                ((Sign)glowstone.getRelative(BlockFace.WEST).getState().getData()).setFacingDirection(BlockFace.WEST);
                glowstone.getRelative(BlockFace.WEST).getState().update();

                SetBlock(Material.OAK_SIGN, glowstone.getRelative(BlockFace.UP));
                ((Sign)glowstone.getRelative(BlockFace.UP).getState().getData()).setFacingDirection(BlockFace.NORTH_WEST);
//                Sign sign4 = (Sign)glowstone.getRelative(BlockFace.UP);
//                sign4.setFacingDirection(BlockFace.NORTH_WEST);
                glowstone.getRelative(BlockFace.UP).getState().update();

                block.getChunk().unload();
            }
        }

        return true;
    }
}
