package meteordevelopment.meteorclient.gui.themes.lemon.widgets.input;

import meteordevelopment.meteorclient.gui.renderer.GuiRenderer;
import meteordevelopment.meteorclient.gui.themes.lemon.LemonGuiTheme;
import meteordevelopment.meteorclient.gui.themes.lemon.LemonWidget;
import meteordevelopment.meteorclient.gui.widgets.input.WDropdown;
import meteordevelopment.meteorclient.utils.render.color.Color;

public class WLemonDropdown<T> extends WDropdown<T> implements LemonWidget {
    public WLemonDropdown(T[] values, T value) {
        super(values, value);
    }

    @Override
    protected WDropdownRoot createRootWidget() {
        return new WRoot();
    }

    @Override
    protected WDropdownValue createValueWidget() {
        return new WValue();
    }

    @Override
    protected void onRender(GuiRenderer renderer, double mouseX, double mouseY, double delta) {
        LemonGuiTheme theme = theme();
        double pad = pad();
        double s = theme.textHeight();

        renderBackground(renderer, this, pressed, mouseOver);

        String text = get().toString();
        double w = theme.textWidth(text);
        renderer.text(text, x + pad + maxValueWidth / 2 - w / 2, y + pad, theme.textColor.get(), false);

        renderer.rotatedQuad(x + pad + maxValueWidth + pad, y + pad, s, s, 0, GuiRenderer.TRIANGLE, theme.textColor.get());
    }

    private static class WRoot extends WDropdownRoot implements LemonWidget {
        @Override
        protected void onRender(GuiRenderer renderer, double mouseX, double mouseY, double delta) {
            LemonGuiTheme theme = theme();
            double s = theme.scale(2);
            Color c = theme.outlineColor.get();

            renderer.quad(x, y + height - s, width, s, c);
            renderer.quad(x, y, s, height - s, c);
            renderer.quad(x + width - s, y, s, height - s, c);
        }
    }

    private class WValue extends WDropdownValue implements LemonWidget {
        @Override
        protected void onCalculateSize() {
            double pad = pad();

            width = pad + theme.textWidth(value.toString()) + pad;
            height = pad + theme.textHeight() + pad;
        }

        @Override
        protected void onRender(GuiRenderer renderer, double mouseX, double mouseY, double delta) {
            LemonGuiTheme theme = theme();

            Color color = theme.backgroundColor.get(pressed, mouseOver, true);
            int preA = color.a;
            color.a += color.a / 2;
            color.validate();

            renderer.quad(this, color);

            color.a = preA;

            String text = value.toString();
            renderer.text(text, x + width / 2 - theme.textWidth(text) / 2, y + pad(), theme.textColor.get(), false);
        }
    }
}
