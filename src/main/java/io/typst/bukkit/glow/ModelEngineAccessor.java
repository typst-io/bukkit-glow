package io.typst.bukkit.glow;

import com.ticxo.modelengine.api.ModelEngineAPI;
import com.ticxo.modelengine.api.model.ActiveModel;
import com.ticxo.modelengine.api.model.ModeledEntity;
import org.bukkit.ChatColor;

import java.util.UUID;

/**
 * for lazy access due the soft depend
 */
class ModelEngineAccessor {
    public static void setGlowing(UUID ownerId, ChatColor color) {
        ModeledEntity modeledEntity = ModelEngineAPI.getModeledEntity(ownerId);
        if (modeledEntity == null) {
            return;
        }
        int rgb = getRGBFromChatColor(color);
        for (ActiveModel model : modeledEntity.getModels().values()) {
            model.setGlowColor(rgb); // NOTE: ME seems to use ItemDisplay for glowing not scoreboard
            model.setGlowing(true);
        }
    }

    public static void removeGlowing(UUID ownerId) {
        ModeledEntity modeledEntity = ModelEngineAPI.getModeledEntity(ownerId);
        if (modeledEntity == null) {
            return;
        }
        for (ActiveModel model : modeledEntity.getModels().values()) {
            model.setGlowing(false);
        }
    }

    // Reference: https://wiki.vg/Text_formatting#Colors
    private static int getRGBFromChatColor(ChatColor color) {
        return switch (color) {
            case BLACK -> 0x000000;
            case DARK_BLUE -> 0x0000aa;
            case DARK_GREEN -> 0x00aa00;
            case DARK_AQUA -> 0x00aaaa;
            case DARK_RED -> 0xaa0000;
            case DARK_PURPLE -> 0xaa00aa;
            case GOLD -> 0xffaa00;
            case GRAY -> 0xaaaaaa;
            case DARK_GRAY -> 0x555555;
            case BLUE -> 0x5555ff;
            case GREEN -> 0x55ff55;
            case AQUA -> 0x55ffff;
            case RED -> 0xff5555;
            case LIGHT_PURPLE -> 0xff55ff;
            case YELLOW -> 0xffff55;
            default -> 0xffffff;
        };
    }
}
