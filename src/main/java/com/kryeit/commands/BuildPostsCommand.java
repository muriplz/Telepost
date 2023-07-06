package com.kryeit.commands;

import com.github.shynixn.structureblocklib.api.bukkit.StructureBlockLibApi;
import com.kryeit.Telepost;
import com.kryeit.compat.CompatAddon;
import com.kryeit.util.BlockFinder;
import com.kryeit.util.GridIterator;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Level;

import static com.kryeit.commands.PostAPI.WORLD;
import static com.kryeit.commands.PostAPI.instance;

public class BuildPostsCommand implements CommandExecutor {

    private BukkitTask buildTask;
    private int alreadyBuilt = 0;
    int width = (Telepost.getInstance().getConfig().getInt("post-width") - 1) / 2;

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Telepost instance = Telepost.getInstance();


        if (!(sender instanceof Player player)) {
            Bukkit.getConsoleSender().sendMessage(instance.name + PostAPI.getMessage("cant-execute-from-console"));
        } else {

            if(!CompatAddon.STRUCTURE_BLOCK_LIB.isLoaded()) {
                player.sendMessage("You need to use StructureBlockLib plugin too!, shutting down command Zzz");
                return false;
            }

            GridIterator gridIterator = new GridIterator();
            buildTask = Bukkit.getScheduler().runTaskTimer(instance, new Runnable() {
                @Override
                public void run() {
                    if (gridIterator.hasNext()) {
                        Location loc = gridIterator.next();
                        BlockFinder.clearArea(loc,width);
                        alreadyBuilt++;
                        executeStructure(player, loc, alreadyBuilt);

                    } else {
                        buildTask.cancel(); // Stop the task when there are no more locations
                    }
                }
            }, 0L, 20L);
        }
        return false;
    }

    private void executeStructure(Player player, Location loc, int alreadyBuilt) {
        player.sendMessage("Built post " + alreadyBuilt + " at location " + loc.getBlockX() + ", " + loc.getBlockY() + ", " + loc.getBlockZ());

        Location signlocation = new Location(WORLD,loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
        Bukkit.broadcastMessage(signlocation.getBlock().getType()+"");
        Location newlocation = new Location(WORLD,loc.getBlockX() - width, loc.getBlockY(), loc.getBlockZ() - width);
        StructureBlockLibApi.INSTANCE
                .loadStructure(instance)
                .at(newlocation)
                .includeEntities(true)
                .loadFromWorld("world","minecraft", "default")
                .onException(e -> instance.getLogger().log(Level.SEVERE, "Failed to load structure.", e))
                .onResult(e -> instance.getLogger().log(Level.INFO, ChatColor.GREEN + "Loaded structure 'mystructure'."));
    }
}
