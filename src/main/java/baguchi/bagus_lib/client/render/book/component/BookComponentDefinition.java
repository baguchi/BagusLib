package baguchi.bagus_lib.client.render.book.component;

import net.minecraft.resources.Identifier;

/*

MIT License
Copyright (c) 2024 LeoMinecraftModding
@link https://github.com/LeoMinecraftModding/eternal-starlight

* */
public record BookComponentDefinition(BookComponent component, Identifier id, int xOffsetL, int yOffsetL,
                                      int xOffsetR, int yOffsetR, boolean enabled) {
    public BookComponentDefinition(BookComponent component, Identifier id, int xOffsetL, int yOffsetL, int xOffsetR, int yOffsetR) {
        this(component, id, xOffsetL, yOffsetL, xOffsetR, yOffsetR, true);
    }
}