package ru.vizzi.Utils.gui;

import java.awt.Color;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import com.google.common.collect.Lists;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;

@SideOnly(Side.CLIENT)
public class GuiScrollingList<T> extends Gui {

    public static final float DEFAULT_SCROLL_SPEED = 10.0F;

    protected final GuiScreen parent;
    protected final Minecraft mc;

    protected int x;
    protected int y;
    protected int width;
    protected int height;
    protected int entryHeight;

    protected int scrollOffset;
    protected float scrollSpeed;

    protected int hoverIndex;
    protected int selected;

    protected int mouseX, mouseY;

    protected int hoverColor, selectedColor, textColor, selectedTextColor, hoverTextColor, sliderColor, sliderBackgroundColor;
    protected int sliderOffset, sliderWidth;
    protected boolean drawHoverColor, drawSelectedColor, drawSliderBackground, canLoseFocus, canSelect;

    private boolean dragging;
    private int dragged, mouseYOffset;
  
    protected List<T> elementData;

    public GuiScrollingList(GuiScreen screen, int listEntryHeight){
        this(screen, 0, 0, 100, 50, listEntryHeight);
    }

    public GuiScrollingList(GuiScreen screen, int posX, int posY, int listWidth, int listHeight, int listEntryHeight){
        parent = screen;
        mc = parent.mc;
        width = listWidth;
        height = listHeight;
        x = posX;
        y = posY;
        entryHeight = listEntryHeight;
        scrollSpeed = DEFAULT_SCROLL_SPEED;
        hoverIndex = -1;
        selected = -1;
        hoverColor = 839518730;
        selectedColor = -1778384896;
        sliderColor = new Color(255, 0, 0, 125).getRGB();
        sliderBackgroundColor = new Color(14, 18, 30).getRGB();
        textColor = selectedTextColor = hoverTextColor = -1;
        sliderOffset = 1;
        sliderWidth = 3;
        drawHoverColor = true;
        drawSelectedColor = true;
        canLoseFocus = true;
        canSelect = true;
        drawSliderBackground = true;
        elementData = Lists.newArrayList();
    }

    public void onEntryClicked(T entry, int index, int mouseX, int mouseY, int button){}

    public void drawEntry(T entry, int index, int x, int y, boolean hovered){
        drawCenteredString(mc.fontRenderer, entry.toString(), x + width / 2, y + entryHeight / 2 - mc.fontRenderer.FONT_HEIGHT / 2, isSelected(index) ? selectedTextColor : hovered ? hoverTextColor : textColor);
    }

    public void drawEntryForeground(T entry, int index, int x, int y, boolean hovered){}

    public int getSize(){
        return elementData.size();
    }

    public int getContentSize(){
        return getSize() * entryHeight;
    }

    public boolean isSelected(int index){
        return index == selected;
    }

    public boolean isSelected(){
        return selected >= 0;
    }

    public void mouseClicked(int mouseX, int mouseY, int button){
        if(hoverIndex != -1){
            if(canSelect){
                selected = hoverIndex;
            }
            T entry = getElement(hoverIndex);
            if(entry != null){
                onEntryClicked(entry, hoverIndex, mouseX, mouseY, button); // TODO:
            }
        }else{
            if(canLoseFocus){
                selected = -1;
            }
        }
      
        int start = getContentSize() - height;
        if(start > 0){
            int scrollBarXStart = x + width + sliderOffset;
            int scrollBarXEnd = scrollBarXStart + sliderWidth;
            int length = height * height / getContentSize(); // height * height / (getSize() * entryHeight);
  
            if(length < 8){
                length = 8;
            }
  
            if(length > height - 8){
                length = height - 8;
            }
  
            int end = scrollOffset * (height - length) / start + y;
  
            if(end < y){
                end = y;
            }
          
            if(mouseX > scrollBarXStart && mouseY >= end && mouseX < scrollBarXEnd && mouseY < end + length){
                dragging = true;
                mouseYOffset = mouseY;
            }
        }
    }
  
    public void mouseClickMove(int mouseX, int mouseY, int button){
        if(dragging){
            scrollOffset += (mouseY - mouseYOffset) * (getContentSize() / height);
            if(scrollOffset > getContentSize() - height){
                scrollOffset = getContentSize() - height;
            }
            if(scrollOffset < 0){
                scrollOffset = 0;
            }
            mouseYOffset = mouseY;
        }
    }
  
    public void mouseReleased(int mouseX, int mouseY, int button){
        dragging = false;
    }

