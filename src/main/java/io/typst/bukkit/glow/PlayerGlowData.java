package io.typst.bukkit.glow;

import org.bukkit.ChatColor;
import org.bukkit.plugin.Plugin;

public class PlayerGlowData {
    private final String name;
    private final ChatColor color;
    private final Plugin plugin;

    public PlayerGlowData(String name, ChatColor color, Plugin plugin) {
        this.name = name;
        this.color = color;
        this.plugin = plugin;
    }

    public PlayerGlowData withName(String name) {
        return new PlayerGlowData(name, getColor(), getPlugin());
    }

    public PlayerGlowData withColor(ChatColor color) {
        return new PlayerGlowData(getName(), color, getPlugin());
    }

    public String getName() {
        return name;
    }

    public ChatColor getColor() {
        return color;
    }

    public Plugin getPlugin() {
        return plugin;
    }
}
