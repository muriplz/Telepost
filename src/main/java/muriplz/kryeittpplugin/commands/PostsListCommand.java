package muriplz.kryeittpplugin.commands;

import io.github.niestrat99.advancedteleport.api.Warp;
import muriplz.kryeittpplugin.KryeitTPPlugin;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class PostsListCommand implements CommandExecutor {
    private final KryeitTPPlugin plugin;

    public PostsListCommand(KryeitTPPlugin plugin) {
        this.plugin = plugin;
    }
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!(sender instanceof Player)) {
            Bukkit.getConsoleSender().sendMessage(plugin.name+"You can't execute this command from console.");
            return false;
        }else{
            Player player = (Player) sender;
            // Get all warps (named posts)
            HashMap<String, Warp> warps = Warp.getWarps();

            // Get all names of named posts and made it into a List
            Set<String> warpNames = warps.keySet();
            List<String> allWarpNames = new ArrayList<>(warpNames);

            // Initialize all the TextComponents
            TextComponent messagePosts = new TextComponent(ChatColor.GOLD+"Posts :");
            TextComponent message;

            // Add to messagePosts all components to teleport to every warp
            for (String warpName : allWarpNames) {
                message = new TextComponent(" " + warpName);
                message.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/v " + warpName));
                message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("Teleport to the " + warpName + " post.")));
                messagePosts.addExtra(message);
            }

            // Send the message with the Text components to the player
            player.spigot().sendMessage(messagePosts);
            return true;


        }
    }
}
