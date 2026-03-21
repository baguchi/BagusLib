package baguchi.bagus_lib.client.game;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class WaterMelonScreen extends Screen {

    private WaterMelonCraft aprilFoolsWaterMelonCraft = null;
    private float scroll;
    private final float scrollSpeed;
    private final float unmodifiedScrollSpeed;

    public WaterMelonScreen(Component p_96550_) {
        super(p_96550_);
        this.unmodifiedScrollSpeed = 0.5F;
        this.scrollSpeed = this.unmodifiedScrollSpeed;
    }

    @Override
    public void tick() {
        super.tick();
        if (aprilFoolsWaterMelonCraft != null) {
            aprilFoolsWaterMelonCraft.tick();
        }
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        super.extractRenderState(graphics, mouseX, mouseY, partialTick);
        this.scroll = Math.max(0.0F, this.scroll + partialTick * this.scrollSpeed);
        if (aprilFoolsWaterMelonCraft == null) {
            aprilFoolsWaterMelonCraft = new WaterMelonCraft();
        } else {
            aprilFoolsWaterMelonCraft.render(this, graphics, partialTick);
        }
    }

}
