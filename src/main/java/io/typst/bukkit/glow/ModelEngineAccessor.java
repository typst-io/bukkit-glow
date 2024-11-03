package io.typst.bukkit.glow;

import com.ticxo.modelengine.api.ModelEngineAPI;
import com.ticxo.modelengine.api.model.ActiveModel;
import com.ticxo.modelengine.api.model.ModeledEntity;
import com.ticxo.modelengine.api.model.bone.render.renderer.RenderQueues;
import com.ticxo.modelengine.api.model.render.DisplayRenderer;
import com.ticxo.modelengine.api.model.render.ModelRenderer;
import com.ticxo.modelengine.core.model.render.DisplayRendererImpl;
import org.bukkit.entity.EntityType;

import java.util.*;

/**
 * for lazy access due the soft depend
 */
class ModelEngineAccessor {
    public static List<EntityId> getBoneEntities(UUID ownerId) {
        ModeledEntity modeledEntity = ModelEngineAPI.getModeledEntity(ownerId);
        if (modeledEntity == null) {
            return Collections.emptyList();
        }
        List<EntityId> ret = new ArrayList<>();
        for (ActiveModel model : modeledEntity.getModels().values()) {
            model.setGlowing(false);
            model.setGlowColor(-1);
            ModelRenderer render = model.getModelRenderer();
            RenderQueues<Object> displayRenderer = render instanceof RenderQueues
                    ? ((RenderQueues<Object>) render)
                    : null;
            Map<String, Object> rendered = displayRenderer != null ? displayRenderer.getRendered() : Collections.emptyMap();
            for (Map.Entry<String, Object> pair : rendered.entrySet()) {
                Object value = pair.getValue();
                DisplayRenderer.Bone bone = value instanceof DisplayRenderer.Bone ? ((DisplayRendererImpl.BoneImpl) value) : null;
                if (bone == null) continue;
                EntityId entityId = new EntityId(bone.getId(), bone.getUuid(), bone.getUuid().toString(), EntityType.ITEM_DISPLAY);
                ret.add(entityId);
            }
        }
        return ret;
    }
}
