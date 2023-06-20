package com.kryeit.leash;

import com.kryeit.Telepost;
import com.kryeit.commands.PostAPI;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.*;

import static com.kryeit.commands.PostAPI.WORLD;

public class LeashAPI {

    public static HashMap<UUID,UUID> leashed = Telepost.getInstance().leashed;

    public static ArrayList<UUID> getLeashed(Player p) {
        ArrayList<UUID> ids = new ArrayList<>();

        for(UUID id : leashed.keySet()) {
            if(leashed.get(id).equals(p.getUniqueId())) {
                ids.add(id);
            }
        }

        return ids;
    }

    public static ArrayList<String> getFlyers() {
        ArrayList<String> flyers = new ArrayList<>();
        flyers.add("chicken");
        flyers.add("parrot");
        flyers.add("bee");
        return flyers;
    }

    public static boolean hasLeashed(Player p) {
        return leashed.containsValue(p.getUniqueId());
    }

    public static int getSafeHeight(Location location) {
        return WORLD.getHighestBlockAt(location).getLocation().getBlockY() + 1;
    }

    public static void teleportLeashed(Player p, Location newlocation) {
        Entity e;
        List<String> flyers = getFlyers();
        for (UUID id : getLeashed(p)) {
            e = Bukkit.getEntity(id);
            if(e == null) {
                leashed.remove(id);
                continue;
            }
            if(Telepost.getInstance().getConfig().getBoolean("launch-feature")) {
                e.setVelocity(new Vector(0,4,0));
                Entity finalE = e;
                Location finalNewlocation = newlocation;
                Bukkit.getScheduler().runTaskLater(Telepost.getInstance(), () -> {
                    Location l1;
                    if(flyers.contains(finalE.getName().toLowerCase())){
                        l1 = new Location( finalE.getWorld() , finalNewlocation.getBlockX() + 0.5 , getSafeHeight(finalNewlocation) , finalNewlocation.getBlockZ() + 0.5 );
                    }else{
                        l1 = new Location( finalE.getWorld() , finalNewlocation.getBlockX() + 0.5 , getSafeHeight(finalNewlocation) , finalNewlocation.getBlockZ() + 0.5 );
                    }
                    finalE.teleport(l1);

                }, 65L);
            }else{
                if(flyers.contains(Objects.requireNonNull(e.getType().getEntityClass()).getName())) {
                    newlocation = new Location( e.getWorld() , newlocation.getBlockX() + 0.5 , PostAPI.getFirstSolid(newlocation) , newlocation.getBlockZ() + 0.5 );
                }else {
                    newlocation = new Location( e.getWorld() , newlocation.getBlockX() + 0.5 , newlocation.getBlockY() , newlocation.getBlockZ() + 0.5 );
                }

                e.teleport(newlocation);
            }
            if(getFlyers().contains(e.getName().toLowerCase())) {
                leashed.remove(id);
            }
        }

    }
}
