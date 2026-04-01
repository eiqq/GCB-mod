/*
 * Original work Copyright (c) 2026 Taiyouh
 * Licensed under AGPL-3.0
 * Source: https://github.com/Gensokyo-Reimagined/meg-client-mod
 */
package net.gensokyoreimagined.megClientMod.network;

import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public final class DisplayFields {

    // Data tracker IDs for Display entities (MC 1.21.x)
    // If IDs shift in your MC version, check DisplayEntity class
    public static final int SHARED_DATA_ID = 0;
    public static final int INTERPOLATION_DELAY_ID = 8;
    public static final int TRANSFORM_DURATION_ID = 9;
    public static final int TRANSLATION_ID = 11;
    public static final int SCALE_ID = 12;
    public static final int LEFT_ROTATION_ID = 13;
    public static final int RIGHT_ROTATION_ID = 14;
    public static final int BILLBOARD_ID = 15;
    public static final int BRIGHTNESS_ID = 16;
    public static final int VIEW_RANGE_ID = 17;
    public static final int GLOW_COLOR_ID = 22;
    public static final int DISPLAY_TYPE_ID = 24;

    public static DataTracker.SerializedEntry<Byte> sharedData(byte value) {
        return new DataTracker.SerializedEntry<>(SHARED_DATA_ID, TrackedDataHandlerRegistry.BYTE, value);
    }

    public static DataTracker.SerializedEntry<Integer> interpolationDelay(int value) {
        return new DataTracker.SerializedEntry<>(INTERPOLATION_DELAY_ID, TrackedDataHandlerRegistry.INTEGER, value);
    }

    public static DataTracker.SerializedEntry<Integer> transformDuration(int value) {
        return new DataTracker.SerializedEntry<>(TRANSFORM_DURATION_ID, TrackedDataHandlerRegistry.INTEGER, value);
    }

    public static DataTracker.SerializedEntry<Vector3f> translation(Vector3f value) {
        return new DataTracker.SerializedEntry<>(TRANSLATION_ID, TrackedDataHandlerRegistry.VECTOR_3F, value);
    }

    public static DataTracker.SerializedEntry<Vector3f> scale(Vector3f value) {
        return new DataTracker.SerializedEntry<>(SCALE_ID, TrackedDataHandlerRegistry.VECTOR_3F, value);
    }

    public static DataTracker.SerializedEntry<Quaternionf> leftRotation(Quaternionf value) {
        return new DataTracker.SerializedEntry<>(LEFT_ROTATION_ID, TrackedDataHandlerRegistry.QUATERNION_F, value);
    }

    public static DataTracker.SerializedEntry<Quaternionf> rightRotation(Quaternionf value) {
        return new DataTracker.SerializedEntry<>(RIGHT_ROTATION_ID, TrackedDataHandlerRegistry.QUATERNION_F, value);
    }

    public static DataTracker.SerializedEntry<Byte> billboard(byte value) {
        return new DataTracker.SerializedEntry<>(BILLBOARD_ID, TrackedDataHandlerRegistry.BYTE, value);
    }

    public static DataTracker.SerializedEntry<Integer> brightness(int value) {
        return new DataTracker.SerializedEntry<>(BRIGHTNESS_ID, TrackedDataHandlerRegistry.INTEGER, value);
    }

    public static DataTracker.SerializedEntry<Float> viewRange(float value) {
        return new DataTracker.SerializedEntry<>(VIEW_RANGE_ID, TrackedDataHandlerRegistry.FLOAT, value);
    }

    public static DataTracker.SerializedEntry<Integer> glowColor(int value) {
        return new DataTracker.SerializedEntry<>(GLOW_COLOR_ID, TrackedDataHandlerRegistry.INTEGER, value);
    }

    public static DataTracker.SerializedEntry<Byte> displayType(byte value) {
        return new DataTracker.SerializedEntry<>(DISPLAY_TYPE_ID, TrackedDataHandlerRegistry.BYTE, value);
    }

    private DisplayFields() {}
}
