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
    private final Player PLAYER;
    private final NPC NPC;
    private final int ID;
    private final Location FROM;
    private final Location TO;

    public NPCPlayerCollideEvent(Player player, NPC npc, Location from, Location to, int id) {
        this.PLAYER = player;
        this.NPC = npc;
        this.ID = id;
        this.FROM = from;
        this.TO = to;
    }

    public Location getFrom() {
        return FROM;
    }

    public Location getTo() {
        return TO;
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
        return PLAYER;
    }

    public NPC getNpc() {
        return NPC;
    }

    public int getId() {
        return ID;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

}