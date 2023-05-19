package de.tjorven.npclib;

import de.tjorven.npclib.netty.ChannelDuplexListener;
import io.netty.channel.ChannelPipeline;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_19_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class Register {

    private final JavaPlugin plugin;
    private ChannelPipeline pipeline;

    public Register(JavaPlugin plugin) {
        this.plugin = plugin;
        Bukkit.getOnlinePlayers().forEach(player -> {
            pipeline = ((CraftPlayer) player).getHandle().connection.connection.channel.pipeline();
            injectPlayer(player);
        });
    }

    public void injectPlayer(Player player) {
        pipeline.addBefore("packet_handler", "npc_" + player.getName(), new ChannelDuplexListener(player));
    }

    public void dejectPlayer(Player player) {
        pipeline.remove("npc_" + player);
    }

}
