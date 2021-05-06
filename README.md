# Telepost
This is a Teleporting plugin for Minecraft Java Edition. Download: https://www.spigotmc.org/resources/telepost.91988/
 
 1) Dependencies: This plugin requires AdvancedTeleport (https://www.spigotmc.org/resources/advancedteleport.64139/) github: (https://github.com/Niestrat99/AT-Rewritten/)
 
 2) Tested versions: This plugin has been tested on 1.16.5 Mohist server 

# What's a post?
It's a structure you can tp from, but not to. (you can only tp to a named post, your home post or a home post you have been invited to)
There is a post every 800 (default) blocks.
[![image](https://www.linkpicture.com/q/2021-05-05_11.14.06.png)](https://www.linkpicture.com/view.php?img=LPic609262f7daa961407012337)

# Commands:
- /nearestpost: Tells you where the nearest post is, based on your position.
- /setpost: Sets a home on the nearest post but on y=260.
- /homepost: Teleports you to your home (That can only be set by /setpost, so disable homes feature on the AdvancedTeleport config.yml)
- /invite <Player>: Invite a player to your home post, this invitation lasts 5 minutes.
- /visit <Player/NamedPost>: Teleports you to an invited post or to a Named Post (to name a post use /setwarp Agua (Extremadura, etc))
- /namepost <PostName> : gives a name to the nearest post. Only for admins.
- /unnamepost <PostName> : unnames a post. Only for admins

# Permissions:
- telepost.homepost : lets you use /homepost everywhere in the Overworld.
- telepost.v : lets you use /v and /visit everywhere in the Overworld.
- telepost.namepost : lets you use /namepost.
- telepost.unnamepost : lets you use /unnamepost.

This plugin is a WIP, so the generation of the post itself (as you will have to build everyone of them by hand, FAWE or something like that), and the protection of the post, as well as the fall damage, all of this can be achieved with other plugins.
