package com.eiqui.gcbmod.atlas;

import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;

/** 바닐라 Sprite 패스처럼 스프라이트 둘레 padding 을 가장자리 색으로 채운 이미지(아틀라스 (x, y) 에 그대로 씀) */
public final class AtlasWrite {
    private AtlasWrite() {
    }

    /** src 에 padding 을 붙인 새 이미지. 안쪽은 네이티브 복사, 둘레만 픽셀 단위 */
    public static NativeImage padded(TextureAtlasSprite sprite, int mip, NativeImage src, int w, int h) {
        int p = sprite.padding >> mip;
        NativeImage out = new NativeImage(w + 2 * p, h + 2 * p, false);
        src.copyRect(out, 0, 0, p, p, w, h, false, false);
        if (p > 0) {
            for (int y = 0; y < h + 2 * p; y++) {
                boolean edgeRow = y < p || y >= h + p;
                int cy = Math.clamp(y - p, 0, h - 1);
                for (int x = 0; x < w + 2 * p; x++) {
                    if (edgeRow || x < p || x >= w + p) {
                        out.setPixel(x, y, src.getPixel(Math.clamp(x - p, 0, w - 1), cy));
                    }
                }
            }
        }
        return out;
    }
}
