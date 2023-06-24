package com.kryeit.commands;

import com.griefdefender.api.claim.Claim;
import com.griefdefender.api.claim.ClaimGroup;
import com.griefdefender.api.claim.ClaimResult;
import com.griefdefender.api.claim.ClaimTypes;
import com.griefdefender.api.data.ClaimData;
import com.griefdefender.lib.flowpowered.math.vector.Vector3i;
import com.griefdefender.lib.kyori.adventure.text.Component;
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

import static com.kryeit.commands.PostAPI.WORLD;

public class ClaimPostsCommand implements CommandExecutor {

    private BukkitTask buildTask;
    int i = 0;


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Telepost instance = Telepost.getInstance();
        int width = (Telepost.getInstance().getConfig().getInt("post-width") - 1) / 2;

        ClaimGroup claimGroup = ClaimGroup.builder().description(Component.text("Post claims")).name("Posts").build();


        if (!(sender instanceof Player player)) {
            Bukkit.getConsoleSender().sendMessage(instance.name + PostAPI.getMessage("cant-execute-from-console"));
        } else {

            GridIterator gridIterator = new GridIterator();

            buildTask = Bukkit.getScheduler().runTaskTimer(instance, new Runnable() {
                @Override
                public void run() {
                    if (gridIterator.hasNext()) {
                        Location loc = gridIterator.next();
                        i++;
                        player.sendMessage("Claimed post " + i + " at location " + loc.getBlockX() + ", " + loc.getBlockY() + ", " + loc.getBlockZ());

                        makeClaim(loc, width, claimGroup);
                    } else {
                        buildTask.cancel(); // Stop the task when there are no more locations
                    }
                }
            }, 0L, 5L);
            player.sendMessage("Done! All posts claimed. Total posts claimed: " + i);
        }

        return false;
    }

    public void makeClaim(Location location, int width, ClaimGroup claimGroup) {
        // Calculate the corners of the claim
        Vector3i lowerCorner = new Vector3i(location.getBlockX() - width, location.getBlockY() - 6, location.getBlockZ() - width);
        Vector3i upperCorner = new Vector3i(location.getBlockX() + width, 319, location.getBlockZ() + width); // Set 255 as max Y value for the upper corner

        // Create the claim
        ClaimResult claimResult = Claim.builder()
                .bounds(lowerCorner, upperCorner)
                .world(WORLD.getUID())
                .cuboid(true)
                .type(ClaimTypes.ADMIN)
                .build();
        if(claimResult.getClaim() == null) return;
        // Set the claim group
        ClaimData claimData = claimResult.getClaim().getData();
        claimData.setClaimGroupUniqueId(claimGroup.getUniqueId());
    }
}
