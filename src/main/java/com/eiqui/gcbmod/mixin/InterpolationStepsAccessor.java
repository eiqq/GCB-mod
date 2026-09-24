package com.eiqui.gcbmod.mixin;

import net.minecraft.world.entity.AbstractInterpolationHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(AbstractInterpolationHandler.class)
public interface InterpolationStepsAccessor {
    @Accessor("interpolationSteps")
    int gcb$getInterpolationSteps();

    @Accessor("interpolationSteps")
    void gcb$setInterpolationSteps(int steps);
}
