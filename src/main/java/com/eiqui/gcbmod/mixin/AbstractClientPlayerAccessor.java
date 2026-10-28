package com.eiqui.gcbmod.mixin;

import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.player.AbstractClientPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(AbstractClientPlayer.class)
public interface AbstractClientPlayerAccessor {
    @Accessor("playerInfo")
    void gcb$setPlayerInfo(PlayerInfo playerInfo);
}
