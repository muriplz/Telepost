package muriplz.kryeittpplugin.tabCompletion;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class InviteTab implements TabCompleter {

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if(args.length==1){
            // Initialize Lists

            // completions is the returned Lists, starts empty
            List<String> completions = new ArrayList<>();

            // Initialize allTabs
            List<String> allTabs = new ArrayList<>();

            // Get the name of all online players and add it to allTabs
            Bukkit.getOnlinePlayers().forEach(p -> allTabs.add(p.getName()));

            // Add to "completions" all words that have letters that are contained on "commands" list
            int i=0;
            while(i< allTabs.size()){
                if(allTabs.get(i).toLowerCase().contains(args[0].toLowerCase())||allTabs.get(i).contains(args[0].toLowerCase())){
                    completions.add(allTabs.get(i));
                }
                i++;
            }
            return completions;
        }
        return null;
    }
}
