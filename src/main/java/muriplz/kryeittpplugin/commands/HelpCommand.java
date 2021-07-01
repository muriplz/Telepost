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
                PostAPI.sendMessage(player,"&aList of commands: &f( + info on /posthelp <Command> )");
                PostAPI.sendMessage(player,"- &6/nearestpost&f: tells you where the nearest post is.");
                PostAPI.sendMessage(player,"- &6/setpost&f: sets the nearest post as your home post.");
                PostAPI.sendMessage(player,"- &6/homepost&f: teleports you to your home post.");
                PostAPI.sendMessage(player,"- &6/visit <PostName/Player>&f: teleports you to another post.");
                PostAPI.sendMessage(player,"- &6/invite <Player>&f: invite a player to your home post.");
                PostAPI.sendMessage(player,"- &6/postlist&f: shows you all the named posts.");
                if(player.hasPermission("telepost.namepost")||player.hasPermission("telepost.unnamepost")||player.hasPermission("telepost.buildpost")){
                    PostAPI.sendMessage(player,"&aAdmin commands: ");
                    if(player.hasPermission("telepost.namepost")){
                        PostAPI.sendMessage(player,"- &6/namepost <Name>&f: names the nearest post.");
                    }
                    if(player.hasPermission("telepost.unnamepost")){
                        PostAPI.sendMessage(player,"- &6/unnamepost <Name>&f: unnames a post.");
                    }
   //                 if(player.hasPermission("telepost.buildpost")){
   //                     PostAPI.sendMessage(player,"- &6/buildpost (y)&f: builds the nearest post.");
   //                 }
                }

                return true;
            }else if(args.length==1) {
  //              if(args[0].equals("buildpost") && player.hasPermission("telepost.buildpost")) {
  //                  PostAPI.sendMessage(player,"&a/BuildPost guide: ");
  //                  PostAPI.sendMessage(player,"- &6/buildpost&f: builds the nearest post.");
  //                  PostAPI.sendMessage(player,"- &6/buildpost (y)&f: builds the nearest post at a certain height.");
  //                  PostAPI.sendMessage(player,"- &6/buildpost (x) (z)&f: builds a post on that location but on the ground level.");
  //                  PostAPI.sendMessage(player,"- &6/buildpost (x) (y) (z)&f: builds a post on that location.");
  //                  PostAPI.sendMessage(player,"NOTE: I do not recommend building posts with (x) and (z) provided by you, because the teleport system won't work.");
                if(args[0].equals("aliases") || args[0].equals("alias")){
                    PostAPI.sendMessage(player, "&aAll aliases for your commands: ");
                    PostAPI.sendMessage(player, "- &6/h&f: alias for /homepost.");
                    PostAPI.sendMessage(player, "- &6/v&f: alias for /visit.");
                    PostAPI.sendMessage(player, "- &6/plist&f: alias for /postlist.");

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
                    PostAPI.sendMessage(player,"- You can be invited to a post with &6/invite&f.");
                    PostAPI.sendMessage(player,"- You can visit &6Named Posts&f.");
                    if(!player.hasPermission("telepost.visit")){
                        PostAPI.sendMessage(player,"- You can only use this command if you are inside a post.");
                    }
                } else if(args[0].equals("postlist")){
                    PostAPI.sendMessage(player,"&a/PostList guide: ");
                    PostAPI.sendMessage(player,"- This command will show you all named posts.");
                    PostAPI.sendMessage(player,"- You can click the names to teleport to the destination.");
                    PostAPI.sendMessage(player,"- You also have to be inside a post to teleport.");
                    PostAPI.sendMessage(player,"- You can use &6/postlist&f anywhere.");
                } else if(args[0].equals("invite")){
                    PostAPI.sendMessage(player,"&a/Invite guide: ");
                    PostAPI.sendMessage(player,"- You can invite a player to your home post.");
                    PostAPI.sendMessage(player,"- He will have 5 minutes to teleport as much as he wants.");
                    PostAPI.sendMessage(player,"- This command can be used anywhere.");
                    PostAPI.sendMessage(player,"- You will get notified if an invite expires.");
                } else if(args[0].equals("namepost")&&player.hasPermission("telepost.namepost")){
                    PostAPI.sendMessage(player,"&a/NamePost guide: ");
                    PostAPI.sendMessage(player,"- You can set a name for the nearest post.");
                    PostAPI.sendMessage(player,"- The named post will be accesible for everyone.");
                    if(plugin.getConfig().getBoolean("multiple-names-per-post")){
                        PostAPI.sendMessage(player,"- You can give the same post different names.");
                    }else{
                        PostAPI.sendMessage(player,"- You can only set one name per post.");
                    }
                    PostAPI.sendMessage(player,"- This command is only for Admins.");
                } else if(args[0].equals("unnamepost")&&player.hasPermission("telepost.unnamepost")) {
                    PostAPI.sendMessage(player, "&a/UnnamePost guide: ");
                    PostAPI.sendMessage(player, "- You can delete the name of any named post.");
                    PostAPI.sendMessage(player, "- Comming soon: /unnamepost unnames the nearest post.");
                    PostAPI.sendMessage(player, "- This command is only for Admins.");
                }
            }else {
                PostAPI.sendMessage(player,"Use /posthelp or /posthelp <Command>");
                return false;
            }

            return true;
        }
    }
}
