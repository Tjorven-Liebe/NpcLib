package de.tjorven.npclib.events;

import de.tjorven.npclib.npc.NPC;
import org.bukkit.Location;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class NPCSpawnEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();
    private final Location SPAWN_LOCATION;
    private final NPC NPC;

    public NPCSpawnEvent(NPC npc) {
        this.NPC = npc;
        this.SPAWN_LOCATION = npc.getSpawnLocation();
    }

    public Location getSpawnLocation() {
        return SPAWN_LOCATION;
    }

    public NPC getNPC() {
        return NPC;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }
}
