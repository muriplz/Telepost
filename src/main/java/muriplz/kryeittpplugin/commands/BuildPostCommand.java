package muriplz.kryeittpplugin.commands;

import muriplz.kryeittpplugin.KryeitTPPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class BuildPostCommand implements CommandExecutor {

    private final KryeitTPPlugin plugin;

    public BuildPostCommand(KryeitTPPlugin plugin) {
        this.plugin = plugin;
    }

    public static void SetBlock(Material material, Block block) { block.setType(material); }

    //  This commands aims to be /BuildPost in-game
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (command.getLabel().equalsIgnoreCase("buildpost")) {
            if (args.length > 1) {
                int X = Integer.parseInt(args[0]);
                int Z = Integer.parseInt(args[1]);
                int Y = Integer.parseInt(args[2]);
                Block block = Bukkit.getWorld("world").getBlockAt(X, Y, Z);
                block.getChunk().load();
                for (int x=X-2;x<X+3;x++){
                    for (int y=Y;y<Y+5;y++){
                        for (int z=Z-2;z<Z+3;z++){
                            SetBlock(Material.AIR, Bukkit.getWorld("world").getBlockAt(x, y, z));
                        }
                    }
                }
                block.getChunk().unload();
            }
        }

        return true;
    }
}
