package me.nelonn.easycomposer.animation;

import me.nelonn.easycomposer.AActor;
import me.nelonn.easycomposer.variable.VariablesMap;
import org.jetbrains.annotations.NotNull;

public interface AnimationType<T extends Animation> {
    @NotNull T create(@NotNull AActor actor, @NotNull VariablesMap properties);
}
