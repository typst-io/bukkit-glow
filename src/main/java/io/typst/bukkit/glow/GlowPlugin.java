package io.typst.bukkit.glow;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * A plugin just to hold the shared state of glow.
 */
public class GlowPlugin extends JavaPlugin {
    private final GlowService glow = new GlowService();

    @Override
    public void onEnable() {
        GlowEngine.register(this);
    }

    GlowService getGlow() {
        return glow;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        GlowPackets.createTeamCreationPacket("test", ChatColor.AQUA, new ArrayList<>(Arrays.asList("a", "b")));
        return true;
    }
}
