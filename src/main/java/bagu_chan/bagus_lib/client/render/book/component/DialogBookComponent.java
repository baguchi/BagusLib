package bagu_chan.bagus_lib.client.render.book.component;

import bagu_chan.bagus_lib.client.render.book.BookAccess;
import bagu_chan.bagus_lib.util.DialogHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.Style;
import net.minecraft.util.FormattedCharSequence;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DialogBookComponent extends BookComponent {
    private final Component text;
    private final boolean mustEndAtEvenPage;
    private final List<FormattedCharSequence> cachedComponents = new ArrayList<>();

    protected DialogHandler.DrawString dialogues;

    public DialogBookComponent(Component text, int width, int height) {
        this(text, true, width, height);
    }

    public DialogBookComponent(Component text, boolean mustEndAtEvenPage, int width, int height) {
        super(width, height);
        this.text = text;
        this.mustEndAtEvenPage = mustEndAtEvenPage;
    }

    @Override
    public int getPageCount(int pagesBefore, Font font) {
        if (cachedComponents.isEmpty()) {
            cachedComponents.addAll(font.split(text, width));
        }
        int linesPerPage = height / font.lineHeight;
        int pageCount = cachedComponents.size() % linesPerPage == 0 ? cachedComponents.size() / linesPerPage : cachedComponents.size() / linesPerPage + 1;
        return mustEndAtEvenPage ? (pageCount % 2 == 0 ? (pageCount + pagesBefore % 2) : (pageCount + (1 - pagesBefore % 2))) : pageCount;
    }

    @Override
    public void render(BookAccess access, GuiGraphics graphics, Font font, int x, int y, int mouseX, int mouseY) {
        int linesPerPage = height / font.lineHeight;
        float g = Minecraft.getInstance().gui.getGuiTicks() + Minecraft.getInstance().getPartialTick();
        if (dialogues == null) {
            dialogues = beginString(graphics, g, 2, Minecraft.getInstance().font, text, 0x000000, width);
        } else {
            DialogHandler.DrawString drawString = this.dialogues;
            drawString.draw(g, x, y);
        }
    }

    @Override
    public void tick(BookAccess access, Font font, int x, int y, int mouseX, int mouseY) {

    }


    @Override
    public void onClick(BookAccess access, Font font, int x, int y, int mouseX, int mouseY) {

    }

    public DialogHandler.DrawString beginString(GuiGraphics guiGraphics, double lastTick, double perTick, Font font, FormattedText string2, int i, int j2) {
        List<FormattedText> list = font.getSplitter().splitLines(string2, j2, Style.EMPTY);
        String string22 = list.stream().map(FormattedText::getString).collect(Collectors.joining("\n"));
        return new DialogHandler.DrawString(lastTick, perTick, string22, (string, j, k) -> {
            String[] strings = string.split("\\r?\\n");
            int l = k;
            for (String string3 : strings) {
                guiGraphics.drawString(font, string3, j, l, i, false);
                l += font.lineHeight + 4;
            }
        });
    }
}