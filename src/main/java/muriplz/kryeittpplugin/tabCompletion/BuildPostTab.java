package muriplz.kryeittpplugin.tabCompletion;

import muriplz.kryeittpplugin.KryeitTPPlugin;
import muriplz.kryeittpplugin.commands.PostAPI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class BuildPostTab implements TabCompleter {
    private final KryeitTPPlugin plugin;

    public BuildPostTab(KryeitTPPlugin plugin) {
        this.plugin = plugin;
    }
    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {

        // Initialize List
        List<String> completions = new ArrayList<>();

        // Get the player
        Player player = (Player) sender;

        // Get values from config.yml
        int gap = plugin.getConfig().getInt("distance-between-posts");
        int originX = plugin.getConfig().getInt("post-x-location");
        int originZ = plugin.getConfig().getInt("post-z-location");

        if(args.length==1){

            // Get the nearest post X and Z
            int postX = PostAPI.getNearPost(gap,player.getLocation().getBlockX(),originX);
            int postZ = PostAPI.getNearPost(gap,player.getLocation().getBlockZ(),originZ);

            // Adding the nearest post (might be good to have it even tho /buildpost does the job)
            String nearestPost = postX+" "+postZ;

            completions.add(nearestPost);
            return completions;
        }
        return null;
    }
}
