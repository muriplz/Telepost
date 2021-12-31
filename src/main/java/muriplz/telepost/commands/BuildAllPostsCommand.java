//package muriplz.kryeittpplugin.commands;
//
//import muriplz.kryeittpplugin.KryeitTPPlugin;
//import org.bukkit.Bukkit;
//import org.bukkit.Location;
//import org.bukkit.command.Command;
//import org.bukkit.command.CommandExecutor;
//import org.bukkit.command.CommandSender;
//import org.bukkit.entity.Player;
//import org.jetbrains.annotations.NotNull;
//
//public class BuildAllPostsCommand implements CommandExecutor {
//
//    KryeitTPPlugin instance = KryeitTPPlugin.getInstance();
//    @Override
//    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
//        if (!(sender instanceof Player)) {
//            Bukkit.getConsoleSender().sendMessage(instance.name + "You can't execute this command from console.");
//            return false;
//        }
//        // Getting the player and seeing if he's in the overworld
//        Player player = (Player) sender;
//
//        // If the command is not /homepost ONLY then return false
//        if(args.length!=0){
//            player.sendMessage(PostAPI.getMessage("homepost-usage"));
//            return false;
//        }
//        // Check if the player is on the right dimension
//        if(!player.getWorld().getName().equals("world")){
//            player.sendMessage(PostAPI.getMessage("not-on-overworld"));
//            return false;
//        }
//        PostAPI.buildAllPosts();
//        return true;
//    }
//}
//