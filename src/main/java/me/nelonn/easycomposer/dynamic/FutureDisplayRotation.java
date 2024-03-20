package me.nelonn.easycomposer.dynamic;

import me.nelonn.easycomposer.variable.VariablesMap;
import org.jetbrains.annotations.NotNull;
import org.joml.Quaternionf;

public interface FutureDisplayRotation {
    @NotNull Quaternionf apply(@NotNull VariablesMap properties);
}
