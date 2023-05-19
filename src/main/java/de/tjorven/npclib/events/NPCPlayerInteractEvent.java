package de.tjorven.npclib.events;

import de.tjorven.npclib.npc.NPC;
import de.tjorven.npclib.npc.enums.ClickAction;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class NPCPlayerInteractEvent extends Event implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();
    private boolean cancelled;
    private final Player PLAYER;
    private final NPC NPC;
    private final int ID;
    private final ClickAction ACTION;

    public NPCPlayerInteractEvent(Player player, NPC npc, int id, ClickAction action) {
        this.PLAYER = player;
        this.NPC = npc;
        this.ID = id;
        this.ACTION = action;
    }

    public ClickAction getAction() {
        return ACTION;
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
