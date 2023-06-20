package com.kryeit.compat;

import com.griefdefender.api.GriefDefender;
import com.griefdefender.api.User;
import com.kryeit.Telepost;

import java.util.UUID;

public class GriefDefenderImpl {

    public static final int NEEDED_CLAIMBLOCKS = Telepost.getInstance().getConfig().getInt("needed-blocks");
    public static int getClaimBlocks(UUID playerID) {
        User user = GriefDefender.getCore().getUser(playerID);
        return user == null ? -1 : user.getPlayerData().getInitialClaimBlocks() + user.getPlayerData().getAccruedClaimBlocks() + user.getPlayerData().getBonusClaimBlocks();
    }
}
