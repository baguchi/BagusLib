package bagu_chan.bagus_lib.client.game;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class WaterMelonScreen extends Screen {

    private WaterMelonCraft aprilFoolsWaterMelonCraft = null;
    private float scroll;
    private float scrollSpeed;
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
            aprilFoolsWaterMelonCraft.tick(this);
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics, int p_281550_, int p_282878_, float partialTick) {
        super.render(guiGraphics, p_281550_, p_282878_, partialTick);
        this.scroll = Math.max(0.0F, this.scroll + partialTick * this.scrollSpeed);
        if (aprilFoolsWaterMelonCraft == null) {
            aprilFoolsWaterMelonCraft = new WaterMelonCraft();
        } else {
            aprilFoolsWaterMelonCraft.render(this, guiGraphics, partialTick);
        }
    }

    @Override
    public void renderBackground(GuiGraphics p_282239_, int p_294762_, int p_295473_, float p_296441_) {
        int i = this.width;
        float f = this.scroll * 0.5F;
        p_282239_.setColor(1, 1, 1, 1.0F);
        p_282239_.blit(BACKGROUND_LOCATION, 0, 0, 0, 0.0F, f, i, this.height, 64, 64);
        p_282239_.setColor(1.0F, 1.0F, 1.0F, 1.0F);
    }

}
