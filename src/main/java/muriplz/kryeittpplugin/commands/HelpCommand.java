package muriplz.kryeittpplugin.commands;

import muriplz.kryeittpplugin.KryeitTPPlugin;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class HelpCommand implements CommandExecutor {
    private final KryeitTPPlugin plugin;

    public HelpCommand(KryeitTPPlugin plugin) {
        this.plugin = plugin;
    }
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if ( !( sender instanceof Player)) {
            Bukkit.getConsoleSender().sendMessage(plugin.name+"You can't execute this command from console.");
            return false;
        } else {
            Player player = (Player) sender;

            // /posthelp
            if(args.length==0){
                PostAPI.sendMessage(player,"&aHow to use the commands: &f(use /posthelp <Command> for more info about a command)");
                PostAPI.sendMessage(player,"- &6/nearestpost&f: tells you where the nearest post is.");
                PostAPI.sendMessage(player,"- &6/setpost&f: sets the nearest post as your home post.");
                PostAPI.sendMessage(player,"- &6/homepost&f: teleports you to your home post.");
                PostAPI.sendMessage(player,"- &6/visit <PostName/Player>&f: teleports you to a named post or another player's home post.");
                PostAPI.sendMessage(player,"- &6/invite <Player>&f: invite a player to your home post for 5 minutes");
                PostAPI.sendMessage(player,"Admin commands: ");
                PostAPI.sendMessage(player,"- &6/namepost <Name>&f: names the nearest post, so everyone will be able to visit it.");
                PostAPI.sendMessage(player,"- &6/unnamepost <Name>&f: unnames a post.");
                if(player.hasPermission("telepost.buildpost")){
                    PostAPI.sendMessage(player,"- &6/buildpost (y)&f: use just /buildpost to build the nearest post, see more /posthelp buildpost.");
                }
                return true;
            }
            if(args.length==1) {
                if(args[0].equals("buildpost") && player.hasPermission("telepost.buildpost")) {
                    PostAPI.sendMessage(player,"&a/BuildPost guide: ");
                    PostAPI.sendMessage(player,"- &6/buildpost&f: builds the nearest post.");
                    PostAPI.sendMessage(player,"- &6/buildpost (y)&f: builds the nearest post at a certain height.");
                    PostAPI.sendMessage(player,"- &6/buildpost (x) (z)&f: builds a post on that location but on the ground level.");
                    PostAPI.sendMessage(player,"- &6/buildpost (x) (y) (z)&f: builds a post on that location.");
                    PostAPI.sendMessage(player,"NOTE: I do not recommend building posts with (x) and (z) provided by you, because the teleport system won't work.");
                    return true;
                } else if(args[0].equals("aliases") || args[0].equals("alias")){
                    PostAPI.sendMessage(player, "&aAll aliases for your permissions: ");
                    PostAPI.sendMessage(player, "- &6/phelp&f: alias for /posthelp.");
                    PostAPI.sendMessage(player, "- &6/ph&f: alias for /posthelp.");
                    if (player.hasPermission("telepost.buildpost")) {
                        PostAPI.sendMessage(player, "- &6/bp&f: alias for /buildpost.");
                    }
                    return true;
                }
            }
            player.sendMessage("Use /posthelp or /posthelp <Command>.");
            return false;
        }
    }
}
