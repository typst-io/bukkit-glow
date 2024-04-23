package io.typst.bukkit.glow;


import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class GlowAPI {
    private final Plugin plugin;

    public GlowAPI(Plugin plugin) {
        this.plugin = plugin;
    }

    public void setGlowing(Player target, Player receiver) {
        setGlowing(target, receiver, ChatColor.WHITE);
    }

    private GlowService getServiceOrThrow() {
        GlowService service = Bukkit.getServicesManager().load(GlowService.class);
        return Objects.requireNonNull(service);
    }

    public void setGlowing(Player target, Player receiver, ChatColor color) {
        GlowService service = getServiceOrThrow();
        service.setGlowing(target, receiver, color, plugin);
    }

    public void removeGlowing(Player target, Player receiver) {
        GlowService service = getServiceOrThrow();
        service.removeGlowing(target, receiver, plugin);
    }

    public boolean hasGlowingView(UUID receiverId) {
        GlowService service = getServiceOrThrow();
        Map<String, PlayerGlowData> view = service.getViews().getOrDefault(receiverId, Collections.emptyMap());
        return view.entrySet().stream()
                .anyMatch(pair -> pair.getValue().getPlugin().equals(plugin));
    }

    public boolean checkGlowing(String targetName, UUID receiverId) {
        GlowService service = getServiceOrThrow();
        return service.getPlayerGlowData(targetName, receiverId, plugin)
                .isPresent();
    }
}
