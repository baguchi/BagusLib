package bagu_chan.bagus_lib.client.render.book.component;

import bagu_chan.bagus_lib.client.render.book.BookAccess;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.StreamSupport;

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

@link https://github.com/LeoMinecraftModding/eternal-starlight/blob/1.21.1-arch/common/src/main/java/cn/leolezury/eternalstarlight/common/client/book/component/DisplayBookComponent.java

* */
public class DisplayBookComponent extends BookComponent {
    private final List<TextDisplay> textDisplays = new ArrayList<>();
    private final List<EntityDisplay> entityDisplays = new ArrayList<>();
    private final Map<EntityDisplay, LivingEntity> entities = new HashMap<>();
    private final List<ItemDisplay> itemDisplays = new ArrayList<>();
    private final List<ItemTagDisplay> itemTagDisplays = new ArrayList<>();
    private final List<ImageDisplay> imageDisplays = new ArrayList<>();

    private int tickCount;

    public DisplayBookComponent(int width, int height) {
        super(width, height);
    }

    @Override
    public int getPageCount(int pagesBefore, Font font) {
        return 1;
    }

    @Override
    public void render(BookAccess access, GuiGraphics graphics, Font font, int x, int y, int mouseX, int mouseY) {
        for (ImageDisplay display : imageDisplays) {
            graphics.blit(RenderType::guiTextured, display.location(), x + display.x(), y + display.y(), 0, 0, display.width(), display.height(), display.width(), display.height());
        }
        for (EntityDisplay display : entityDisplays) {
            if (!entities.containsKey(display) && Minecraft.getInstance().level != null) {
                LivingEntity livingEntity = display.type().create(Minecraft.getInstance().level, EntitySpawnReason.EVENT);
                if (livingEntity != null) {
                    livingEntity.yBodyRot = display.yRot();
                    livingEntity.setXRot(display.xRot());
                    livingEntity.setYRot(display.yRot());
                    livingEntity.yHeadRot = livingEntity.getYRot();
                    livingEntity.yHeadRotO = livingEntity.getYRot();
                    entities.put(display, livingEntity);
                }
            }
            if (entities.containsKey(display)) {
                InventoryScreen.renderEntityInInventory(graphics, x + display.x(), y + display.y(), display.scale(), new Vector3f(), display.rotation, null, entities.get(display));
            }
        }
        for (ItemDisplay display : itemDisplays) {
            graphics.renderItem(display.stack(), x + display.x(), y + display.y());
        }
        for (ItemTagDisplay display : itemTagDisplays) {
            List<Item> items = StreamSupport.stream(BuiltInRegistries.ITEM.getTagOrEmpty(display.tag()).spliterator(), false).map(Holder::value).toList();
            if (!items.isEmpty()) {
                graphics.renderItem(items.get((tickCount / 20) % items.size()).getDefaultInstance(), x + display.x(), y + display.y());
            }
        }
        for (TextDisplay display : textDisplays) {
            graphics.pose().pushPose();
            graphics.pose().translate(x + display.x(), y + display.y(), 0);
            graphics.pose().scale(display.scale(), display.scale(), display.scale());
            graphics.drawString(font, display.text(), -font.width(display.text()) / 2, -font.lineHeight, -16777216, false);
            graphics.pose().popPose();
        }
    }

    @Override
    public void tick(BookAccess access, Font font, int x, int y, int mouseX, int mouseY) {
        tickCount++;
    }


    @Override
    public void onClick(BookAccess access, Font font, int x, int y, int mouseX, int mouseY) {

    }

    public DisplayBookComponent textDisplay(Component text, int x, int y, float scale) {
        textDisplays.add(new TextDisplay(text, x, y, scale));
        return this;
    }

    public DisplayBookComponent entityDisplay(EntityType<? extends LivingEntity> type, int x, int y, float xRot, float yRot, float scale, Quaternionf rotation) {
        entityDisplays.add(new EntityDisplay(type, x, y, xRot, yRot, scale, rotation));
        return this;
    }

    public DisplayBookComponent itemDisplay(ItemStack stack, int x, int y) {
        itemDisplays.add(new ItemDisplay(stack, x, y));
        return this;
    }

    public DisplayBookComponent itemTagDisplay(TagKey<Item> tag, int x, int y) {
        itemTagDisplays.add(new ItemTagDisplay(tag, x, y));
        return this;
    }

    public DisplayBookComponent imageDisplay(ResourceLocation location, int x, int y, int width, int height) {
        imageDisplays.add(new ImageDisplay(location, x, y, width, height));
        return this;
    }

    private record TextDisplay(Component text, int x, int y, float scale) {

    }

    private record EntityDisplay(EntityType<? extends LivingEntity> type, int x, int y, float xRot, float yRot,
                                 float scale, Quaternionf rotation) {

    }

    private record ItemDisplay(ItemStack stack, int x, int y) {

    }

    private record ItemTagDisplay(TagKey<Item> tag, int x, int y) {

    }

    private record ImageDisplay(ResourceLocation location, int x, int y, int width, int height) {

    }
}