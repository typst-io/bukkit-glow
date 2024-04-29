package io.typst.bukkit.glow;


import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class GlowAPI {

    private static GlowService getServiceOrThrow() {
        GlowService service = JavaPlugin.getPlugin(GlowPlugin.class).getGlow();
        return Objects.requireNonNull(service);
    }

    private static boolean checkModelEngine() {
        return Bukkit.getPluginManager().isPluginEnabled("ModelEngine");
    }

    // TODO: should be removed
    public void setGlowing(Player target, Player receiver) {
        setGlowing((Entity) target, receiver);
    }

    public void setGlowing(Entity target, Player receiver) {
        setGlowing(target, receiver, ChatColor.WHITE);
    }

    // TODO: should be removed
    public void setGlowing(Player target, Player receiver, ChatColor color) {
        setGlowing((Entity) target, receiver, color);
    }

    public void setGlowing(Entity target, Player receiver, ChatColor color) {
        GlowService service = getServiceOrThrow();
        service.setGlowing(target, receiver, color);
        if (checkModelEngine()) {
            ModelEngineAccessor.setGlowing(target.getUniqueId(), color);
        }
    }

    // TODO: should be removed
    public void removeGlowing(Player target, Player receiver) {
        removeGlowing((Entity) target, receiver);
    }

    public void removeGlowing(Entity target, Player receiver) {
        GlowService service = getServiceOrThrow();
        service.removeGlowing(target, receiver);
        if (checkModelEngine()) {
            ModelEngineAccessor.removeGlowing(target.getUniqueId());
        }
    }

    public boolean hasGlowingView(UUID receiverId) {
        GlowService service = getServiceOrThrow();
        Map<String, PlayerGlowData> view = service.getViews().getOrDefault(receiverId, Collections.emptyMap());
        return !view.isEmpty();
    }

    public boolean checkGlowing(String targetName, UUID receiverId) {
        GlowService service = getServiceOrThrow();
        return service.getPlayerGlowData(targetName, receiverId)
                .isPresent();
    }
}
