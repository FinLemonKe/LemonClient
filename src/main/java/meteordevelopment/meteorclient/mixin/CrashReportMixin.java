package meteordevelopment.meteorclient.mixin;

import meteordevelopment.meteorclient.LemonClient;
import meteordevelopment.meteorclient.systems.modules.Category;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.systems.modules.Modules;
import net.minecraft.util.crash.CrashReport;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(CrashReport.class)
public class CrashReportMixin {
    @Inject(method = "addStackTrace", at = @At("TAIL"))
    private void onAddStackTrace(StringBuilder sb, CallbackInfo info) {
        if (Modules.get() != null) {
            sb.append("\n\n");
            sb.append("-- Meteor Client --\n");
            sb.append("Version: ").append(LemonClient.VERSION).append("\n");

            if (!LemonClient.DEV_BUILD.isEmpty()) {
                sb.append("Dev Build: ").append(LemonClient.DEV_BUILD).append("\n");
            }

            for (Category category : Modules.loopCategories()) {
                List<Module> modules = Modules.get().getGroup(category);
                boolean active = false;

                for (Module module : modules) {
                    if (module != null && module.isActive()) {
                        active = true;
                        break;
                    }
                }

                if (active) {
                    sb.append("\n");
                    sb.append("[").append(category).append("]:").append("\n");

                    for (Module module : modules) {
                        if (module != null && module.isActive()) {
                            sb.append(module.name).append("\n");
                        }
                    }
                }
            }
        }
    }
}
