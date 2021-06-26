# Telepost
This is a Teleporting plugin for Minecraft Java Edition. Download: https://www.spigotmc.org/resources/telepost.91988/
 
 1) Dependencies: This plugin requires AdvancedTeleport (https://www.spigotmc.org/resources/advancedteleport.64139/) github: (https://github.com/Niestrat99/AT-Rewritten/)
 
 2) Tested versions: This plugin has been tested on 1.16.5 Mohist server 

# What's a post?
It's a structure you can tp from, but not to. (you can only tp to a named post, your home post or a home post you have been invited to)
There is a post every 800 (default) blocks.
<img src="https://www.linkpicture.com/view.php?img=LPic609262f7daa961407012337" data-canonical-src="https://www.linkpicture.com/q/2021-05-05_11.14.06.png" width="200" height="400" />
[![image](https://www.linkpicture.com/q/2021-05-05_11.14.06.png)](https://www.linkpicture.com/view.php?img=LPic609262f7daa961407012337)

# Commands:
- /nearestpost: Tells you where the nearest post is, based on your position.
- /setpost: Sets a home on the nearest post but on y=260.
- /homepost: Teleports you to your home (That can only be set by /setpost, so disable homes feature on the AdvancedTeleport config.yml)
- /invite <Player>: Invite a player to your home post, this invitation lasts 5 minutes.
- /visit <Player/NamedPost>: Teleports you to an invited post or to a Named Post (to name a post use /setwarp Agua (Extremadura, etc))
- /namepost <PostName> : gives a name to the nearest post. Only for admins.
- /unnamepost <PostName> : unnames a post. Only for admins.
- /posthelp (Command): sends a message with information about commands.
- /buildpost (x) ( y) (z): builds a post. I suggest using /buildpost to build the nearest post or /buildpost (y) to build the nearest post at a certain height. Only for admins.
- /postlist: shows all Named Posts and lets you click them in order to teleport.

# Permissions:
- telepost.homepost : lets you use /homepost everywhere in the Overworld.
- telepost.v : lets you use /v and /visit everywhere in the Overworld.
- telepost.namepost : lets you use /namepost.
- telepost.unnamepost : lets you use /unnamepost.
- telepost.buildpost : lets you use /buildpost.
- telepost.admin: lets you visit players posts without being invited.


This plugin is a WIP so the protection of the post, as well as the fall damage, all of this can be achieved with other plugins. i strongly recommend using an external plugin/mod to build the posts.
