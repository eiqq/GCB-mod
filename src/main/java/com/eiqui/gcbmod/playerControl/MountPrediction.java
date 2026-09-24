package com.eiqui.gcbmod.playerControl;

import com.eiqui.gcbmod.mixin.InterpolationStepsAccessor;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.core.PositionAndRotation;
import net.minecraft.world.entity.AbstractInterpolationHandler;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.InterpolationHandler;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.phys.Vec3;

/**
 * TPS 캐릭터(ModelEngine 베이스 몹) 탑승 조작감 개선. 컨트롤러 수식과 무관한 범용 로직:
 *  1) 탑승 중인 서버 권한 몹의 위치 보간을 STEPS 틱으로 줄임(바닐라 몹 3틱 → 렌더 지연 100ms 절감)
 *  2) 서버가 보낸 속도(needsSync 로 매 틱 전송)로 LEAD 틱만큼 앞당겨 그리고, 갱신이 끊기면 MAX_EXTRAPOLATE 틱까지 외삽(dead reckoning)
 * 예측은 InterpolationHandler.applyPredictedMovement(충돌 검사 포함)로 넣어 다음 서버 위치가 오면 자연히 보정된다.
 */
public final class MountPrediction {
    public static int STEPS = 1;
    public static double LEAD = 1.0;
    public static int MAX_EXTRAPOLATE = 3;

    private static Entity tracked;
    private static int origSteps = -1;
    private static Vec3 expected;
    private static int extrapolated;

    private MountPrediction() {}

    public static void register() {
        ClientTickEvents.END_CLIENT_TICK.register(MountPrediction::tick);
    }

    private static void tick(Minecraft mc) {
        Entity veh = mc.player == null ? null : mc.player.getVehicle();
        if (!(veh instanceof Mob mob) || mob.isLocalInstanceAuthoritative()) {
            release();
            return;
        }
        InterpolationHandler h = mob.getInterpolation();
        if (!(h instanceof AbstractInterpolationHandler)) {
            release();
            return;
        }
        InterpolationStepsAccessor acc = (InterpolationStepsAccessor) (Object) h;
        if (tracked != mob) {
            release();
            tracked = mob;
            origSteps = acc.gcb$getInterpolationSteps();
        }
        if (acc.gcb$getInterpolationSteps() != STEPS) {
            acc.gcb$setInterpolationSteps(STEPS);
        }

        Vec3 target = targetOf(mob, h);
        Vec3 v = mob.getDeltaMovement();
        boolean moving = v.lengthSqr() > 1.0E-8;
        boolean fresh = expected == null || target.distanceToSqr(expected) > 1.0E-8;
        if (fresh) {
            extrapolated = 0;
            if (moving && LEAD > 0) predict(mob, h, v.scale(LEAD));
        } else if (moving && extrapolated < MAX_EXTRAPOLATE) {
            predict(mob, h, v);
            extrapolated++;
        }
        expected = targetOf(mob, h);
    }

    private static Vec3 targetOf(Mob mob, InterpolationHandler h) {
        PositionAndRotation t = h.target();
        return t != null ? t.position() : mob.position();
    }

    private static void predict(Mob mob, InterpolationHandler h, Vec3 delta) {
        if (h.hasActiveInterpolation()) {
            h.applyPredictedMovement(delta);
        } else {
            Vec3 p = mob.position().add(delta);
            if (mob.level().noCollision(mob, mob.getBoundingBox().move(p.subtract(mob.position())))) {
                mob.setPos(p);
            }
        }
    }

    private static void release() {
        if (tracked != null && origSteps >= 0 && tracked.getInterpolation() instanceof AbstractInterpolationHandler) {
            ((InterpolationStepsAccessor) (Object) tracked.getInterpolation()).gcb$setInterpolationSteps(origSteps);
        }
        tracked = null;
        origSteps = -1;
        expected = null;
        extrapolated = 0;
    }
}
