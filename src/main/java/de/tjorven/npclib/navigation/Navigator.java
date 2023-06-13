package de.tjorven.npclib.navigation;

import de.tjorven.npclib.NpcLib;
import net.minecraft.server.level.ServerPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.util.Vector;

public class Navigator {

    Vector vector;

    public Navigator(ServerPlayer serverPlayer, Location location) {
        Bukkit.getScheduler().scheduleAsyncRepeatingTask(NpcLib.getInstance(), () -> {
            System.out.println(serverPlayer.getX() + ":" + serverPlayer.getY() + ":" + serverPlayer.getZ());
            serverPlayer.getBukkitEntity().setVelocity(location.add(5, 5, 5).toVector());
        }, 0,20);
    }
}
