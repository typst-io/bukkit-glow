package io.typst.bukkit.glow;

import org.bukkit.ChatColor;
import org.bukkit.plugin.Plugin;

import javax.annotation.Nullable;

public class EntityGlowData {
    private final String name;
    private final ChatColor color;
    private final String id;
    private final Plugin plugin;

    /**
     * @param name   the player name, this used by internal to create scoreboard team
     * @param color  the glowing color
     * @param id     the id to determine which feature of plugin glowed
     * @param plugin the plugin glowed, nullable
     */
    public EntityGlowData(String name, ChatColor color, String id, @Nullable Plugin plugin) {
        this.name = name;
        this.color = color;
        this.id = id;
        this.plugin = plugin;
    }

    public EntityGlowData withName(String name) {
        return new EntityGlowData(name, getColor(), getId(), getPlugin());
    }

    public EntityGlowData withColor(ChatColor color) {
        return new EntityGlowData(getName(), color, getId(), getPlugin());
    }

    public String getName() {
        return name;
    }

    public ChatColor getColor() {
        return color;
    }

    public String getId() {
        return id;
    }

    @Nullable
    public Plugin getPlugin() {
        return plugin;
    }
}
