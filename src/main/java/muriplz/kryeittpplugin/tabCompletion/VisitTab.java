package muriplz.kryeittpplugin.tabCompletion;

import io.github.niestrat99.advancedteleport.api.ATPlayer;
import io.github.niestrat99.advancedteleport.api.Warp;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class VisitTab implements TabCompleter {

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if(args.length==1){
            // Initialize Lists

            // completions is the returned Lists, starts empty
            List<String> completions = new ArrayList<>();

            // allTabs is the List that has all possible words that might go onto completions
            HashMap<String, Warp> warps = Warp.getWarps();
            Set<String> warpNames = warps.keySet();

            // Add all names of named posts and initialize allTabs
            List<String> allTabs = new ArrayList<>(warpNames);

            // Get the name of all online players and add it to allTabs
            Bukkit.getOnlinePlayers().forEach(p -> allTabs.add(p.getName()));

            Player player = (Player) sender;

            // Add all offline player's names if the player has the right permission node
            if(player.hasPermission("telepost.visit.others")){
                for(OfflinePlayer p : Bukkit.getServer().getOfflinePlayers()){
                    if(p.hasPlayedBefore()){
                        ATPlayer atPlayer = new ATPlayer(p.getUniqueId(),p.getName());
                        if(atPlayer.hasHome("home")){
                            allTabs.add(p.getName());
                        }
                    }
                }
            }

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
