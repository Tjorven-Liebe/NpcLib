package de.tjorven.npclib.npc.pathfinder;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.phys.Vec3;
import org.bukkit.Location;

public class Navigator {

    ServerPlayer player;

    public Navigator(ServerPlayer player) {
        this.player = player;
    }

    public void moveLinear(Location location) {
        player.move(MoverType.PLAYER, new Vec3(location.getX(), location.getY(), location.getZ()));
//        player.setPos(location.getX(), location.getY(), location.getZ());
    }

}
