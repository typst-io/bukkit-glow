package io.typst.bukkit.glow;


import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class GlowAPI {
    public void setGlowing(Player target, Player receiver) {
        setGlowing(target, receiver, ChatColor.WHITE);
    }

    private GlowService getServiceOrThrow() {
        GlowService service = JavaPlugin.getPlugin(GlowPlugin.class).getGlow();
        return Objects.requireNonNull(service);
    }

    public void setGlowing(Player target, Player receiver, ChatColor color) {
        GlowService service = getServiceOrThrow();
        service.setGlowing(target, receiver, color);
    }

    public void removeGlowing(Player target, Player receiver) {
        GlowService service = getServiceOrThrow();
        service.removeGlowing(target, receiver);
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
