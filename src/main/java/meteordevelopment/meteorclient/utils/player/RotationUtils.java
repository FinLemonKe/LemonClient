package meteordevelopment.meteorclient.utils.player;

import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec2f;

public class RotationUtils {
    public static double nextYaw(double current, double target, double step) {
        double i = yawAngle(current, target);

        if (step >= Math.abs(i)) {
            return current + i;
        } else {
            return current + (i < 0 ? -1 : 1) * step;
        }
    }

    public static double yawAngle(double current, double target) {
        double c = MathHelper.wrapDegrees(current) + 180, t = MathHelper.wrapDegrees(target) + 180;
        if (c > t) {
            return t + 360 - c < Math.abs(c - t) ? 360 - c + t : t - c;
        } else {
            return 360 - t + c < Math.abs(c - t) ? -(360 - t + c) : t - c;
        }
    }

    public static double nextPitch(double current, double target, double step) {
        double i = target - current;

        return Math.abs(i) <= step ? target : i >= 0 ? current + step : current - step;
    }

    public static double radAngle(Vec2f vec1, Vec2f vec2) {
        double p = vec1.x * vec2.x + vec1.y * vec2.y;
        p /= Math.sqrt(vec1.x * vec1.x + vec1.y * vec1.y);
        p /= Math.sqrt(vec2.x * vec2.x + vec2.y * vec2.y);
        return Math.acos(p);
    }
}
