package de.tjorven.npclib.events;

import de.tjorven.npclib.npc.NPC;
import de.tjorven.npclib.npc.actions.ClickAction;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class NPCPlayerInteractEvent extends Event implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();
    private boolean cancelled;
    private final Player player;
    private final NPC npc;
    private final int id;
    private final ClickAction action;

    public NPCPlayerInteractEvent(Player player, NPC npc, int id, ClickAction action) {
        this.player = player;
        this.npc = npc;
        this.id = id;
        this.action = action;
    }

    public ClickAction getAction() {
        return action;
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
