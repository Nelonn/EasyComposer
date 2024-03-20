package me.nelonn.easycomposer.dynamic;

import me.nelonn.easycomposer.rotation.MutRotation2d;
import me.nelonn.easycomposer.variable.VariablesMap;
import org.jetbrains.annotations.NotNull;

public interface FutureRotation {
    @NotNull MutRotation2d apply(@NotNull VariablesMap properties);
}