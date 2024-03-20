package me.nelonn.easycomposer.transform;

import me.nelonn.easycomposer.variable.VariablesMap;
import me.nelonn.entitycomposer.shaded.bestvecs.Vec3d;
import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface RelativePosition {

    @NotNull Vec3d apply(@NotNull VariablesMap rootProperties);

    @NotNull
    static RelativePosition vec(@NotNull Vec3d vec3d) {
        return (unused) -> vec3d;
    }

}
