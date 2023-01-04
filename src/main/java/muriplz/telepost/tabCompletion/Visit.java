package muriplz.telepost.tabCompletion;

import io.github.niestrat99.advancedteleport.api.ATPlayer;
import io.github.niestrat99.advancedteleport.api.Warp;
import muriplz.telepost.commands.PostAPI;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Visit implements TabCompleter {

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {

        if(args.length == 0) {
            return new ArrayList<>();
        }

        // Initialize Lists

        // completions is the returned Lists, starts empty
        List<String> completions = new ArrayList<>();

        // Add all names of named posts and initialize allTabs
        List<String> allTabs = new ArrayList<>(Warp.getWarps().keySet());

        // Get the name of all online players and add it to allTabs
        Bukkit.getOnlinePlayers().forEach(p -> allTabs.add(p.getName()));

        Player player = (Player) sender;

        // Add all offline player's names if the player has the right permission node
        if(player.hasPermission("telepost.visit.others")){
            for(OfflinePlayer p : Bukkit.getServer().getOfflinePlayers()){
                String name = p.getName();
                if(name!=null){
                    if(ATPlayer.getPlayer(p).hasHome("home")){
                        allTabs.add(name);
                    }
                }


            }
        }

        int size = args.length;
        String aux;

        // Add to "completions" all words that have letters that are contained on "commands" list
        for (String allTab : allTabs) {

            allTab = PostAPI.idToName(allTab);
            aux = allTab;

            for(int i=0;i<size-1;i++){
                allTab = allTab.replaceFirst("(?i)"+args[i]+" ","");
            }

            if (aux.toLowerCase().startsWith(PostAPI.getPostName(args).toLowerCase())) {
                completions.add(allTab);
            }
        }
        return completions;

}}
