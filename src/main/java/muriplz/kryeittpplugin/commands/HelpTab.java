package muriplz.kryeittpplugin.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class HelpTab implements TabCompleter {
    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        List<String> commands = new ArrayList<>();
        List<String> completions = new ArrayList<>();
        commands.add("buildpost");
        int i=0;
        while(i< commands.size()){
            if(commands.get(i).toLowerCase().contains(args[0])||commands.get(i).contains(args[0])){
                completions.add(commands.get(i));
            }
            i++;
        }
        return completions;
    }
}