    public void handleMouseInput(){
        if(isMouseOver()){
            int delta = Mouse.getDWheel();
            if(delta != 0){
                if(delta > 0){
                    delta = -1;
                }else if(delta < 0){
                    delta = 1;
                }
                int maxScrollOffset = Math.max(0, getSize() * entryHeight - height);
                scrollOffset = (int)Math.max(Math.min(scrollOffset + (delta * scrollSpeed), maxScrollOffset), 0);
            }
        }
    }

    public void updateScreen(){
        if(isMouseOver()){
            hoverIndex = (mouseY - y + scrollOffset) / entryHeight;
            if(hoverIndex >= getSize() || hoverIndex < 0){
                hoverIndex = -1;
            }
        }else{
            hoverIndex = -1;
        }
    }

    public void drawScreen(int mX, int mY, float ticks){
        mouseX = mX;
        mouseY = mY;

        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        glScissor(x, y, width, height);

        int currentY = y - scrollOffset;
        for(int l = 0; l < getSize(); l++){
            if(currentY >= y - entryHeight && currentY <= y + height){
                boolean isHover = hoverIndex == l;
                T entry = getElement(l);
                if(entry != null){
                    if(isSelected(l) && drawSelectedColor){
                        drawRect(x, currentY, x + width, currentY + entryHeight, selectedColor);
                    }
                    if(isHover && drawHoverColor){
                        drawRect(x, currentY, x + width, currentY + entryHeight, hoverColor);
                    }
                    drawEntry(entry, l, x, currentY, isHover);
                }
            }
            currentY += entryHeight;
        }

        GL11.glDisable(GL11.GL_SCISSOR_TEST);

        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glDisable(GL11.GL_ALPHA_TEST);

        int start = getContentSize() - height;

        if(start > 0) {
            int length = height * height / getContentSize(); // height * height / (getSize() * entryHeight);

            if(length < 8) {
                length = 8;
            }

            if(length > height - 8) {
                length = height - 8;
            }

            int end = scrollOffset * (height - length) / start + y;

            if(end < y) {
                end = y;
            }

            int scrollBarXStart = x + width + sliderOffset;
            int scrollBarXEnd = scrollBarXStart + sliderWidth;

            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            if(drawSliderBackground) {
                drawRect(scrollBarXStart, y, scrollBarXEnd, y + height, sliderBackgroundColor); // Background
            }
            drawRect(scrollBarXStart, end + length, scrollBarXEnd, end, sliderColor);
          } else {
        	  
        	  int length = height * height / 1;
        	  
              if(length < 8) {
                  length = 8;
              }

              if(length > height - 8) {
                  length = height - 8;
              }

              int end = scrollOffset * (height - length) / start + y;

              if(end < y) {
                  end = y;
              }
              int scrollBarXStart = x + width + sliderOffset;
              int scrollBarXEnd = scrollBarXStart + sliderWidth;
        	  drawRect(scrollBarXStart, end + length + 8, scrollBarXEnd, end, sliderColor);
          }


        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glDisable(GL11.GL_BLEND);

        // Foreground
        currentY = y - scrollOffset;
        for(int l = 0; l < getSize(); l++){
            if(currentY >= y - entryHeight && currentY <= y + height){
                boolean isHover = hoverIndex == l;
                T entry = getElement(l);
                if(entry != null){
                    drawEntryForeground(entry, l, x, currentY, isHover);
                }
            }
            currentY += entryHeight;
        }
    }

    public void cleanUp(){
        hoverIndex = -1;
        selected = -1;
        scrollOffset = 0;
        elementData.clear();
    }

    public boolean isMouseOver(){
        return mouseX >= x && mouseX < x + width && mouseY >= y && mouseY < y + height;
    }

    public List<T> getElements(){
        return elementData;
    }

    public void clear(){
        elementData.clear();
    }

    public T removeFirstElement(){
        return elementData.size() > 0 ? elementData.remove(0) : null;
    }

    public T removeLastElement(){
        return elementData.size() > 0 ? elementData.remove(elementData.size() - 1) : null;
    }

    public T removeSelectedElement(){
        return isSelected() ? elementData.remove(getSelectedIndex()) : null;
    }

    public boolean removeElement(T element){
        return elementData.remove(element);
    }

    public T removeElement(int index){
        return elementData.remove(index);
    }

    public void setElements(List<T> elements){
        elementData = elements;
    }

    public boolean addElement(T element){
        return elementData.add(element);
    }

