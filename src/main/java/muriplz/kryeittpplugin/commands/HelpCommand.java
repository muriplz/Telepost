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
                if(player.hasPermission("telepost.namepost")||player.hasPermission("telepost.unnamepost")||player.hasPermission("telepost.buildpost")){
                    PostAPI.sendMessage(player,"Admin commands: ");
                    if(player.hasPermission("telepost.namepost")){
                        PostAPI.sendMessage(player,"- &6/namepost <Name>&f: names the nearest post, so everyone will be able to visit it.");
                    }
                    if(player.hasPermission("telepost.unnamepost")){
                        PostAPI.sendMessage(player,"- &6/unnamepost <Name>&f: unnames a post.");
                    }
                    if(player.hasPermission("telepost.buildpost")){
                        PostAPI.sendMessage(player,"- &6/buildpost (y)&f: use just /buildpost to build the nearest post, see more /posthelp buildpost.");
                    }
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
                    PostAPI.sendMessage(player, "&aAll aliases for your commands: ");
                    PostAPI.sendMessage(player, "- &6/h&f: alias for /homepost.");
                    PostAPI.sendMessage(player, "- &6/v&f: alias for /visit.");
                    return true;
                } else if(args[0].equals("nearestpost")){
                    PostAPI.sendMessage(player,"&a/NearestPost guide: ");
                    PostAPI.sendMessage(player,"- There is a post every &6"+plugin.getConfig().getInt("distance-between-posts")+ "&f blocks.");
                    PostAPI.sendMessage(player,"- You can only use teleports inside posts.");
                    PostAPI.sendMessage(player,"- Use F3 to see your own coordinates.");
                } else if(args[0].equals("setpost")){
                    PostAPI.sendMessage(player,"&a/SetPost guide: ");
                    PostAPI.sendMessage(player,"- This command will set a home for you on the nearest post.");
                    PostAPI.sendMessage(player,"- You can visit your home post using &6/homepost&f.");
                    PostAPI.sendMessage(player,"- You can only have 1 home post at a time.");
                } else if(args[0].equals("homepost")){
                    PostAPI.sendMessage(player,"&a/HomePost guide: ");
                    PostAPI.sendMessage(player,"- This command will teleport to your home post.");
                    PostAPI.sendMessage(player,"- You can set your home post using &6/setpost&f.");
                    if(!player.hasPermission("telepost.homepost")){
                        PostAPI.sendMessage(player,"- You can only use this command if you are inside a post.");
                    }
                    PostAPI.sendMessage(player,"- You can only have 1 home post at a time.");
                } else if(args[0].equals("visit")){
                    PostAPI.sendMessage(player,"&a/Visit guide: ");
                    PostAPI.sendMessage(player,"- This command will teleport to your destination.");
                    PostAPI.sendMessage(player,"- Possible destinations are: Post you have been invited to or &6named posts&f.");
                    if(!player.hasPermission("telepost.visit")){
                        PostAPI.sendMessage(player,"- You can only use this command if you are inside a post.");
                    }
                } else if(args[0].equals("postlist")){
                    PostAPI.sendMessage(player,"&a/PostList guide: ");
                    PostAPI.sendMessage(player,"- This command will show you all named posts.");
                    PostAPI.sendMessage(player,"- You can click the names to teleport to the destination.");
                    PostAPI.sendMessage(player,"- You also have to be inside a post to teleport, but you can use &6/postlist&f anywhere.");
                }
            }
            player.sendMessage("Use /posthelp or /posthelp <Command>.");
            return false;
        }
    }
}
