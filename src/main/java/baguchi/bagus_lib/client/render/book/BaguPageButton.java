package baguchi.bagus_lib.client.render.book;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.sounds.SoundEvents;


public class BaguPageButton extends Button {
    private final Book book;
    private final boolean isForward;
    private final boolean playTurnSound;

    public BaguPageButton(int x, int y, Book book, boolean isForward, boolean turnSound, Button.OnPress onPress) {
        super(x + (isForward ? -1 : 1) * book.buttonXOffset(), y - book.buttonYOffset(), book.buttonWidth(), book.buttonHeight(), CommonComponents.EMPTY, onPress, DEFAULT_NARRATION);
        this.book = book;
        this.isForward = isForward;
        this.playTurnSound = turnSound;
    }

    @Override
    protected void extractContents(GuiGraphicsExtractor guiGraphicsExtractor, int i, int i1, float v) {
        guiGraphicsExtractor.blit(RenderPipelines.GUI_TEXTURED, isForward ? book.rightButton() : book.leftButton(), this.getX(), this.getY(), 0, 0, book.buttonWidth(), book.buttonHeight(), book.buttonWidth(), book.buttonHeight());

    }

    @Override
    public void playDownSound(SoundManager soundManager) {
        if (this.playTurnSound) {
            soundManager.play(SimpleSoundInstance.forUI(SoundEvents.BOOK_PAGE_TURN, 1.0F));
        }
    }
}