    public boolean addElements(T... elements){
        return elementData.addAll(Arrays.asList(elements));
    }

    public boolean addElements(Collection<? extends T> elements){
        return elementData.addAll(elements);
    }

    public T getElement(int index){
        return index >= 0 && index < elementData.size() ? elementData.get(index) : null;
    }

    public T getSelectedElement(){
        return getElement(selected);
    }

    public float getScrollSpeed(){
        return scrollSpeed;
    }

    public GuiScrollingList setScrollSpeed(float speed){
        scrollSpeed = speed;
        return this;
    }

    public int getHoverColor(){
        return hoverColor;
    }

    public GuiScrollingList setHoverColor(int color){
        hoverColor = color;
        return this;
    }

    public int getSelectedColor(){
        return selectedColor;
    }

    public GuiScrollingList setSelectedColor(int color){
        selectedColor = color;
        return this;
    }

    public int getSliderColor(){
        return sliderColor;
    }

    public GuiScrollingList setSliderColor(int color){
        sliderColor = color;
        return this;
    }

    public boolean isDrawHoverColor(){
        return drawHoverColor;
    }

    public GuiScrollingList setDrawHoverColor(boolean draw){
        drawHoverColor = draw;
        return this;
    }

    public boolean isDrawSelectedColor(){
        return drawSelectedColor;
    }

    public GuiScrollingList setDrawSelectedColor(boolean draw){
        drawSelectedColor = draw;
        return this;
    }

    public boolean isDrawSliderBackground(){
        return drawSliderBackground;
    }

    public GuiScrollingList setDrawSliderBackground(boolean draw){
        drawSliderBackground = draw;
        return this;
    }

    public int getTextColor(){
        return textColor;
    }

    public GuiScrollingList setTextColor(int color){
        textColor = color;
        return this;
    }

    public int getHoverTextColor(){
        return hoverTextColor;
    }

    public GuiScrollingList setHoverTextColor(int color){
        hoverTextColor = color;
        return this;
    }

    public int getSelectedTextColor(){
        return selectedTextColor;
    }

    public GuiScrollingList setSelectedTextColor(int color){
        selectedTextColor = color;
        return this;
    }

    public int getSliderBackgroundColor(){
        return sliderBackgroundColor;
    }

    public GuiScrollingList setSliderBackgroundColor(int color){
        sliderBackgroundColor = color;
        return this;
    }

    public int getSliderOffset(){
        return sliderOffset;
    }

    public GuiScrollingList setSliderOffset(int offset){
        sliderOffset = offset;
        return this;
    }

    public int getSliderWidth(){
        return sliderWidth;
    }

    public GuiScrollingList setSliderWidth(int width){
        sliderWidth = width;
        return this;
    }

    public int getScrollOffset(){
        return scrollOffset;
    }

    public void setScrollOffset(int offset){
        scrollOffset = offset;
    }

    public int getSelectedIndex(){
        return selected;
    }

    public void setSelectedIndex(int index){
        selected = index;
    }

    public boolean canLoseFocus(){
        return canLoseFocus;
    }

    public GuiScrollingList setCanLoseFocus(boolean loseFocus){
        canLoseFocus = loseFocus;
        return this;
    }

    public boolean canSelect(){
        return canSelect;
    }

    public GuiScrollingList setCanSelect(boolean select){
        canSelect = select;
        return this;
    }

    public GuiScreen getParent(){
        return parent;
    }

    public void setPosition(int xPos, int yPos){
        x = xPos;
        y = yPos;
    }

    public void setSize(int w, int h){
        width = w;
        height = h;
    }

    public int getX(){
        return x;
    }

    public int getY(){
        return y;
    }

    public int getWidth(){
        return width;
    }

    public int getHeight(){
        return height;
    }

    public int getEntryHeight(){
        return entryHeight;
    }

    public int getHoverIndex(){
        return hoverIndex;
    }

    public int getMouseX(){
        return mouseX;
    }

    public int getMouseY(){
        return mouseY;
    }

    private void glScissor(int x, int y, int width, int height){
        Minecraft mc = Minecraft.getMinecraft();
        ScaledResolution resolution = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
        int scale = resolution.getScaleFactor();

        int scissorWidth = width * scale;
        int scissorHeight = height * scale;
        int scissorX = x * scale;
        int scissorY = mc.displayHeight - scissorHeight - (y * scale);

        GL11.glScissor(scissorX, scissorY, scissorWidth, scissorHeight);
    }
}