package baguchi.bagus_lib.client.render.book.component;

import baguchi.bagus_lib.client.render.book.BookAccess;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;

import java.util.ArrayList;
import java.util.List;

/*
MIT License

Copyright (c) 2024 LeoMinecraftModding

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.

@link https://github.com/LeoMinecraftModding/eternal-starlight/blob/1.21.1-arch/common/src/main/java/cn/leolezury/eternalstarlight/common/client/book/component/TextBookComponent.java

* */
public class TextBookComponent extends BookComponent {
    private final Component text;
    private final boolean mustEndAtEvenPage;
    private final List<FormattedCharSequence> cachedComponents = new ArrayList<>();

    public TextBookComponent(Component text, int width, int height) {
        this(text, true, width, height);
    }

    public TextBookComponent(Component text, boolean mustEndAtEvenPage, int width, int height) {
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
        for (int i = access.getRelativePage() * linesPerPage; i < Math.min((access.getRelativePage() + 1) * linesPerPage, cachedComponents.size()); i++) {
            int textY = (i - access.getRelativePage() * linesPerPage) * font.lineHeight;
            graphics.drawString(font, cachedComponents.get(i), x, y + textY, -1, false);
        }
    }

    @Override
    public void tick(BookAccess access, Font font, int x, int y, int mouseX, int mouseY) {

    }


    @Override
    public void onClick(BookAccess access, Font font, int x, int y, int mouseX, int mouseY) {

    }
}