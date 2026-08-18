package baguchi.bagus_lib.client.render.book.component;

import baguchi.bagus_lib.client.render.book.BookAccess;
import it.unimi.dsi.fastutil.ints.*;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.FormattedCharSequence;

import java.util.ArrayList;
import java.util.List;

public class IndexBookComponent extends BookComponent {
    private final Component introText;
    private final List<IndexItem> indexItems = new ArrayList<>();
    private final Int2BooleanArrayMap leftHovers = new Int2BooleanArrayMap();
    private final Int2BooleanArrayMap rightHovers = new Int2BooleanArrayMap();
    private final Int2IntArrayMap oldHoverTicks = new Int2IntArrayMap();
    private final Int2IntArrayMap hoverTicks = new Int2IntArrayMap();
    private final List<FormattedCharSequence> cachedComponents = new ArrayList<>();
    private final List<FormattedCharSequence> hoveredIndexItemComponents = new ArrayList<>();
    private final IntList linesPerIndexItem = new IntArrayList();
    private int indexStartLine;

    public IndexBookComponent(Component introText, List<IndexItem> indexItems, int width, int height) {
        super(width, height);
        this.introText = introText;
        for (IndexItem indexItem : indexItems) {
            if (indexItem.enabled()) {
                this.indexItems.add(indexItem);
            }
        }
    }

    @Override
    public int getPageCount(int pagesBefore, Font font) {
        if (cachedComponents.isEmpty()) {
            cachedComponents.addAll(font.split(introText, width));
            indexStartLine = cachedComponents.size();
            for (IndexItem indexItem : indexItems) {
                cachedComponents.addAll(font.split(indexItem.text().copy().withColor(0x473d4b), width));
                List<FormattedCharSequence> list = font.split(indexItem.text().copy().withColor(0x261c09).withStyle(ChatFormatting.ITALIC).withStyle(ChatFormatting.BOLD), width);
                hoveredIndexItemComponents.addAll(list);
                linesPerIndexItem.add(list.size());
            }
        }
        int linesPerPage = height / font.lineHeight;
        return cachedComponents.size() % linesPerPage == 0 ? cachedComponents.size() / linesPerPage : cachedComponents.size() / linesPerPage + 1;
    }

    private int getIndexItemFromRelativeLine(int line) {
        int lines = 0;
        for (int i = 0; i < indexItems.size(); i++) {
            lines += linesPerIndexItem.getInt(i);
            if (lines > line) {
                return i;
            }
        }
        return -1;
    }

    private boolean isHovered(int index) {
        return leftHovers.getOrDefault(index, false) || rightHovers.getOrDefault(index, false);
    }

    @Override
    public void render(BookAccess access, GuiGraphicsExtractor graphics, Font font, int x, int y, int mouseX, int mouseY) {
        int linesPerPage = height / font.lineHeight;
        for (int i = access.getRelativePage() * linesPerPage; i < Math.min((access.getRelativePage() + 1) * linesPerPage, cachedComponents.size()); i++) {
            int textY = (i - access.getRelativePage() * linesPerPage) * font.lineHeight;
            FormattedCharSequence toDraw = cachedComponents.get(i);
            if (i >= indexStartLine) {
                if (isHovered(getIndexItemFromRelativeLine(i - indexStartLine))) {
                    toDraw = hoveredIndexItemComponents.get(i - indexStartLine);
                }
            }
            graphics.text(font, toDraw, x, y + textY, -13408581, false);
        }
    }

    @Override
    public void tick(BookAccess access, Font font, int x, int y, int mouseX, int mouseY) {
        int linesPerPage = height / font.lineHeight;
        int relativeY = mouseY - y;
        int line = access.getRelativePage() * linesPerPage + Math.min(relativeY / font.lineHeight, linesPerPage - 1);
        int hovered = getIndexItemFromRelativeLine(line - indexStartLine);
        Int2BooleanArrayMap hovers = access.isLeftPage() ? leftHovers : rightHovers;
        for (Int2BooleanMap.Entry entry : hovers.int2BooleanEntrySet()) {
            hovers.put(entry.getIntKey(), false);
        }
        if (line >= indexStartLine && hovered != -1 && mouseX > x && mouseX < x + width && mouseY > y && mouseY < y + height) {
            hovers.put(hovered, true);
        }
    }

    @Override
    public void singleTick(BookAccess access, Font font, int x, int y, int mouseX, int mouseY) {
        oldHoverTicks.putAll(hoverTicks);
        for (int i = 0; i < indexItems.size(); i++) {
            int value = hoverTicks.getOrDefault(i, 0);
            if (!isHovered(i)) {
                hoverTicks.put(i, Math.max(value - 1, 0));
            } else if (value < 20) {
                hoverTicks.put(i, value + 1);
            }
        }
    }

    @Override
    public void onClick(BookAccess access, Font font, int x, int y, int mouseX, int mouseY) {
        int linesPerPage = height / font.lineHeight;
        int relativeY = mouseY - y;
        int line = access.getRelativePage() * linesPerPage + Math.min(relativeY / font.lineHeight, linesPerPage - 1);
        int selected = getIndexItemFromRelativeLine(line - indexStartLine);
        if (line >= indexStartLine && selected != -1 && mouseX > x && mouseX < x + width && mouseY > y && mouseY < y + height) {
            Identifier jumpTo = indexItems.get(selected).jumpTo();
            for (int i = 0; i < access.getComponents().size(); i++) {
                BookComponentDefinition definition = access.getComponents().get(i);
                if (definition.id().equals(jumpTo)) {
                    int pages = 0;
                    for (int j = 0; j < i; j++) {
                        pages += access.getComponents().get(j).component().getPageCount(pages, font);
                    }
                    access.setPage(pages);
                    break;
                }
            }
        }
    }

    public List<IndexItem> getIndexItems() {
        return indexItems;
    }

    public record IndexItem(Component text, Component originalText, Identifier jumpTo, boolean enabled) {
        public IndexItem(Component text, Identifier jumpTo, boolean enabled) {
            this(Component.literal("•").append(text), text, jumpTo, enabled);
        }
    }
}