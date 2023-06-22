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
import org.jetbrains.annotations.NotNull;

import static com.kryeit.commands.PostAPI.WORLD;
import static com.kryeit.commands.PostAPI.WORLDBORDER_RADIUS;

public class ClaimPostsCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Telepost instance = Telepost.getInstance();
        int width = (Telepost.getInstance().getConfig().getInt("post-width") - 1) / 2;

        ClaimGroup claimGroup = ClaimGroup.builder().description(Component.text("Post claims")).name("Posts").build();


        if (!(sender instanceof Player player)) {
            Bukkit.getConsoleSender().sendMessage(instance.name + PostAPI.getMessage("cant-execute-from-console"));
        } else {

            int i = 0;
            GridIterator gridIterator = new GridIterator();
            while (gridIterator.hasNext()) {
                Location location = gridIterator.next();

                // Calculate the corners of the claim
                Vector3i lowerCorner = new Vector3i(location.getBlockX() - width, 0, location.getBlockZ() - width);
                Vector3i upperCorner = new Vector3i(location.getBlockX() + width, 255, location.getBlockZ() + width); // Set 255 as max Y value for the upper corner

                // Create the claim
                ClaimResult claimResult = Claim.builder()
                        .bounds(lowerCorner, upperCorner)
                        .world(location.getWorld().getUID())
                        .cuboid(false) // set to true if the claim should be a cuboid
                        .type(ClaimTypes.ADMIN)// change this to the appropriate claim type
                        .build();
                if(claimResult.getClaim() == null) continue;
                // Set the claim group
                ClaimData claimData = claimResult.getClaim().getData();
                claimData.setClaimGroupUniqueId(claimGroup.getUniqueId());
                i++;
            }
            player.sendMessage("Done! All posts claimed. Total posts claimed: " + i);
        }

        return false;
    }
}
