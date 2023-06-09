package meteordevelopment.meteorclient.systems.modules.settings;

import meteordevelopment.meteorclient.enums.SwingState;
import meteordevelopment.meteorclient.enums.SwingType;
import meteordevelopment.meteorclient.settings.EnumSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.modules.Categories;
import meteordevelopment.meteorclient.systems.modules.Module;
import net.minecraft.network.packet.c2s.play.HandSwingC2SPacket;
import net.minecraft.util.Hand;

public class SwingSettings extends Module {
    public SwingSettings() {
        super(Categories.Settings, "Swing", "Global swing settings for every blackout module");
    }

    private final SettingGroup sgCrystal = settings.createGroup("Crystal");
    private final SettingGroup sgInteract = settings.createGroup("Interact");
    private final SettingGroup sgMining = settings.createGroup("Mining");
    private final SettingGroup sgPlace = settings.createGroup("Placing");
    private final SettingGroup sgAttack = settings.createGroup("Attacking");
    private final SettingGroup sgUse = settings.createGroup("Using");
    private final Setting<SwingMode> crystalPlace = sgCrystal.add(new EnumSetting.Builder<SwingMode>()
        .name("Crystal Place Swing")
        .defaultValue(SwingMode.Disabled)
        .build()
    );
    private final Setting<SwingHand> crystalHand = sgCrystal.add(new EnumSetting.Builder<SwingHand>()
        .name("Crystal Hand")
        .defaultValue(SwingHand.MainHand)
        .build()
    );
    private final Setting<SwingState> crystalState = sgCrystal.add(new EnumSetting.Builder<SwingState>()
        .name("Crystal Swing State")
        .defaultValue(SwingState.Post)
        .build()
    );
    private final Setting<SwingMode> interact = sgInteract.add(new EnumSetting.Builder<SwingMode>()
        .name("Interact Swing")
        .defaultValue(SwingMode.Full)
        .build()
    );private final Setting<SwingHand> interactHand = sgInteract.add(new EnumSetting.Builder<SwingHand>()
        .name("Interact Hand")
        .defaultValue(SwingHand.MainHand)
        .build()
    );
    private final Setting<SwingState> interactState = sgInteract.add(new EnumSetting.Builder<SwingState>()
        .name("Interact Swing State")
        .defaultValue(SwingState.Post)
        .build()
    );
    private final Setting<SwingMode> mining = sgMining.add(new EnumSetting.Builder<SwingMode>()
        .name("Mining Swing")
        .defaultValue(SwingMode.Full)
        .build()
    );
    private final Setting<SwingHand> miningHand = sgMining.add(new EnumSetting.Builder<SwingHand>()
        .name("Mining Hand")
        .defaultValue(SwingHand.MainHand)
        .build()
    );
    private final Setting<MiningSwingState> miningState = sgMining.add(new EnumSetting.Builder<MiningSwingState>()
        .name("Mining Swing State")
        .defaultValue(MiningSwingState.End)
        .build()
    );
    private final Setting<SwingMode> placing = sgPlace.add(new EnumSetting.Builder<SwingMode>()
        .name("Placing Swing")
        .defaultValue(SwingMode.Full)
        .build()
    );
    private final Setting<SwingHand> placinghand = sgPlace.add(new EnumSetting.Builder<SwingHand>()
        .name("Placing Hand")
        .defaultValue(SwingHand.MainHand)
        .build()
    );
    private final Setting<SwingState> placingState = sgPlace.add(new EnumSetting.Builder<SwingState>()
        .name("Placing Swing State")
        .defaultValue(SwingState.Post)
        .build()
    );
    private final Setting<SwingMode> attacking = sgAttack.add(new EnumSetting.Builder<SwingMode>()
        .name("Attacking Swing")
        .defaultValue(SwingMode.Full)
        .build()
    );
    private final Setting<SwingHand> attackingHand = sgAttack.add(new EnumSetting.Builder<SwingHand>()
        .name("Attacking Hand")
        .defaultValue(SwingHand.MainHand)
        .build()
    );
    private final Setting<SwingState> attackingState = sgAttack.add(new EnumSetting.Builder<SwingState>()
        .name("Attacking Swing State")
        .defaultValue(SwingState.Post)
        .build()
    );
    private final Setting<SwingMode> using = sgUse.add(new EnumSetting.Builder<SwingMode>()
        .name("Using Swing")
        .defaultValue(SwingMode.Full)
        .build()
    );
    private final Setting<SwingHand> usingHand = sgUse.add(new EnumSetting.Builder<SwingHand>()
        .name("Using Hand")
        .defaultValue(SwingHand.MainHand)
        .build()
    );
    private final Setting<SwingState> usingState = sgUse.add(new EnumSetting.Builder<SwingState>()
        .name("Using Swing State")
        .defaultValue(SwingState.Post)
        .build()
    );

    public enum SwingMode {
        Disabled,
        Client,
        Packet,
        Full
    }
    public enum MiningSwingState {
        Full,
        Start,
        End,
        Double
    }
    public enum SwingHand {
        MainHand,
        OffHand
    }

    public void swing(SwingState state, SwingType type) {
        if (mc.player == null) {return;}
        if (!state.equals(getState(type))) {return;}
        Hand hand = gethand(type);
        switch (type) {
            case Crystal -> swing(crystalPlace.get(), hand);
            case Interact -> swing(interact.get(), hand);
            case Placing -> swing(placing.get(), hand);
            case Attacking -> swing(attacking.get(), hand);
            case Using -> swing(using.get(), hand);
        }
    }
    public void mineSwing(MiningSwingState state) {
        switch (state) {
            case Start -> {
                if (miningState.get() != MiningSwingState.Double && miningState.get() != MiningSwingState.Start && miningState.get() != MiningSwingState.Full) {
                    return;
                }
            }
            case End -> {
                if (miningState.get() != MiningSwingState.Double && miningState.get() != MiningSwingState.End && miningState.get() != MiningSwingState.Full) {
                    return;
                }
            }
            case Full -> {
                if (miningState.get() != MiningSwingState.Full) {
                    return;
                }
            }
        }
        if (mc.player == null) {return;}
        Hand hand = gethand(SwingType.Mining);
        swing(mining.get(), hand);
    }
    Hand gethand(SwingType type) {
        SwingHand swingHand = SwingHand.MainHand;
        switch (type) {
            case Crystal -> swingHand = crystalHand.get();
            case Interact -> swingHand = interactHand.get();
            case Mining -> swingHand = miningHand.get();
            case Placing -> swingHand = placinghand.get();
            case Attacking -> swingHand = attackingHand.get();
            case Using -> swingHand = usingHand.get();
        }
        return getHand(swingHand);
    }
    Hand getHand(SwingHand hand) {
        switch (hand) {
            case MainHand -> {
                return Hand.MAIN_HAND;
            }
            case OffHand -> {
                return Hand.OFF_HAND;
            }
        }
        return Hand.MAIN_HAND;
    }
    SwingState getState(SwingType type) {
        switch (type) {
            case Crystal -> {
                return crystalState.get();
            }
            case Interact -> {
                return interactState.get();
            }
            case Mining -> {

            }
            case Placing -> {
                return placingState.get();
            }
            case Attacking -> {
                return attackingState.get();
            }
            case Using -> {
                return usingState.get();
            }
        }
        return SwingState.Post;
    }
    void swing(SwingMode mode, Hand hand) {
        switch (mode) {
            case Full -> mc.player.swingHand(hand);
            case Packet -> mc.player.networkHandler.sendPacket(new HandSwingC2SPacket(hand));
            case Client -> mc.player.swingHand(hand, true);
        }
    }
}
