package meteordevelopment.meteorclient.systems.modules.hig;

import meteordevelopment.meteorclient.settings.EnumSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.modules.Categories;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.systems.modules.Modules;
import meteordevelopment.meteorclient.systems.modules.borers.*;
import meteordevelopment.meteorclient.systems.modules.misc.AutoLog;
import meteordevelopment.meteorclient.systems.modules.misc.InvManager;
import meteordevelopment.meteorclient.systems.modules.movement.AutoCenter;
import meteordevelopment.meteorclient.systems.modules.movement.SafeWalk;
import meteordevelopment.meteorclient.systems.modules.movement.ScaffoldPlus;
import meteordevelopment.meteorclient.systems.modules.player.AutoEatPlus;
import meteordevelopment.meteorclient.systems.modules.player.Rotation;
import meteordevelopment.meteorclient.systems.modules.render.FreeLook;
import meteordevelopment.meteorclient.systems.modules.world.LiquidFiller;

public class HighwayTools extends Module {
    public enum Mode {
        HighwayBuilding,
        AxisDigging,
        NegNegDigging,
        NegPosDigging,
        PosNegDigging,
        PosPosDigging,
        RingRoadDigging
    }

    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<Mode> mode = sgGeneral.add(new EnumSetting.Builder<Mode>()
            .name("profile")
            .description("Which highway profile to use.")
            .defaultValue(Mode.HighwayBuilding)
            .build()
    );

    public HighwayTools() {
        super(Categories.Hig, "highway-tools", "Digs, builds and repairs highways automatically.");
    }

    @Override
    public void onActivate() {
        Modules modules = Modules.get();

        if (mode.get() == Mode.HighwayBuilding) {
            modules.get(HighwayBuilderPlus.class).toggle();

            modules.get(AutoEatPlus.class).toggle();
            modules.get(AutoLog.class).toggle();
            modules.get(FreeLook.class).toggle();
            modules.get(HandManager.class).toggle();
            modules.get(InvManager.class).toggle();
            modules.get(LiquidFiller.class).toggle();
            modules.get(Rotation.class).toggle();
            modules.get(SafeWalk.class).toggle();
            modules.get(ScaffoldPlus.class).toggle();
        }


        if (mode.get() == Mode.AxisDigging) {
            modules.get(meteordevelopment.meteorclient.systems.modules.borers.AxisBorer.class).toggle();

            modules.get(AutoEatPlus.class).toggle();
            modules.get(AutoLog.class).toggle();
            modules.get(FreeLook.class).toggle();
            modules.get(HandManager.class).toggle();
            modules.get(InvManager.class).toggle();
            modules.get(LiquidFiller.class).toggle();
            modules.get(Rotation.class).toggle();
            modules.get(SafeWalk.class).toggle();
            modules.get(ScaffoldPlus.class).toggle();
        }


        if (mode.get() == Mode.NegNegDigging) {
            modules.get(meteordevelopment.meteorclient.systems.modules.borers.NegNegBorer.class).toggle();

            modules.get(AutoEatPlus.class).toggle();
            modules.get(AutoCenter.class).toggle();
            modules.get(AutoLog.class).toggle();
            modules.get(FreeLook.class).toggle();
            modules.get(HandManager.class).toggle();
            modules.get(InvManager.class).toggle();
            modules.get(LiquidFiller.class).toggle();
            modules.get(Rotation.class).toggle();
            modules.get(SafeWalk.class).toggle();
            modules.get(ScaffoldPlus.class).toggle();
        }


        if (mode.get() == Mode.NegPosDigging) {
            modules.get(meteordevelopment.meteorclient.systems.modules.borers.NegPosBorer.class).toggle();

            modules.get(AutoEatPlus.class).toggle();
            modules.get(AutoCenter.class).toggle();
            modules.get(AutoLog.class).toggle();
            modules.get(FreeLook.class).toggle();
            modules.get(HandManager.class).toggle();
            modules.get(InvManager.class).toggle();
            modules.get(LiquidFiller.class).toggle();
            modules.get(Rotation.class).toggle();
            modules.get(SafeWalk.class).toggle();
            modules.get(ScaffoldPlus.class).toggle();
        }


        if (mode.get() == Mode.PosNegDigging) {
            modules.get(meteordevelopment.meteorclient.systems.modules.borers.PosNegBorer.class).toggle();

            modules.get(AutoEatPlus.class).toggle();
            modules.get(AutoCenter.class).toggle();
            modules.get(AutoLog.class).toggle();
            modules.get(FreeLook.class).toggle();
            modules.get(HandManager.class).toggle();
            modules.get(InvManager.class).toggle();
            modules.get(LiquidFiller.class).toggle();
            modules.get(Rotation.class).toggle();
            modules.get(SafeWalk.class).toggle();
            modules.get(ScaffoldPlus.class).toggle();
        }


        if (mode.get() == Mode.PosPosDigging) {
            modules.get(meteordevelopment.meteorclient.systems.modules.borers.PosPosBorer.class).toggle();

            modules.get(AutoEatPlus.class).toggle();
            modules.get(AutoCenter.class).toggle();
            modules.get(AutoLog.class).toggle();
            modules.get(FreeLook.class).toggle();
            modules.get(HandManager.class).toggle();
            modules.get(InvManager.class).toggle();
            modules.get(LiquidFiller.class).toggle();
            modules.get(Rotation.class).toggle();
            modules.get(SafeWalk.class).toggle();
            modules.get(ScaffoldPlus.class).toggle();
        }


        if (mode.get() == Mode.RingRoadDigging) {
            modules.get(meteordevelopment.meteorclient.systems.modules.borers.RingRoadBorer.class).toggle();

            modules.get(AutoEatPlus.class).toggle();
            modules.get(AutoLog.class).toggle();
            modules.get(FreeLook.class).toggle();
            modules.get(HandManager.class).toggle();
            modules.get(InvManager.class).toggle();
            modules.get(LiquidFiller.class).toggle();
            modules.get(Rotation.class).toggle();
            modules.get(SafeWalk.class).toggle();
            modules.get(ScaffoldPlus.class).toggle();
        }
    }

