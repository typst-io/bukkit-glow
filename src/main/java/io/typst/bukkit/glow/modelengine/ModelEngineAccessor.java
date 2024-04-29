package io.typst.bukkit.glow.modelengine;

import com.ticxo.modelengine.api.ModelEngineAPI;
import com.ticxo.modelengine.api.model.ActiveModel;
import com.ticxo.modelengine.api.model.ModeledEntity;
import org.bukkit.ChatColor;

import java.util.UUID;

/**
 * for lazy access due the soft depend
 */
public class ModelEngineAccessor {
    public static void setGlowing(UUID ownerId, ChatColor color) {
        ModeledEntity modeledEntity = ModelEngineAPI.getModeledEntity(ownerId);
        if (modeledEntity == null) {
            return;
        }
        for (ActiveModel model : modeledEntity.getModels().values()) {
            model.setGlowing(true);
            model.setGlowColor((int) color.getChar());
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
}
