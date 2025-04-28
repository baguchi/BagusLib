package baguchi.bagus_lib.client.render.book;

import baguchi.bagus_lib.client.render.book.component.BookComponentDefinition;
import net.minecraft.resources.ResourceLocation;

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

@link https://github.com/LeoMinecraftModding/eternal-starlight/blob/1.21.1-arch/common/src/main/java/cn/leolezury/eternalstarlight/common/client/book/Book.java

* */
public record Book(List<BookComponentDefinition> components, int width, int height, int buttonWidth, int buttonHeight,
                   int buttonXOffset, int buttonYOffset, ResourceLocation background, ResourceLocation cover,
                   ResourceLocation backCover, ResourceLocation leftButton, ResourceLocation rightButton) {
    public void removeDisabled() {
        components().removeIf(d -> !d.enabled());
    }
}
