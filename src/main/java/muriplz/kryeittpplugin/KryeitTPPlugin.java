package muriplz.kryeittpplugin;

import muriplz.kryeittpplugin.commands.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public class KryeitTPPlugin extends JavaPlugin implements Listener {
    PluginDescriptionFile pdffile = getDescription();
    FileConfiguration config = this.getConfig();
    public String name = ChatColor.YELLOW+"["+ChatColor.WHITE+pdffile.getName()+ChatColor.YELLOW+"]";
    public String version = pdffile.getVersion();


    public void onEnable(){
        // Register all commands and tab completions
        registerCommands();

        // Set the config.yml file
        config.addDefault("distance-between-posts", 800);
        config.addDefault("post-x-location", 0);
        config.addDefault("post-z-location", 0);
        config.addDefault("post-width", 5);
        config.addDefault("launch-feature", false);
        config.addDefault("multiple-names-per-post",false);
        config.options().copyDefaults(true);
        saveDefaultConfig();

        // Plugin activated at this point
        Bukkit.getConsoleSender().sendMessage(name+ChatColor.GRAY+" The plugin has been activated. version: "+ChatColor.GREEN+version);
    }
    public void onDisable() {
        Bukkit.getConsoleSender().sendMessage(name+ChatColor.WHITE+" The plugin has been deactivated.");
    }
    public void registerCommands() {
        // /nearestpost
        Objects.requireNonNull(this.getCommand("nearestpost")).setExecutor(new NearestPostCommand(this));

        // /setpost
        Objects.requireNonNull(this.getCommand("setpost")).setExecutor(new SetPostCommand(this));

        // /homepost
        Objects.requireNonNull(this.getCommand("homepost")).setExecutor(new HomePostCommand(this));

        // /invite <Player>
        Objects.requireNonNull(this.getCommand("invite")).setExecutor(new InviteCommand(this));

        // /visit <NamedPost/Player>
        Objects.requireNonNull(this.getCommand("v")).setExecutor(new VisitCommand(this));
        Objects.requireNonNull(getCommand("v")).setTabCompleter(new VisitTab());

        // /namepost <Name>
        Objects.requireNonNull(this.getCommand("namepost")).setExecutor(new NamePostCommand(this));
        Objects.requireNonNull(getCommand("namepost")).setTabCompleter(new NamePostTab()); // So the name part doesn't show online players

        // /unnamepost <Name>
        Objects.requireNonNull(this.getCommand("unnamepost")).setExecutor(new UnnamePostCommand(this));
        Objects.requireNonNull(getCommand("unnamepost")).setTabCompleter(new UnnamePostTab());

        // /posthelp (command)
        // <> means that has to be used, () is optional
        Objects.requireNonNull(this.getCommand("posthelp")).setExecutor( new HelpCommand(this));
        Objects.requireNonNull(getCommand("posthelp")).setTabCompleter(new HelpTab());

        // /buildpost (y)
        // /buildpost (x) (z)
        // /buildpost (x) (y) (z)
        Objects.requireNonNull(this.getCommand("buildpost")).setExecutor( new BuildPostCommand(this));
        Objects.requireNonNull(getCommand("buildpost")).setTabCompleter(new BuildPostTab(this));
    }
}





