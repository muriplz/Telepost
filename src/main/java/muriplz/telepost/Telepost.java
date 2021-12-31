package muriplz.telepost;


import io.github.thatsmusic99.configurationmaster.CMFile;
import muriplz.telepost.Listeners.*;
import muriplz.telepost.commands.*;
import muriplz.telepost.tabCompletion.*;
import muriplz.telepost.tabCompletion.Help;
import muriplz.telepost.tabCompletion.Invite;
import muriplz.telepost.tabCompletion.NearestPost;
import muriplz.telepost.tabCompletion.UnnamePost;
import muriplz.telepost.tabCompletion.Visit;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

public class Telepost extends JavaPlugin {

    public ArrayList<Integer> counterNearest;
    public ArrayList<UUID> showNearest;
    public ArrayList<UUID> blockFall;

    PluginDescriptionFile pdffile = getDescription();
    public String name = ChatColor.YELLOW+"["+ChatColor.WHITE+pdffile.getName()+ChatColor.YELLOW+"]";
    public String version = pdffile.getVersion();

    public static Telepost instance;

    public void onEnable(){

        // All global lists
        telepostData();

        // Register all commands and tab completions
        registerCommands();

        instance = this;

        // Register events
        registerEvents();

        // Set the config.yml file
        loadConfig();

        // Set the messages.yml file
        loadMessages();

        // Plugin activated at this point
        Bukkit.getConsoleSender().sendMessage(name+ChatColor.GRAY+" The plugin has been activated. Version: "+ChatColor.GREEN+version);
    }

    public void onDisable() {
        Bukkit.getConsoleSender().sendMessage(name+ChatColor.WHITE+" The plugin has been deactivated.");
    }

    void loadConfig (){
        CMFile myConfigFile = new CMFile(this, "config") {
            @Override
            public void loadDefaults() {
                addLink("GitHub","https://github.com/muriplz/Telepost");
                addLink("Spigot","https://www.spigotmc.org/resources/telepost.91988");

                addComment("This number has to be higher than 0. (default = 800 blocks)");
                addDefault("distance-between-posts",800);

                addComment("First post's X coordinate (default -> x = 0)");
                addDefault("post-x-location",0);

                addComment("First post's Z coordinate (default -> z = 0)");
                addDefault("post-z-location",0);

                addComment("The width of the post, with center on /nearestpost. Only odd, don't even. ( default = 5 blocks, 2 to each coordinate + the center )");
                addDefault("post-width",5);

                addComment("/homepost and /visit have this feature, this launches you to the sky before teleporting. ( default = true )");
                addDefault("launch-feature",true);

                addComment("With this option true, you can give multiple names to the same post with /namepost. ( default = false )");
                addDefault("multiple-names-per-post",false);

                addComment("After using a TP command you get teleported to y = 265 if this is true, if not, it will teleport you to ground level. ( default = true )");
                addDefault("tp-in-the-air",true);

                addComment("Shows the message on the Action Bar. ( doesn't work for all commands ) ( default = true )");
                addDefault("messages-on-action-bar",true);

                addComment("Teleports to a random post. Homepost and Named Posts are not in the poll. ( default = true )");
                addDefault("random-post",true);

                addComment("Material for the base of the post. (Takes in count the width)");
                addDefault("base-material","STONE_BRICKS");

                addComment("Material for the pillar of the post.");
                addDefault("pillar-material","GLOWSTONE");
            }

        };
        myConfigFile.load();
    }

    void loadMessages (){
        CMFile myMessagesFile = new CMFile(this,"messages") {
            @Override
            public void loadDefaults() {
                addLink("GitHub","https://github.com/muriplz/Telepost");
                addLink("Spigot","https://www.spigotmc.org/resources/telepost.91988");

                addComment("Usage:");
                addDefault("namepost-usage","&fUse /NamePost <PostName>.");
                addDefault("visit-usage","&fUse /visit <PostName/PlayerName>.");
                addDefault("setpost-usage","&fUse /setpost.");
                addDefault("invite-usage","&fUse /invite <Player>.");
                addDefault("homepost-usage","&fUse /HomePost.");

                addComment("Global:");
                addDefault("cant-execute-from-console","You can't execute this command from console.");
                addDefault("no-permission","&cYou don't have permission to use this command.");
                addDefault("not-on-overworld","&cYou have to be in the Overworld to use this command.");
                addDefault("not-inside-post","&cYou have to be inside a post.");

                addComment("/PostList:");
                addDefault("named-posts-translation","&6Named posts");
                addDefault("hover-postlist","&fClick to teleport to %NAMED_POST% post.\nThis post is at &6%POST_LOCATION%&f.");

                addComment("/SetPost:");
                addDefault("set-post-success","&fYou have successfully set your home post at: &6%POST_LOCATION%&f.");
                addDefault("move-post-success","&fYou have successfully moved your home post at: &6%POST_LOCATION%&f.");

                addComment("/HomePost or /Visit:");
                addDefault("already-at-homepost","&cYou are already at your home post.");
                addDefault("named-post-arrival","&fWelcome to &6%NAMED_POST%&f.");
                addDefault("invited-home-arrival","&fWelcome to &6%PLAYER_NAME%&f's home post.");
                addDefault("own-homepost-arrival", "&fWelcome to your home post.");
                addDefault("homepost-without-setpost","&fPlease, set a post with &6/SetPost&f first.");
                addDefault("visit-not-invited","&cYou have not been invited.");
                addDefault("no-homepost","&cYou do not have a home post yet.");
                addDefault("already-invited-post","&cYou are already at his/her home post.");
                addDefault("already-at-namedpost","&cYou are already in&6 %NAMED_POST%&c.");
                addDefault("unknown-post","&fThe post &6%POST_NAME%&f does not exist.");

                addComment("/NearestPost:");
                addDefault("nearest-message","&fThe nearest post is on: &6%POST_LOCATION%&f.");
                addDefault("nearest-message-named","&fThe nearest post is on: &6%POST_LOCATION%&f, it's &6%NAMED_POST%&f.");
                addDefault("nearestpost-already-on","&cYou already have the option enabled.");
                addDefault("nearestpost-already-off","&cYou don't have the option enabled.");

                addComment("/Invite:");
                addDefault("own-invite","&cYou can't invite yourself.");
                addDefault("not-found", "&cPlayer not found.");
                addDefault("invite-expire","&6%PLAYER_NAME%&f does not have access to your home post anymore.");
                addDefault("inviting","&fYou have invited &6%PLAYER_NAME%&f.");
                addDefault("invited","&fYou have been invited by &6%PLAYER_NAME%&f.");

                addComment("/UnnamePost:");
                addDefault("unname-named-post","&6%NAMED_POST% &apost has been unnamed.");
                addDefault("no-such-post","&cNo posts by that name.");

                addComment("/NamePost:");
                addDefault("nearest-already-named","&cThe nearest post is already named, it's &6%NAMED_POST%&c.");
                addDefault("no-named-posts","&cThere are no named posts.");
                addDefault("name-post","&fYou have given the name &6%POST_NAME%&f to the nearest post.");
                addDefault("named-post-already-exists","&cThe post &6%NAMED_POST%&c already exists.");

                addComment("/RandomPost");
                addDefault("random-tp","&fYou have been teleported to a random post.");
                addDefault("random-not-enough-posts","&cThere is not enough posts to choose from.");
                addDefault("random-no-posts","&cThere are no available posts.");
                addDefault("worldborder-too-big","&fThe world border is too big or you have not set it right, use &6/worldborder set <Amount>&f.");

            }
        };
        myMessagesFile.load();
    }

