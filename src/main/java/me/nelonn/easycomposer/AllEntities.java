package me.nelonn.easycomposer;

import me.nelonn.coprolite.paper.std.registryaccessor.RegistryAccessor;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

public class AllEntities {
    public static final EntityType<ModelDisplay> MODEL_DISPLAY = entity("model_display", EntityType.Builder.of(ModelDisplay::new, MobCategory.MISC).sized(0.0F, 0.0F).clientTrackingRange(512).updateInterval(1).noSave(), EntityType.ITEM_DISPLAY);

    public static <T extends Entity> EntityType<T> entity(String name, EntityType.Builder<?> type, EntityType<?> clientSide) {
        return RegistryAccessor.entityType().register("easycomposer" + ':' + name, type, clientSide);
    }

    public static void register() {
    }
}
