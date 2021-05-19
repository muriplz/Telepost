package muriplz.kryeittpplugin.commands;

import io.github.niestrat99.advancedteleport.api.Warp;
import io.github.niestrat99.advancedteleport.sql.WarpSQLManager;
import muriplz.kryeittpplugin.KryeitTPPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Set;


public class NamePostCommand implements CommandExecutor {

    private final KryeitTPPlugin plugin;

    public NamePostCommand(KryeitTPPlugin plugin) {
        this.plugin = plugin;
    }

    // This command aims to be /NamePost in-game
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if( ! ( sender instanceof Player)) {
            Bukkit.getConsoleSender().sendMessage(plugin.name+"You cant execute this command from console.");
            return false;
        }else {
            Player player = (Player) sender;
            if(args.length==0){
                PostAPI.sendMessage(player,"&fUse /NamePost <PostName> to name the nearest post.");
                return false;
            }
            if(!player.hasPermission("telepost.namepost")){
                PostAPI.sendMessage(player,"&cYou don't have permission to use this command.");
                return false;
            }
            if(!player.getWorld().getName().equals("world")){
                PostAPI.sendMessage(player,"&cYou have to be in the Overworld to use this command.");
                return false;
            }
            if(args.length==1){
                if(Warp.getWarps().containsKey(args[0])){
                    PostAPI.sendMessage(player,"&cThe post "+args[0]+" already exists, try a different name or unname the post using /UnnamePost Agua.");
                    return false;
                }
                // get distance between posts from config.yml
                int gap = plugin.getConfig().getInt("distance-between-posts");
                
                // for the X axis
                int originX = plugin.getConfig().getInt("post-x-location");
                int postX = PostAPI.getNearPost(gap,player.getLocation().getBlockX(),originX);

                // for the Z axis
                int originZ = plugin.getConfig().getInt("post-z-location");
                int postZ = PostAPI.getNearPost(gap,player.getLocation().getBlockZ(),originZ);

                Location nearestpostLocation = new Location(player.getWorld(), postX , 260, postZ );
                HashMap<String, Warp> warps = Warp.getWarps();
                Set<String> warpNames = warps.keySet();
                for(String warpName: warpNames){
                    if(Warp.getWarps().get(warpName).getLocation().getBlockX()==nearestpostLocation.getBlockX()&&Warp.getWarps().get(warpName).getLocation().getBlockZ()==nearestpostLocation.getBlockZ()&&!plugin.getConfig().getBoolean("multiple-names-per-post")){
                        PostAPI.sendMessage(player,"&cThe nearest post is already named, it's "+warpName+".");
                        return false;
                    }
                }
                final int finalpostX = postX;
                final int finalpostZ = postZ;
                WarpSQLManager.get().addWarp(new Warp(player.getUniqueId(),
                        args[0],
                        nearestpostLocation,
                        System.currentTimeMillis(),
                        System.currentTimeMillis()), callback ->
                        PostAPI.sendMessage(player,"&7You have named to &6"+args[0]+"&7 the nearest post &a("+finalpostX+" , "+finalpostZ+" )&7. To visit this post use /visit "+args[0]));
                return true;
        }
        return false;
    }
}}