    public static Telepost getInstance(){
        return instance;
    }

    public void registerEvents(){
        getServer().getPluginManager().registerEvents(new onGlide(), this);
        getServer().getPluginManager().registerEvents(new onFall(), this);
        getServer().getPluginManager().registerEvents(new onPlayerMove(), this);
        getServer().getPluginManager().registerEvents(new onPlayerLeave(), this);
        getServer().getPluginManager().registerEvents(new onKick(), this);
    }

    public void telepostData(){
        blockFall = new ArrayList<>();
        showNearest = new ArrayList<>();
        counterNearest = new ArrayList<>();
    }

    public void registerCommands() {
        // /nearestpost
        Objects.requireNonNull(getCommand("nearestpost")).setExecutor(new muriplz.telepost.commands.NearestPost());
        Objects.requireNonNull(getCommand("nearestpost")).setTabCompleter(new NearestPost());

        // /setpost
        Objects.requireNonNull(getCommand("setpost")).setExecutor(new SetPost());
        Objects.requireNonNull(getCommand("setpost")).setTabCompleter(new ReturnEmpty());

        // /homepost
        Objects.requireNonNull(getCommand("homepost")).setExecutor(new HomePost());
        Objects.requireNonNull(getCommand("homepost")).setTabCompleter(new ReturnEmpty());

        // /invite <Player>
        Objects.requireNonNull(getCommand("invite")).setExecutor(new muriplz.telepost.commands.Invite());
        Objects.requireNonNull(getCommand("invite")).setTabCompleter(new Invite());

        // /visit <NamedPost/Player>
        Objects.requireNonNull(getCommand("v")).setExecutor(new muriplz.telepost.commands.Visit());
        Objects.requireNonNull(getCommand("v")).setTabCompleter(new Visit());

        // /namepost <Name>
        Objects.requireNonNull(getCommand("namepost")).setExecutor(new NamePost());
        Objects.requireNonNull(getCommand("namepost")).setTabCompleter(new ReturnEmpty());

        // /unnamepost <Name>
        Objects.requireNonNull(getCommand("unnamepost")).setExecutor(new muriplz.telepost.commands.UnnamePost());
        Objects.requireNonNull(getCommand("unnamepost")).setTabCompleter(new UnnamePost());

        // /posthelp (command)
        // <> means that has to be used, () is optional
        Objects.requireNonNull(getCommand("posthelp")).setExecutor( new muriplz.telepost.commands.Help());
        Objects.requireNonNull(getCommand("posthelp")).setTabCompleter(new Help());

       //  /buildpost (y)
       //  /buildpost (x) (z)
       //  /buildpost (x) (y) (z)
 //       Objects.requireNonNull(this.getCommand("buildpost")).setExecutor( new BuildPostCommand(this));
 //       Objects.requireNonNull(getCommand("buildpost")).setTabCompleter(new BuildPostTab(this));

        // /postlist
        Objects.requireNonNull(getCommand("postlist")).setExecutor( new PostsList());
        Objects.requireNonNull(getCommand("postlist")).setTabCompleter(new ReturnEmpty());

        // /randompost
        Objects.requireNonNull(getCommand("randompost")).setExecutor( new RandomPost());
        Objects.requireNonNull(getCommand("randompost")).setTabCompleter(new ReturnEmpty());

        // /buildallposts
   //     Objects.requireNonNull(getCommand("buildallposts")).setExecutor(new BuildAllPostsCommand());
   //     Objects.requireNonNull(getCommand("buildallposts")).setTabCompleter(new ReturnNullTab());


    }

    public static YamlConfiguration getMessages(){
        File messages = new File(getInstance().getDataFolder(), "messages.yml");
        return YamlConfiguration.loadConfiguration(messages);
    }

}





