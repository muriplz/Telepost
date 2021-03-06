package muriplz.telepost.tabCompletion;

import io.github.niestrat99.advancedteleport.api.Warp;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class UnnamePost implements TabCompleter {

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if(args.length==1){
            // completions is the returned Lists, starts empty
            List<String> completions = new ArrayList<>();

            // allTabs is the List that has all possible words that might go onto completions
            HashMap<String, Warp> warps = Warp.getWarps();
            Set<String> warpNames = warps.keySet();
            // Add all names of named posts and initialize allTabs
            List<String> allTabs = new ArrayList<>(warpNames);


            // Add to "completions" all words that have letters that are contained on "commands" list
            for (String allTab : allTabs) {
                if (allTab.toLowerCase().startsWith(args[0].toLowerCase())) {
                    completions.add(allTab);
                }
            }
            return completions;
        }
        return new ArrayList<>();
    }}
