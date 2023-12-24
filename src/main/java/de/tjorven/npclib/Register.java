package de.tjorven.npclib;

import de.tjorven.npclib.command.Test;
import de.tjorven.npclib.netty.ChannelDuplexListener;
import io.netty.channel.ChannelPipeline;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_20_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class Register {

    public Register(JavaPlugin plugin) {
        Bukkit.getOnlinePlayers().forEach(this::injectPlayer);
        plugin.getCommand("test").setExecutor(new Test());
    }

    /**
     * @param player for pipeline name
     * @return true if registered
     */
    public boolean containsPlayer(Player player) {
        ChannelPipeline pipeline = ((CraftPlayer) player).getHandle().connection.connection.channel.pipeline();
        return pipeline.names().contains("npc_" + player.getName());
    }

    public void injectPlayer(Player player) {
        dejectPlayer(player);
        ChannelPipeline pipeline = ((CraftPlayer) player).getHandle().connection.connection.channel.pipeline();
        pipeline.addBefore("packet_handler", "npc_" + player.getName(), new ChannelDuplexListener(player));
    }

    public void dejectPlayer(Player player) {
        if (containsPlayer(player)) {
            ChannelPipeline pipeline = ((CraftPlayer) player).getHandle().connection.connection.channel.pipeline();
            pipeline.remove("npc_" + player.getName());
        }
    }

}
