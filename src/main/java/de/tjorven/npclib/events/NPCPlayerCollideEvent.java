package de.tjorven.npclib.events;

import de.tjorven.npclib.npc.NPC;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class NPCPlayerCollideEvent extends Event implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();
    private boolean cancelled;
    private final Player player;
    private final NPC npc;
    private final int id;
    private final Location from;
    private final Location to;

    public NPCPlayerCollideEvent(Player player, NPC npc, Location from, Location to, int id) {
        this.player = player;
        this.npc = npc;
        this.id = id;
        this.from = from;
        this.to = to;
    }

    public Location getFrom() {
        return from;
    }

    public Location getTo() {
        return to;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    public Player getPlayer() {
        return player;
    }

    public NPC getNpc() {
        return npc;
    }

    public int getId() {
        return id;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

}