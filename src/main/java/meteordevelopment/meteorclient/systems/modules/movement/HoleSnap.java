package meteordevelopment.meteorclient.systems.modules.movement;

import meteordevelopment.meteorclient.events.entity.player.PlayerMoveEvent;
import meteordevelopment.meteorclient.events.packets.PacketEvent;
import meteordevelopment.meteorclient.events.render.Render3DEvent;
import meteordevelopment.meteorclient.mixininterface.IVec3d;
import meteordevelopment.meteorclient.settings.*;
import meteordevelopment.meteorclient.systems.modules.Categories;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.systems.modules.Modules;
import meteordevelopment.meteorclient.systems.modules.world.Timer;
import meteordevelopment.meteorclient.utils.LemonKUtils;
import meteordevelopment.meteorclient.utils.player.ChatUtils;
import meteordevelopment.meteorclient.utils.player.Rotations;
import meteordevelopment.meteorclient.utils.render.color.SettingColor;
import meteordevelopment.orbit.EventHandler;
import meteordevelopment.orbit.EventPriority;
import net.minecraft.block.Blocks;
import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.List;

public class HoleSnap extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();
    private final SettingGroup sgRender = settings.createGroup("Render");
    private final Setting<Boolean> single = sgGeneral.add(new BoolSetting.Builder()
        .name("Single")
        .description("Only chooses target hole once")
        .defaultValue(true)
        .build()
    );

    private final Setting<Boolean> jump = sgGeneral.add(new BoolSetting.Builder()
        .name("Jump")
        .description("Jumps to the hole (very useful)")
        .defaultValue(false)
        .build()
    );

    private final Setting<Integer> jumpCoolDown = sgGeneral.add(new IntSetting.Builder()
        .name("Jump Cooldown")
        .description("Ticks between jumps")
        .defaultValue(5)
        .range(0, 100)
        .sliderMax(100)
        .build()
    );

    private final Setting<Double> speed = sgGeneral.add(new DoubleSetting.Builder()
        .name("Speed")
        .description("Movement Speed")
        .defaultValue(0.2873)
        .min(0)
        .sliderMax(1)
        .build()
    );

    private final Setting<Double> timer = sgGeneral.add(new DoubleSetting.Builder()
        .name("Timer")
        .description("Sends packets faster")
        .defaultValue(30)
        .min(0)
        .sliderMax(100)
        .build()
    );

    private final Setting<Integer> range = sgGeneral.add(new IntSetting.Builder()
        .name("Range")
        .description("Horizontal range for finding holes")
        .defaultValue(3)
        .range(0, 10)
        .sliderMax(10)
        .build()
    );

    private final Setting<Integer> downRange = sgGeneral.add(new IntSetting.Builder()
        .name("Down Range")
        .description("Vertical range for finding holes")
        .defaultValue(3)
        .range(0, 10)
        .sliderMax(10)
        .build()
    );

    private final Setting<Integer> depth = sgGeneral.add(new IntSetting.Builder()
        .name("Hole Depth")
        .description("How deep a hole has to be")
        .defaultValue(3)
        .range(1, 10)
        .sliderRange(1, 10)
        .build()
    );

    private final Setting<Integer> coll = sgGeneral.add(new IntSetting.Builder()
        .name("Collisions to disable")
        .description("0 = doesn't disable")
        .defaultValue(15)
        .sliderRange(0, 100)
        .build()
    );

    private final Setting<Integer> rDisable = sgGeneral.add(new IntSetting.Builder()
        .name("Rubberbands to disable")
        .description("0 = doesn't disable")
        .defaultValue(1)
        .sliderRange(0, 100)
        .build()
    );

    private final Setting<SettingColor> color = sgRender.add(new ColorSetting.Builder()
            .name("render-color")
            .description("The color of the line.")
            .defaultValue(new SettingColor(255,255,255))
            .build()
    );

    public HoleSnap() {
        super(Categories.Movement, "Hole-Snap", "Move you into the hole nearby");
    }

    BlockPos singleHole;
    int collisions;
    int rubberbands;
    int ticks;
    List<BlockPos> holes = new ArrayList<>();
    BlockPos curHole;

    @Override
    public void onActivate() {
        super.onActivate();
        singleHole = findHole();
        rubberbands = 0;
        ticks = 0;
    }

    @Override
    public void onDeactivate() {
        super.onDeactivate();
        Modules.get().get(Timer.class).setOverride(1);
    }

    @EventHandler
    private void onPacket(PacketEvent.Receive event) {
        if (event.packet instanceof PlayerPositionLookS2CPacket && rDisable.get() > 0) {
            rubberbands++;
            if (rubberbands >= rDisable.get() && rDisable.get() > 0) {
                this.toggle();
                ChatUtils.error("rubberbanding");
            }
        }
    }

    @EventHandler
    private void onRender(Render3DEvent event) {
        if (curHole != null) {
            BlockPos from = mc.player.getBlockPos().add(0,0.25,0);
            BlockPos to = curHole.add(0,0.1,0);
            int iFrom = event.renderer.lines.vec3(from.getX(), from.getY(), from.getZ()).color(color.get()).next();
            int iTo = event.renderer.lines.vec3(to.getX(), to.getY(), to.getZ()).color(color.get()).next();
            event.renderer.lines.line(iFrom,iTo);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    private void onMove(PlayerMoveEvent event) {
        if (mc.player != null && mc.world != null) {
            BlockPos hole = single.get() ? singleHole : findHole();
            if (hole != null && mc.world.getBlockState(singleHole).getBlock() == Blocks.AIR) {
                curHole = hole;
                Modules.get().get(Timer.class).setOverride(timer.get());
                double yaw =
                    Math.cos(Math.toRadians(
                        getAngle(new Vec3d(hole.getX() + 0.5, hole.getY(), hole.getZ() + 0.5)) + 90.0f));
                double pit =
                    Math.sin(Math.toRadians(
                        getAngle(new Vec3d(hole.getX() + 0.5, hole.getY(), hole.getZ() + 0.5)) + 90.0f));

                if (mc.player.getX() == hole.getX() + 0.5 && mc.player.getZ() == hole.getZ() + 0.5) {
                    if (mc.player.getY() == hole.getY()) {
                        this.toggle();
                        ChatUtils.info("snapped");
                    } else if (LemonKUtils.inside(mc.player, mc.player.getBoundingBox().offset(0, -0.05, 0))){
                        this.toggle();
                        ChatUtils.warning("hole unreachable");
                    } else {
                        ((IVec3d) event.movement).setXZ(0, 0);
                    }
                } else {
                    double x = speed.get() * yaw;
                    double dX = hole.getX() + 0.5 - mc.player.getX();
                    double z = speed.get() * pit;
                    double dZ = hole.getZ() + 0.5 - mc.player.getZ();
                    if (LemonKUtils.inside(mc.player, mc.player.getBoundingBox().offset(x, 0, z))) {
                        collisions++;
                        if (collisions >= coll.get() && coll.get() > 0) {
                            this.toggle();
                            ChatUtils.error("collided");
                        }
                    } else {
                        collisions = 0;
                    }
                    if (ticks > 0) {
                        ticks--;
                    } else if (LemonKUtils.inside(mc.player, mc.player.getBoundingBox().offset(0, -0.05, 0)) && jump.get()) {
                        ticks = jumpCoolDown.get();
                        ((IVec3d) event.movement).setY(0.42);
                    }
                    ((IVec3d) event.movement).setXZ(Math.abs(x) < Math.abs(dX) ? x : dX, Math.abs(z) < Math.abs(dZ) ? z : dZ);
                }
            } else {
                this.toggle();
                ChatUtils.error("no hole found");
            }
        }
    }

    BlockPos findHole() {
        BlockPos closest = null;
        if (LemonKUtils.isHole(mc.player.getBlockPos(), mc.world, depth.get())) {return mc.player.getBlockPos();}
        for (int y = -downRange.get(); y <= 0; y++) {
            for (int x = -range.get(); x <= range.get(); x++) {
                for (int z = -range.get(); z <= range.get(); z++) {
                    BlockPos position = mc.player.getBlockPos().add(x, y, z);
                    if (LemonKUtils.isHole(position, mc.world, depth.get()) && y < 0) {
                        if (closest == null || LemonKUtils.distance(mc.player.getPos(), new Vec3d(position.getX() + 0.5, mc.player.getY(), position.getZ() + 0.5)) <
                            LemonKUtils.distance(mc.player.getPos(), new Vec3d(closest.getX() + 0.5, mc.player.getY(), closest.getZ() + 0.5))) {
                            closest = position;
                        }
                    }
                }
            }
        }
        return closest;
    }

    boolean air(BlockPos pos) {return mc.world.getBlockState(pos).getBlock().equals(Blocks.AIR);}

    float getAngle(Vec3d pos) {
        return (float) Rotations.getYaw(pos);
    }
}