    @Override
    public void onDeactivate() {
        Modules modules = Modules.get();

        if (mode.get() == Mode.HighwayBuilding) {
            if (modules.get(HighwayBuilderPlus.class).isActive())
                modules.get(HighwayBuilderPlus.class).toggle();

            if (modules.get(AutoEatPlus.class).isActive())
                modules.get(AutoEatPlus.class).toggle();
            if (modules.get(AutoLog.class).isActive())
                modules.get(AutoLog.class).toggle();
            if (modules.get(FreeLook.class).isActive())
                modules.get(FreeLook.class).toggle();
            if (modules.get(HandManager.class).isActive())
                modules.get(HandManager.class).toggle();
            if (modules.get(InvManager.class).isActive())
                modules.get(InvManager.class).toggle();
            if (modules.get(LiquidFiller.class).isActive())
                modules.get(LiquidFiller.class).toggle();
            if (modules.get(Rotation.class).isActive())
                modules.get(Rotation.class).toggle();
            if (modules.get(SafeWalk.class).isActive())
                modules.get(SafeWalk.class).toggle();
            if (modules.get(ScaffoldPlus.class).isActive())
                modules.get(ScaffoldPlus.class).toggle();
        }


        if (mode.get() == Mode.AxisDigging) {
            if (modules.get(meteordevelopment.meteorclient.systems.modules.borers.AxisBorer.class).isActive())
                modules.get(AxisBorer.class).toggle();

            if (modules.get(AutoEatPlus.class).isActive())
                modules.get(AutoEatPlus.class).toggle();
            if (modules.get(AutoLog.class).isActive())
                modules.get(AutoLog.class).toggle();
            if (modules.get(FreeLook.class).isActive())
                modules.get(FreeLook.class).toggle();
            if (modules.get(HandManager.class).isActive())
                modules.get(HandManager.class).toggle();
            if (modules.get(InvManager.class).isActive())
                modules.get(InvManager.class).toggle();
            if (modules.get(LiquidFiller.class).isActive())
                modules.get(LiquidFiller.class).toggle();
            if (modules.get(Rotation.class).isActive())
                modules.get(Rotation.class).toggle();
            if (modules.get(SafeWalk.class).isActive())
                modules.get(SafeWalk.class).toggle();
            if (modules.get(ScaffoldPlus.class).isActive())
                modules.get(ScaffoldPlus.class).toggle();
        }


        if (mode.get() == Mode.NegNegDigging) {
            if (modules.get(meteordevelopment.meteorclient.systems.modules.borers.NegNegBorer.class).isActive())
                modules.get(NegNegBorer.class).toggle();

            if (modules.get(AutoEatPlus.class).isActive())
                modules.get(AutoEatPlus.class).toggle();
            if (modules.get(AutoCenter.class).isActive())
                modules.get(AutoCenter.class).toggle();
            if (modules.get(AutoLog.class).isActive())
                modules.get(AutoLog.class).toggle();
            if (modules.get(FreeLook.class).isActive())
                modules.get(FreeLook.class).toggle();
            if (modules.get(HandManager.class).isActive())
                modules.get(HandManager.class).toggle();
            if (modules.get(InvManager.class).isActive())
                modules.get(InvManager.class).toggle();
            if (modules.get(LiquidFiller.class).isActive())
                modules.get(LiquidFiller.class).toggle();
            if (modules.get(Rotation.class).isActive())
                modules.get(Rotation.class).toggle();
            if (modules.get(SafeWalk.class).isActive())
                modules.get(SafeWalk.class).toggle();
            if (modules.get(ScaffoldPlus.class).isActive())
                modules.get(ScaffoldPlus.class).toggle();
        }


        if (mode.get() == Mode.NegPosDigging) {
            if (modules.get(meteordevelopment.meteorclient.systems.modules.borers.NegPosBorer.class).isActive())
                modules.get(NegPosBorer.class).toggle();

            if (modules.get(AutoEatPlus.class).isActive())
                modules.get(AutoEatPlus.class).toggle();
            if (modules.get(AutoCenter.class).isActive())
                modules.get(AutoCenter.class).toggle();
            if (modules.get(AutoLog.class).isActive())
                modules.get(AutoLog.class).toggle();
            if (modules.get(FreeLook.class).isActive())
                modules.get(FreeLook.class).toggle();
            if (modules.get(HandManager.class).isActive())
                modules.get(HandManager.class).toggle();
            if (modules.get(InvManager.class).isActive())
                modules.get(InvManager.class).toggle();
            if (modules.get(LiquidFiller.class).isActive())
                modules.get(LiquidFiller.class).toggle();
            if (modules.get(Rotation.class).isActive())
                modules.get(Rotation.class).toggle();
            if (modules.get(SafeWalk.class).isActive())
                modules.get(SafeWalk.class).toggle();
            if (modules.get(ScaffoldPlus.class).isActive())
                modules.get(ScaffoldPlus.class).toggle();
        }


        if (mode.get() == Mode.PosNegDigging) {
            if (modules.get(meteordevelopment.meteorclient.systems.modules.borers.PosNegBorer.class).isActive())
                modules.get(PosNegBorer.class).toggle();

            if (modules.get(AutoEatPlus.class).isActive())
                modules.get(AutoEatPlus.class).toggle();
            if (modules.get(AutoCenter.class).isActive())
                modules.get(AutoCenter.class).toggle();
            if (modules.get(AutoLog.class).isActive())
                modules.get(AutoLog.class).toggle();
            if (modules.get(FreeLook.class).isActive())
                modules.get(FreeLook.class).toggle();
            if (modules.get(HandManager.class).isActive())
                modules.get(HandManager.class).toggle();
            if (modules.get(InvManager.class).isActive())
                modules.get(InvManager.class).toggle();
            if (modules.get(LiquidFiller.class).isActive())
                modules.get(LiquidFiller.class).toggle();
            if (modules.get(Rotation.class).isActive())
                modules.get(Rotation.class).toggle();
            if (modules.get(SafeWalk.class).isActive())
                modules.get(SafeWalk.class).toggle();
            if (modules.get(ScaffoldPlus.class).isActive())
                modules.get(ScaffoldPlus.class).toggle();
        }


        if (mode.get() == Mode.PosPosDigging) {
            if (modules.get(meteordevelopment.meteorclient.systems.modules.borers.PosPosBorer.class).isActive())
                modules.get(PosPosBorer.class).toggle();

            if (modules.get(AutoEatPlus.class).isActive())
                modules.get(AutoEatPlus.class).toggle();
            if (modules.get(AutoCenter.class).isActive())
                modules.get(AutoCenter.class).toggle();
            if (modules.get(AutoLog.class).isActive())
                modules.get(AutoLog.class).toggle();
            if (modules.get(FreeLook.class).isActive())
                modules.get(FreeLook.class).toggle();
            if (modules.get(HandManager.class).isActive())
                modules.get(HandManager.class).toggle();
            if (modules.get(InvManager.class).isActive())
                modules.get(InvManager.class).toggle();
            if (modules.get(LiquidFiller.class).isActive())
                modules.get(LiquidFiller.class).toggle();
            if (modules.get(Rotation.class).isActive())
                modules.get(Rotation.class).toggle();
            if (modules.get(SafeWalk.class).isActive())
                modules.get(SafeWalk.class).toggle();
            if (modules.get(ScaffoldPlus.class).isActive())
                modules.get(ScaffoldPlus.class).toggle();
        }


        if (mode.get() == Mode.RingRoadDigging) {
            if (modules.get(meteordevelopment.meteorclient.systems.modules.borers.RingRoadBorer.class).isActive())
                modules.get(RingRoadBorer.class).toggle();

            if (modules.get(AutoEatPlus.class).isActive())
                modules.get(AutoEatPlus.class).toggle();
            if (modules.get(AutoLog.class).isActive())
                modules.get(AutoLog.class).toggle();
            if (modules.get(FreeLook.class).isActive())
                modules.get(FreeLook.class).toggle();
            if (modules.get(HandManager.class).isActive())
                modules.get(HandManager.class).toggle();
            if (modules.get(InvManager.class).isActive())
                modules.get(InvManager.class).toggle();
            if (modules.get(LiquidFiller.class).isActive())
                modules.get(LiquidFiller.class).toggle();
            if (modules.get(Rotation.class).isActive())
                modules.get(Rotation.class).toggle();
            if (modules.get(SafeWalk.class).isActive())
                modules.get(SafeWalk.class).toggle();
            if (modules.get(ScaffoldPlus.class).isActive())
                modules.get(ScaffoldPlus.class).toggle();
        }
    }
}
