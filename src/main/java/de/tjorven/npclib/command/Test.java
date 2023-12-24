package de.tjorven.npclib.command;

import com.mojang.authlib.GameProfile;
import de.tjorven.npclib.NpcLibApi;
import de.tjorven.npclib.npc.NPC;
import de.tjorven.npclib.npc.skin.Skin;
import net.kyori.adventure.chat.SignedMessage;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class Test implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        NPC bob = NpcLibApi.createNPC((Player) sender, "Bob", new Skin("Du_Hurensohn", "rF/PiesUb7nuSykRq651iIGlGzUblPRe+0OcwJSykwMq2vunaJwFizl4Mrf1AruxLhms0vUkbQssJDC+58ClnDlHQPDMSybOokF2ARsyrse2w4caN6XXxFm0eXBLtik9j2WbzcpQd1wtv1CS//ZN9On4BsH9kpsDDqBoodhYvSxBvcj0GZJHjDhILTdqMkg4l0LAfWuiC7YA5n/5B/TWVSa5CLFAjlzOuh9BUz9kfonZIIB3+dj51hn+gbjRz360qxUi95qLksU4qI6pdbLotnOx6dI0IaGvnl3MbMHRbvEENycrp3xpEVgNhIAE8gAsd1Epa14sIyA8goHLxC8/EZ1Rthzp5ueSPd753xiBQTtFJ0MsO1GE2RUB5Mfya47IdAANC3GB5mT4jJAQtlgJRuHnaiIk7Wk3CkFQcClSOZtR41o8G4vK3VSau2R/IMZOhSRkQ33RBrbhTHGIwHuDU6bcU5LSjCA2oBauhh5rqxeDIVn3xOb7knSyHgZd3HI0FAap4zE/EGLG+o6hqbP2GEp4vlDbzE/1nUkKKi9Rv/NzSunexCnj0PZE5tJC8swLncC/gK4aNngW97GEgtaT1AHYlOVQXbcarUxg6VdIt08T5IFlhCvgJb720JUdcJ6gCrBTkwvCSecl9lUAIVVOOAW97bXhqSB2OSci3O9/Okc=", "ewogICJ0aW1lc3RhbXAiIDogMTcwMzQ1OTk4NjE0MCwKICAicHJvZmlsZUlkIiA6ICIxOWY1YzkwMWEzMjQ0YzVmYTM4NThjZGVhNDk5ZWMwYSIsCiAgInByb2ZpbGVOYW1lIiA6ICJzb2RpdW16aXAiLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzZmY2ExOTdiZTU0MGM3MTBhZmVkNjM2ZmQ0NWMwZDU0NmFiYjdiMzEzYjBkZjZhNDNkOWY5NzhlNThhMTE1NiIsCiAgICAgICJtZXRhZGF0YSIgOiB7CiAgICAgICAgIm1vZGVsIiA6ICJzbGltIgogICAgICB9CiAgICB9CiAgfQp9"));
        bob.lockViewToPlayer(true);
        bob.spawn();
        return false;
    }
}
