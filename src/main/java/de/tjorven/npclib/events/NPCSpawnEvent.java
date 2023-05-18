package de.tjorven.npclib.events;

import de.tjorven.npclib.npc.NPC;
import org.bukkit.Location;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class NPCSpawnEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();
    private final Location spawnLocation;
    private final NPC npc;

    public NPCSpawnEvent(NPC npc) {
        this.npc = npc;
        this.spawnLocation = npc.getSpawnLocation();
    }

    public Location getSpawnLocation() {
        return spawnLocation;
    }

    public NPC getNPC() {
        return npc;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }
}
