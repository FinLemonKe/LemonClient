package meteordevelopment.meteorclient.utils.other;

import meteordevelopment.meteorclient.enums.RotationType;
import meteordevelopment.meteorclient.enums.SwingState;
import meteordevelopment.meteorclient.enums.SwingType;
import meteordevelopment.meteorclient.systems.modules.Modules;
import meteordevelopment.meteorclient.systems.modules.settings.*;
import meteordevelopment.meteorclient.utils.Utils;
import meteordevelopment.meteorclient.utils.world.PlaceData;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

import java.util.function.Predicate;

public class SettingUtils extends Utils {

    private static final FacingSettings facing = Modules.get().get(FacingSettings.class);
    private static final RangeSettings range = Modules.get().get(RangeSettings.class);
    private static final RaytraceSettings raytrace = Modules.get().get(RaytraceSettings.class);
    private static final RotationSettings rotation = Modules.get().get(RotationSettings.class);
    private static final SwingSettings swing = Modules.get().get(SwingSettings.class);

    //  Range
    public static double getPlaceRange() {
        return range.placeRange.get();
    }
    public static double getPlaceWallsRange() {
        return range.placeRangeWalls.get();
    }
    public static double getAttackRange() {
        return range.attackRange.get();
    }
    public static double getAttackWallsRange() {
        return range.attackRangeWalls.get();
    }
    public static double getMineRange() {
        return range.miningRange.get();
    }
    public static double getMineWallsRange() {
        return range.miningRangeWalls.get();
    }
    public static double placeRangeTo(BlockPos pos) {return range.placeRangeTo(pos, null);}
    public static boolean inPlaceRange(BlockPos pos) {
        return range.inPlaceRange(pos);
    }
    public static boolean inPlaceRangeNoTrace(BlockPos pos) {
        return range.inPlaceRangeNoTrace(pos);
    }
    public static boolean inAttackRange(Box bb) {
        return range.inAttackRange(bb);
    }
    public static boolean inAttackRange(Box bb, Vec3d feet) {return range.inAttackRange(bb, feet);}
    public static double mineRangeTo(BlockPos pos) {return range.miningRangeTo(pos, null);}
    public static boolean inMineRange(BlockPos pos) {
        return range.inMineRange(pos);
    }
    public static boolean inMineRangeNoTrace(BlockPos pos) {
        return range.inMineRangeNoTrace(pos);
    }
    public static boolean inAttackRangeNoTrace(Box bb, double eyeHeight, Vec3d feet) {return range.inAttackRangeNoTrace(bb, feet);}
    public static double attackRangeTo(Box bb, Vec3d feet) {return range.attackRangeTo(bb, feet, null);}


    //  Rotate
    public static int rotationPackets() {return rotation.packets.get();}
    public static boolean startMineRot() {
        return rotation.startMineRot();
    }
    public static boolean endMineRot() {
        return rotation.endMineRot();
    }
    public static boolean shouldGhostRotate() {return rotation.ghostRotation.get();}
    public static boolean shouldGhostCheck() {return rotation.rotationCheckMode.get() == RotationSettings.RotationCheckMode.Ghost;}
    public static boolean shouldVanillaRotate() {return rotation.vanillaRotation.get();}
    public static Vec3d getGhostRot(BlockPos pos, Vec3d vec) {return rotation.getGhostRot(pos, vec);}
    public static Vec3d getGhostRot(Box box, Vec3d vec) {return rotation.getGhostRot(box, vec);}
    public static boolean shouldRotate(RotationType type) {
        return rotation.shouldRotate(type);
    }
    public static boolean rotationCheckHistory(Box box, RotationType type) {return rotation.rotationCheckHistory(box, rotation.getExisted(type) + 1);}
    public static boolean rotationCheck(Vec3d pPos, double yaw, double pitch, Box box, RotationType type) {return rotation.rotationCheck(pPos, yaw, pitch, box, rotation.getExisted(type));}
    public static boolean raytraceCheck(Vec3d vec, double yaw, double pitch, Box box) {return rotation.raytraceCheck(vec, yaw, pitch, box);}
    public static boolean raytraceCheck(Vec3d vec, double yaw, double pitch, BlockPos pos) {return rotation.raytraceCheck(vec, yaw, pitch, pos);}

    //  Swing
    public static void swing(SwingState state, SwingType type) {
        swing.swing(state, type);
    }
    public static void mineSwing(SwingSettings.MiningSwingState state) {
        swing.mineSwing(state);
    }

    //  Facing
    public static PlaceData getPlaceData(BlockPos pos) {return facing.getPlaceData(pos, true);}
    public static PlaceData getPlaceDataAND(BlockPos pos, Predicate<BlockPos> predicate) {return facing.getPlaceDataAND(pos, predicate, true);}
    public static PlaceData getPlaceDataOR(BlockPos pos, Predicate<BlockPos> predicate) {return facing.getPlaceDataOR(pos, predicate, true);}
    public static PlaceData getPlaceData(BlockPos pos, boolean ignoreContainers) {return facing.getPlaceData(pos, ignoreContainers);}
    public static PlaceData getPlaceDataAND(BlockPos pos, Predicate<BlockPos> predicate, boolean ignoreContainers) {return facing.getPlaceDataAND(pos, predicate, ignoreContainers);}
    public static PlaceData getPlaceDataOR(BlockPos pos, Predicate<BlockPos> predicate, boolean ignoreContainers) {return facing.getPlaceDataOR(pos, predicate, ignoreContainers);}
    public static Direction getPlaceOnDirection(BlockPos pos) {
        return facing.getPlaceOnDirection(pos);
    }

    //  Raytracing
    public static boolean shouldPlaceTrace() {
        return raytrace.placeTrace.get();
    }
    public static boolean shouldAttackTrace() {return raytrace.attackTrace.get();}
    public static boolean placeTrace(BlockPos pos) {
        return !shouldPlaceTrace() || raytrace.placeTrace(pos);
    }
    public static boolean attackTrace(Box bb) {
        return !shouldAttackTrace() || raytrace.attackTrace(bb);
    }
}
