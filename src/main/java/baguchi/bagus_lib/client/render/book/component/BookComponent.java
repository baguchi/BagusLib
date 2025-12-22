package baguchi.bagus_lib.client.render.book.component;

import baguchi.bagus_lib.client.render.book.BookAccess;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;

/*

MIT License
Copyright (c) 2024 LeoMinecraftModding
@link https://github.com/LeoMinecraftModding/eternal-starlight

* */
public abstract class BookComponent {
    protected final int width;
    protected final int height;

    public BookComponent(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public abstract int getPageCount(int pagesBefore, Font font);

    public abstract void render(BookAccess access, GuiGraphics graphics, Font font, int x, int y, int mouseX, int mouseY);

    public abstract void tick(BookAccess access, Font font, int x, int y, int mouseX, int mouseY);

    public abstract void onClick(BookAccess access, Font font, int x, int y, int mouseX, int mouseY);
}