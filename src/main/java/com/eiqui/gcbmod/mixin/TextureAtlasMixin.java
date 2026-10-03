package com.eiqui.gcbmod.mixin;

import com.eiqui.gcbmod.atlas.AtlasWrite;
import com.eiqui.gcbmod.config.GcbConfig;
import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.renderpearl.api.commands.CommandEncoder;
import com.mojang.renderpearl.api.textures.GpuTexture;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.SpriteContents;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

/**
 * directAtlasUpload: 리소스를 불러올 때 RAM 의 이미지를 아틀라스에 바로 쓴다(1.21.8 방식).
 * 26.3 바닐라는 스프라이트마다 GPU 텍스처를 만들어 전부 들고 있는 채로 아틀라스에 그려 넣어서, 불러오는 순간 VRAM 이 아틀라스의 약 2배가 된다.
 * 애니메이션은 바닐라 GPU 방식 그대로 둔다(CPU 로 매 틱 올리면 큰 보간 이펙트 때문에 프레임이 크게 떨어짐).
 */
@Mixin(TextureAtlas.class)
public abstract class TextureAtlasMixin {
    @Shadow
    private List<TextureAtlasSprite> sprites;
    @Shadow
    private int maxMipLevel;

    @Shadow
    protected abstract void uploadAnimationFrames();

    @Inject(method = "uploadInitialContents", at = @At("HEAD"), cancellable = true)
    private void gcb$uploadDirect(CallbackInfo ci) {
        if (!GcbConfig.get().directAtlasUpload) return;
        GpuTexture atlas = ((AbstractTexture) (Object) this).getTexture();
        CommandEncoder encoder = RenderSystem.getDevice().createCommandEncoder();
        for (TextureAtlasSprite sprite : sprites) {
            if (sprite.isAnimated()) continue;
            SpriteContents contents = sprite.contents();
            NativeImage[] mips = contents.byMipLevel;
            for (int mip = 0; mip <= maxMipLevel && mip < mips.length; mip++) {
                int w = contents.width() >> mip, h = contents.height() >> mip;
                if (w <= 0 || h <= 0) break;
                try (NativeImage image = AtlasWrite.padded(sprite, mip, mips[mip], w, h)) {
                    encoder.writeToTexture(atlas, image, mip, 0, sprite.getX() >> mip, sprite.getY() >> mip);
                }
            }
        }
        uploadAnimationFrames();
        ci.cancel();
    }
}
