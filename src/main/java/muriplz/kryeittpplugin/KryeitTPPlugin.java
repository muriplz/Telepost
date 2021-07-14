package muriplz.kryeittpplugin;


import muriplz.kryeittpplugin.Listeners.onFall;
import muriplz.kryeittpplugin.Listeners.onGlide;
import muriplz.kryeittpplugin.Listeners.onPlayerLeave;
import muriplz.kryeittpplugin.Listeners.onPlayerMove;
import muriplz.kryeittpplugin.commands.*;
import muriplz.kryeittpplugin.tabCompletion.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

public class KryeitTPPlugin extends JavaPlugin {

    public ArrayList<Integer> counterNearest;
    public ArrayList<UUID> showNearest;
    public ArrayList<UUID> blockFall;

    PluginDescriptionFile pdffile = getDescription();
    FileConfiguration config = this.getConfig();
    public String name = ChatColor.YELLOW+"["+ChatColor.WHITE+pdffile.getName()+ChatColor.YELLOW+"]";
    public String version = pdffile.getVersion();


    public void onEnable(){
        // All global lists
        telepostData();

        // Register all commands and tab completions
        registerCommands();

        // Register events
        registerEvents();

        // Set the config.yml file
        defaultConfig();

        // Plugin activated at this point
        Bukkit.getConsoleSender().sendMessage(name+ChatColor.GRAY+" The plugin has been activated. Version: "+ChatColor.GREEN+version);
    }

    public void onDisable() {
        Bukkit.getConsoleSender().sendMessage(name+ChatColor.WHITE+" The plugin has been deactivated.");
    }

    public void registerEvents(){
        getServer().getPluginManager().registerEvents(new onGlide(this), this);
        getServer().getPluginManager().registerEvents(new onFall(this), this);
        getServer().getPluginManager().registerEvents(new onPlayerMove(this), this);
        getServer().getPluginManager().registerEvents(new onPlayerLeave(this), this);
    }

    public void telepostData(){
        blockFall = new ArrayList<>();
        showNearest = new ArrayList<>();
        counterNearest = new ArrayList<>();
    }

    public void defaultConfig(){
        // Set the config.yml file
        config.addDefault("distance-between-posts", 800);
        config.addDefault("post-x-location", 0);
        config.addDefault("post-z-location", 0);
        config.addDefault("post-width", 5);
        config.addDefault("launch-feature", false);
        config.addDefault("multiple-names-per-post",false);
        config.addDefault("tp-in-the-air",true);
        config.addDefault("messages-on-action-bar",true);
        config.addDefault("random-post", true);

        config.options().copyDefaults(true);
        saveDefaultConfig();
    }

    public void registerCommands() {
        // /nearestpost
        Objects.requireNonNull(this.getCommand("nearestpost")).setExecutor(new NearestPostCommand(this));
        Objects.requireNonNull(getCommand("nearestpost")).setTabCompleter(new NearestPostTab());

        // /setpost
        Objects.requireNonNull(this.getCommand("setpost")).setExecutor(new SetPostCommand(this));
        Objects.requireNonNull(getCommand("setpost")).setTabCompleter(new ReturnNullTab());

        // /homepost
        Objects.requireNonNull(this.getCommand("homepost")).setExecutor(new HomePostCommand(this));
        Objects.requireNonNull(getCommand("homepost")).setTabCompleter(new ReturnNullTab());

        // /invite <Player>
        Objects.requireNonNull(this.getCommand("invite")).setExecutor(new InviteCommand(this));
        Objects.requireNonNull(getCommand("invite")).setTabCompleter(new InviteTab());

        // /visit <NamedPost/Player>
        Objects.requireNonNull(this.getCommand("v")).setExecutor(new VisitCommand(this));
        Objects.requireNonNull(getCommand("v")).setTabCompleter(new VisitTab());

        // /namepost <Name>
        Objects.requireNonNull(this.getCommand("namepost")).setExecutor(new NamePostCommand(this));
        Objects.requireNonNull(getCommand("namepost")).setTabCompleter(new ReturnNullTab());

        // /unnamepost <Name>
        Objects.requireNonNull(this.getCommand("unnamepost")).setExecutor(new UnnamePostCommand(this));
        Objects.requireNonNull(getCommand("unnamepost")).setTabCompleter(new UnnamePostTab());

        // /posthelp (command)
        // <> means that has to be used, () is optional
        Objects.requireNonNull(this.getCommand("posthelp")).setExecutor( new HelpCommand(this));
        Objects.requireNonNull(getCommand("posthelp")).setTabCompleter(new HelpTab(this));

        // /buildpost (y)
        // /buildpost (x) (z)
        // /buildpost (x) (y) (z)
 //       Objects.requireNonNull(this.getCommand("buildpost")).setExecutor( new BuildPostCommand(this));
 //       Objects.requireNonNull(getCommand("buildpost")).setTabCompleter(new BuildPostTab(this));

        // /postlist
        Objects.requireNonNull(this.getCommand("postlist")).setExecutor( new PostsListCommand(this));
        Objects.requireNonNull(getCommand("postlist")).setTabCompleter(new ReturnNullTab());

        // /randompost
        Objects.requireNonNull(this.getCommand("randompost")).setExecutor( new RandomPostCommand(this));
        Objects.requireNonNull(getCommand("randompost")).setTabCompleter(new ReturnNullTab());

    }
}





