package muriplz.kryeittpplugin.tabCompletion;

import muriplz.kryeittpplugin.KryeitTPPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class HelpTab implements TabCompleter {
    private final KryeitTPPlugin plugin;

    public HelpTab(KryeitTPPlugin plugin) {
        this.plugin = plugin;
    }
    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {

        List<String> completions = new ArrayList<>();
        if(args.length==1){
            // Getting the Player
            Player player = (Player) sender;

            // Creating the lists with the autocomplete text
            List<String> commands = new ArrayList<>();


            // Adding the autocomplete text
            if(player.hasPermission("telepost.namepost")){
                commands.add("namepost");
            }
            if(player.hasPermission("telepost.unnamepost")){
                commands.add("unnamepost");
            }
            if(plugin.getConfig().getBoolean("random-post")){
                commands.add("randompost");
            }
            commands.add("aliases");
            commands.add("nearestpost");
            commands.add("setpost");
            commands.add("homepost");
            commands.add("visit");
            commands.add("postlist");
            commands.add("invite");

            // Add to "completions" all words that have letters that are contained on "commands" list
            int i=0;
            while(i < commands.size()){
                if(commands.get(i).toLowerCase().startsWith(args[0].toLowerCase()) || commands.get(i).startsWith(args[0].toLowerCase())) {
                    completions.add(commands.get(i));
                }
                i++;
            }
        }
        return completions;
    }
}
