package me.nelonn.easycomposer;

import me.nelonn.entitycomposer.api.Root;
import me.nelonn.entitycomposer.api.actor.Actor;
import me.nelonn.entitycomposer.api.actor.ActorType;
import org.jetbrains.annotations.NotNull;

public class Blueprint extends Actor {
    public Blueprint(@NotNull ActorType<?> type, @NotNull Root root) {
        super(type, root);
    }
}
