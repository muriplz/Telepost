package com.kryeit.commands;

import com.kryeit.Telepost;
import com.kryeit.util.GridIterator;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

public class BuildPostsCommand implements CommandExecutor {

    private BukkitTask buildTask;


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Telepost instance = Telepost.getInstance();


        if (!(sender instanceof Player player)) {
            Bukkit.getConsoleSender().sendMessage(instance.name + PostAPI.getMessage("cant-execute-from-console"));
        } else {

            int i = 0;
            GridIterator gridIterator = new GridIterator();
            buildTask = Bukkit.getScheduler().runTaskTimer(instance, new Runnable() {
                @Override
                public void run() {
                    if (gridIterator.hasNext()) {
                        Location loc = gridIterator.next();
                        // Assuming executeStructure is a method that executes your structure command
                        executeStructure(loc);
                    } else {
                        buildTask.cancel(); // Stop the task when there are no more locations
                    }
                }
            }, 0L, 20L);
        }
        return false;
    }

    private void executeStructure(Location loc) {
        // Your structure command here. For example:
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "setblock " + loc.getBlockX() + " " + loc.getBlockY() + " " + loc.getBlockZ() + " minecraft:structure_block[mode=load]{name:\"default\",posX:0,posY:0,posZ:0,ignoreEntities:1b,showboundingbox:0b}");
    }
}
