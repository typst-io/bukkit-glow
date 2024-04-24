package io.typst.bukkit.glow;

import org.bukkit.plugin.java.JavaPlugin;

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
}
