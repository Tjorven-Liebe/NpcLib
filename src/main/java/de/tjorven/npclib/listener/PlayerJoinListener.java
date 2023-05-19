package de.tjorven.npclib.listener;

import de.tjorven.npclib.NpcLib;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        if (!NpcLib.getRegister().containsPlayer(event.getPlayer()))
            NpcLib.getRegister().injectPlayer(event.getPlayer());
    }

}
