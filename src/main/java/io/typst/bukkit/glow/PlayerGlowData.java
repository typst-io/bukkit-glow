package io.typst.bukkit.glow;

import org.bukkit.ChatColor;

public class PlayerGlowData {
    private final String name;
    private final ChatColor color;

    public PlayerGlowData(String name, ChatColor color) {
        this.name = name;
        this.color = color;
    }

    public PlayerGlowData withName(String name) {
        return new PlayerGlowData(name, getColor());
    }

    public PlayerGlowData withColor(ChatColor color) {
        return new PlayerGlowData(getName(), color);
    }

    public String getName() {
        return name;
    }

    public ChatColor getColor() {
        return color;
    }
}
