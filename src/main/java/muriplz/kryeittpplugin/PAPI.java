package muriplz.kryeittpplugin;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PAPI extends PlaceholderExpansion {
    @Override
    public @NotNull String getIdentifier() {
        return "Telepost";
    }

    @Override
    public @NotNull String getAuthor() {
        return "MuriPlz";
    }

    @Override
    public @NotNull String getVersion() {
        return "0.1";
    }

    @Override
    public boolean persist(){
        return true;
    }

    @Override
    public String onPlaceholderRequest(Player p, @NotNull String params){
        if(p==null){
            return "";
        }
        if(params.equals("nearest_location")){
            return KryeitTPPlugin.getInstance().PAPIsupport.get(p.getUniqueId());
        }
        return null;
    }

}
