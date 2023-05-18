package de.tjorven.npclib.netty;

import de.tjorven.npclib.NpcLib;
import de.tjorven.npclib.events.NPCPlayerInteractEvent;
import de.tjorven.npclib.npc.NPC;
import de.tjorven.npclib.npc.actions.ClickAction;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.network.protocol.game.ServerboundInteractPacket;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ChannelDuplexListener extends ChannelDuplexHandler {

    private final Player player;
    private final Map<UUID, Long> longMap;

    public ChannelDuplexListener(Player player) {
        this.player = player;
        this.longMap = new HashMap<>();
    }

    @Override
    public void channelRead(@NotNull ChannelHandlerContext ctx, @NotNull Object msg) throws Exception {
        if (msg instanceof ServerboundInteractPacket packet) {
            if (longMap.containsKey(player.getUniqueId()))
                if (System.currentTimeMillis() - longMap.get(player.getUniqueId()) < 100) return;
            longMap.put(player.getUniqueId(), System.currentTimeMillis());
            int id = packet.getEntityId();
            Bukkit.getScheduler().scheduleSyncDelayedTask(NpcLib.getInstance(), () -> {
                NPC npc = null;
//                if (NPCRegister.containsNpc(id))
//                    npc = NPCRegister.getNpc(id);
//                else return;
                if (packet.getActionType().equals(ServerboundInteractPacket.ActionType.INTERACT)) {
                    Bukkit.getPluginManager().callEvent(new NPCPlayerInteractEvent(player, npc, npc.getEntityId(), ClickAction.RIGHT_CLICK));
                } else {
                    Bukkit.getPluginManager().callEvent(new NPCPlayerInteractEvent(player, npc, npc.getEntityId(), ClickAction.LEFT_CLICK));
                }

            });
        }
        super.channelRead(ctx, msg);
    }
}
