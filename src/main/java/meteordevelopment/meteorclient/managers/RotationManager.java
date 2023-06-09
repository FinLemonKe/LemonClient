package meteordevelopment.meteorclient.managers;

import meteordevelopment.meteorclient.LemonClient;
import meteordevelopment.meteorclient.enums.RotationType;
import meteordevelopment.meteorclient.systems.modules.settings.RotationSettings;
import meteordevelopment.meteorclient.mixin.MixinPlayerMoveC2SPacket;
import meteordevelopment.meteorclient.utils.LemonKUtils;
import meteordevelopment.meteorclient.utils.player.RotationUtils;
import meteordevelopment.meteorclient.utils.other.SettingUtils;
import meteordevelopment.meteorclient.events.entity.player.SendMovementPacketsEvent;
import meteordevelopment.meteorclient.events.packets.PacketEvent;
import meteordevelopment.meteorclient.events.render.Render3DEvent;
import meteordevelopment.meteorclient.systems.modules.Modules;
import meteordevelopment.meteorclient.utils.player.Rotations;
import meteordevelopment.orbit.EventHandler;
import meteordevelopment.orbit.EventPriority;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.List;

import static meteordevelopment.meteorclient.LemonClient.mc;

public class RotationManager {

    public Target target = null;
    public double timer = 0;

    public float[] lastDir = new float[]{0, 0};
    public double priority = 1000;
    public float[] rot = new float[]{0, 0};
    public RotationSettings settings = null;
    public boolean unsent = false;
    public static List<Rotation> history = new ArrayList<>();
    public double rotationsLeft = 0;

    public RotationManager() {
        LemonClient.EVENT_BUS.subscribe(this);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    private void onRender(Render3DEvent event) {
        if (mc.player == null) {return;}
        rotationsLeft = Math.min(rotationsLeft + event.frameTime * (SettingUtils.rotationPackets() - 20), Math.ceil((SettingUtils.rotationPackets() - 20) / 20f));
        if (settings == null) {
            settings = Modules.get().get(RotationSettings.class);
        }
        timer -= event.frameTime;
        if (timer > 0 && target != null && lastDir != null) {
            rot = lastDir;
            if (SettingUtils.shouldVanillaRotate()) {
                mc.player.setYaw(lastDir[0]);
                mc.player.setPitch(lastDir[1]);
            }
        } else if (target != null) {
            target = null;
            priority = 1000;
        } else {
            priority = 1000;
        }
    }

    @EventHandler
    private void onMovePre(SendMovementPacketsEvent.Pre event) {
        unsent = true;
    }
    @EventHandler
    private void onMovePost(SendMovementPacketsEvent.Post event) {
        if (unsent && target != null && timer > 0) {
            mc.getNetworkHandler().sendPacket(new PlayerMoveC2SPacket.LookAndOnGround(0, 0, Managers.ONGROUND.isOnGround()));
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    private void onPacket(PacketEvent.Send event) {
        if (event.packet instanceof PlayerMoveC2SPacket packet) {
            if (target != null && timer > 0) {
                unsent = false;
                target.vec = getTargetPos();
                float[] next = new float[]{(float) RotationUtils.nextYaw(lastDir[0], Rotations.getYaw(target.vec), settings.yawStep.get()), (float) RotationUtils.nextPitch(lastDir[1], Rotations.getPitch(target.vec), settings.pitchStep.get())};

                ((MixinPlayerMoveC2SPacket) packet).setLook(true);
                ((MixinPlayerMoveC2SPacket) packet).setYaw(next[0]);
                ((MixinPlayerMoveC2SPacket) packet).setPitch(next[1]);
                addHistory(next[0], next[1]);
            }
            if (packet.changesLook()) {
                lastDir = new float[]{((MixinPlayerMoveC2SPacket) packet).getYaw(), ((MixinPlayerMoveC2SPacket) packet).getPitch()};
            }
        }
    }
    public boolean isTarget(Box box) {
        return target != null && box.minX == target.box.minX && box.minY == target.box.minY && box.minZ == target.box.minZ &&
            box.maxX == target.box.maxX && box.maxY == target.box.maxY && box.maxZ == target.box.maxZ;
    }
    public void end(Box box) {
        if (isTarget(box)) {
            priority = 1000;
        }
    }
    public void end(BlockPos pos) {
        end(LemonKUtils.getBox(pos));
    }

    public boolean start(BlockPos pos, Box box, Vec3d vec, double p, RotationType type) {
        if (p <= priority && settings != null) {
            priority = p;
            target = pos != null ? new Target(pos, vec != null ? vec : LemonKUtils.getMiddle(box), p, type) : new Target(box, vec != null ? vec : LemonKUtils.getMiddle(box), p, type);
            timer = 1;

            target.vec = getTargetPos();

            if (SettingUtils.rotationCheckHistory(box, type)) {
                return true;
            }
            if (!isTarget(box) && SettingUtils.rotationCheck(mc.player.getEyePos(), RotationUtils.nextYaw(lastDir[0], Rotations.getYaw(target.vec), settings.yawStep.get()),
                RotationUtils.nextPitch(lastDir[1], Rotations.getPitch(target.vec), settings.pitchStep.get()), target.box, type) && rotationsLeft >= 1) {
                rotationsLeft -= 1;
                mc.getNetworkHandler().sendPacket(new PlayerMoveC2SPacket.LookAndOnGround(0, 0, Managers.ONGROUND.isOnGround()));
                return true;
            }
        }
        return false;
    }

    public boolean start(Box box, Vec3d vec, double p, RotationType type) {
        return start(null, box, vec, p, type);
    }
    public boolean start(Box box, double p, RotationType type) {
        return start(box, LemonKUtils.getMiddle(box), p, type);
    }
    public boolean start(BlockPos pos, double p, RotationType type) {
        return start(LemonKUtils.getBox(pos), p, type);
    }
    public boolean start(BlockPos pos, Vec3d vec, double p, RotationType type) {
        return start(pos, new Box(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1), vec, p, type);
    }
    public void addHistory(double yaw, double pitch) {
        if (history.size() > 10) {
            for (int i = history.size() - 9; i > 0; i++) {
                history.remove(history.size() - 1);
            }
        } else if (history.size() == 10) {
            history.remove(9);
        }
        history.add(0, new Rotation(yaw, pitch, mc.player.getEyePos()));
    }
    public void endAny() {
        target = null;
        timer = 0;
    }
    public record Rotation(double yaw, double pitch, Vec3d vec) {}

    public Vec3d getTargetPos() {
        if (SettingUtils.shouldGhostRotate()) {
            if (target.pos == null) {
                return SettingUtils.getGhostRot(target.box, target.vec);
            } else {
                return SettingUtils.getGhostRot(target.pos, target.vec);
            }
        }
        return target.vec;
    }

    class Target {
        public final BlockPos pos;
        public final Box box;
        public final Vec3d targetVec;
        public Vec3d vec;
        public final double priority;
        public final RotationType type;

        public Target(BlockPos pos, Vec3d vec, double priority, RotationType type) {
            this.pos = pos;
            this.box = new Box(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1);
            this.vec = vec;
            this.targetVec = vec;
            this.priority = priority;
            this.type = type;
        }
        public Target(Box box, Vec3d vec, double priority, RotationType type) {
            this.pos = null;
            this.box = box;
            this.vec = vec;
            this.targetVec = vec;
            this.priority = priority;
            this.type = type;
        }
    }
}



