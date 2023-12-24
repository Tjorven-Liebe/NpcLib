package de.tjorven.npclib.netty;

import de.tjorven.npclib.NpcLib;
import de.tjorven.npclib.events.NPCPlayerInteractEvent;
import de.tjorven.npclib.npc.NPC;
import de.tjorven.npclib.npc.enums.ClickAction;
import de.tjorven.npclib.util.save.NPCRegister;
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

    private final Player PLAYER;
    private final Map<UUID, Long> LONG_MAP;

    public ChannelDuplexListener(Player player) {
        this.PLAYER = player;
        this.LONG_MAP = new HashMap<>();
    }

    @Override
    public void channelRead(@NotNull ChannelHandlerContext ctx, @NotNull Object msg) throws Exception {
        if (msg instanceof ServerboundInteractPacket packet) {
            if (LONG_MAP.containsKey(PLAYER.getUniqueId()))
                if (System.currentTimeMillis() - LONG_MAP.get(PLAYER.getUniqueId()) < 100) return;
            LONG_MAP.put(PLAYER.getUniqueId(), System.currentTimeMillis());
            int id = packet.getEntityId();
            Bukkit.getScheduler().scheduleSyncDelayedTask(NpcLib.getInstance(), () -> {
                NPC npc;
                if (NPCRegister.contains(id))
                    npc = NPCRegister.getNPC(id);
                else return;
                if (packet.isAttack()) {
                    Bukkit.getPluginManager().callEvent(new NPCPlayerInteractEvent(PLAYER, npc, npc.getEntityId(), ClickAction.LEFT_CLICK));
                } else {
                    Bukkit.getPluginManager().callEvent(new NPCPlayerInteractEvent(PLAYER, npc, npc.getEntityId(), ClickAction.RIGHT_CLICK));
                }
            });
        }
        super.channelRead(ctx, msg);
    }
}
