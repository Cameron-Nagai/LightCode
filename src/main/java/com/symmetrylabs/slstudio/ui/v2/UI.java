package com.symmetrylabs.slstudio.ui.v2;

import java.nio.ByteBuffer;


public class UI {
    public static int TREE_FLAG_LEAF;
    public static int TREE_FLAG_DEFAULT_OPEN;
    public static int TREE_FLAG_SELECTED;

    public static int WINDOW_HORIZ_SCROLL;
    public static int WINDOW_NO_RESIZE;
    public static int WINDOW_NO_MOVE;
    public static int WINDOW_NO_TITLE_BAR;
    public static int WINDOW_NO_DOCKING;
    public static int WINDOW_NO_BACKGROUND;
    public static int WINDOW_ALWAYS_AUTO_RESIZE;
    public static int WINDOW_NO_DECORATION;
    public static int WINDOW_NO_SCROLL_WITH_MOUSE;
    public static int WINDOW_FORCE_HORIZ_SCROLL;
    public static int WINDOW_NO_SCROLLBAR;

    public static int DEFAULT_WIDTH = 250;

    public static int COLOR_WIDGET;
    public static int COLOR_WIDGET_HOVERED;
    public static int COLOR_HEADER;
    public static int COLOR_HEADER_ACTIVE;
    public static int COLOR_HEADER_HOVERED;
    public static int COLOR_BUTTON;
    public static int COLOR_BUTTON_ACTIVE;
    public static int COLOR_BUTTON_HOVERED;
    public static int COLOR_WINDOW_BORDER;

    public static int COND_ALWAYS;
    public static int COND_ONCE;
    public static int COND_FIRST_USE_EVER;
    public static int COND_APPEARING;

    public static final int BLUE = 0xFF0096C8;
    public static final int BLUE_HOVER = 0xFF2EB5E1;
    public static final int RED = 0xFFF95951;
    public static final int RED_HOVER = 0xFFFA7770;
    public static final int PURPLE = 0xFF930FA5;
    public static final int PURPLE_HOVER = 0xFFA63AB5;

    public static class CollapseResult {
        /** true if the collapsable section should be open (i.e., client should draw the contents of the section) */
        public boolean isOpen;
        /** true if the collapsable section should be removed (the user pressed the close button) */
        public boolean shouldRemove;
    }

    /**
     * The width, in pixels, of the current SLStudio window
     *
     * Set on every frame by SLStudioGDX for reading by UI clients that want to anchor themselves somewhere.
     */
    public static float width;
    /**
     * The height, in pixels, of the current SLStudio window
     *
     * Set on every frame by SLStudioGDX for reading by UI clients that want to anchor themselves somewhere.
     */
    public static float height;
    /**
     * The density scaling of the display; lower numbers mean higher density
     */
    public static float density = 1; // this one is also used from native code in init()

    static void setDensity(float d) {
        /* we would really prefer to not scale, so if we're close to 1 we don't scale at all */
        density = Math.abs(d - 1.f) > 0.1 ? d : 1.f;
    }

    /**
     * Initialize ImGui
     * @param windowPointer the native pointer to our GLFW window encoded as a long
     * @param useMacBehaviors when true, we use Mac style input behaviors (ctrl-click right clicks, shortcuts use Cmd instead of Ctrl, etc)
     */
    public static native boolean init(long windowPointer, boolean useMacBehaviors);

    /**
     * Begin processing the UI for a new frame.
     *
     * This absorbs mouse and keyboard input data, synthesizes it, and prepares buffers for drawing.
     */
    public static native void newFrame();

    /**
     * Draw buffered draw data to the screen.
     */
    public static native void render();

    /**
     * Deallocate all resources and drop all UI state.
     *
     * After a call to shutdown(), UI should be considered unusable.
     */
    public static native boolean shutdown();

    /**
     * Add a font using a byte buffer of TTF or OTF data.
     *
     * This compiles the font into a font texture and prepares it for display
     * through ImGui. Fonts can be viewed and changed in the style editor. If
     * this function is never called, the ImGui default font is used. If addFont
     * is called, the default UI font is the font given in the first call to
     * addFont.
     *
     * @param name the font name
     * @param ttfData the font data in TTF or OTF format
     * @param fontSize the font size, in pixels, that we want to compile the font at
     * @returns an opaque handle that can be used to refer to the font
     */
    public static native long addFont(String name, ByteBuffer ttfData, float fontSize);

    public static native void pushFont(long fontHandle);
    public static native void popFont();

    /**
     * Temporarily change a color in the UI style.
     *
     * This changes the UI style until a corresponding call to {@link popColor()}
     *
     * @param key the style color to change; use one of the COLOR_ constants on the UI class
     * @param color the color in LX format (0xAARRGGBB). If no alpha is given the color is assumed to be full-opaque.
     */
    public static native void pushColor(int key, int color);

    /**
     * Undo one call to {@link pushColor(int, int)}.
     *
     * This restores the colors in the UI style to whatever they before the last pushColor call.
     */
    public static void popColor() {
        popColor(1);
    }

    /**
     * Undo multiple calls to {@link pushColor(int, int)} at once.
     *
     * This restores the colors in the UI style to whatever they before the last {@code count} pushColor calls.
     *
     * @param count the number of pushColor calls to undo
     */
    public static native void popColor(int count);

    public static native void pushWidth(float width);
    public static native void popWidth();
    public static native float calcWidth();

    /* Layout */
    public static native void setNextWindowPosition(float x, float y, float pivotX, float pivotY);
    public static native void setNextWindowDefaults(float x, float y, float w, float h);
    public static native void setNextWindowDefaultToCursor(float w, float h);
    public static native void setNextWindowContentSize(float w, float h);
    public static native void setNextWindowSize(float w, float h);
    public static void begin(String label) {
        begin(label, 0);
    }
    public static native void begin(String label, int flags);
    public static boolean beginClosable(String label) {
        return beginClosable(label, 0);
    }
    public static native boolean beginClosable(String label, int flags);
    public static native void end();
    public static native void sameLine();

    public static native void beginColumns(int num, String id);
    public static native void nextColumn();
    public static native void endColumns();

    public static native void separator();
    public static native void spacing();

    public static boolean beginChild(String id, boolean border, int flags) {
        return beginChild(id, border, flags, 0, 0);
    }
    public static native boolean beginChild(String id, boolean border, int flags, int w, int h);
    public static native void endChild();
    public static native void beginGroup();
    public static native void endGroup();

    /* Popup model windows */
    public static native void openPopup(String name);
    public static native void closePopup();
    public static boolean beginPopup(String name, boolean modal) {
        return beginPopup(name, modal, 0);
    }
    public static native boolean beginPopup(String name, boolean modal, int flags);
    public static native void endPopup();

    /* Widgets */
    public static native void text(String t);
    public static void text(String t, Object... objs) {
        text(String.format(t, objs));
    }
    public static native void labelText(String label, String value);
    public static boolean button(String t) {
        return button(t, 0, 0);
    }
    public static boolean button(String t, float w) {
        return button(t, w, 0);
    }
    public static native boolean button(String t, float w, float h);
    public static native boolean checkbox(String label, boolean v);
    public static boolean selectable(String label, boolean v) {
        return selectable(label, v, 0);
    }
    public static native boolean selectable(String label, boolean v, float height);
    public static native String inputText(String label, String text);
    public static native int colorPicker(String label, int rgb);
    public static native float[] colorPickerHSV(String label, float h, float s, float v);
    public static native float sliderFloat(String label, float v, float v0, float v1);
    public static native float vertSliderFloat(String label, float v, float v0, float v1, String valFmt, float width, float height);
    public static native int sliderInt(String label, int v, int v0, int v1);
    public static native int combo(String label, int selected, String[] options);
    public static float floatBox(String label, float v) {
        return floatBox(label, v, 1, 0, 0, null);
    }
    public static native float floatBox(String label, float v, float speed, float min, float max, String valFmt);

    /* knob widgets are always over [0, 1] */
    public static native float knobFloat(String label, float value, float normalized);
    public static native float knobModulatedFloat(
        String label, float value, float normalizedBase, float normalizedValue,
        int modulatorCount, float[] modulatorMins, float[] modulatorMaxs, int[] modulatorColors);

    public static boolean collapsibleSection(String label) {
        return collapsibleSection(label, false, 0).isOpen;
    }
    public static CollapseResult collapsibleSection(String label, boolean allowClose) {
        return collapsibleSection(label, allowClose, 0);
    }
    public static native CollapseResult collapsibleSection(String label, boolean allowClose, int flags);

    public static native void histogram(String label, float[] values, float min, float max, int size);
    public static native void plot(String label, float[] values, float min, float max, int size);
    public static native boolean colorButton(String id, float h, float s, float b);

    public static native void envelopeEditor(String label, double[] basis, double[] value, double[] shape);

    /* Images. These are package-private; code should interact with TextureManager, which
       actually handles the complexity of texture loading/unloading for you. */
    static native void image(int texId, float w, float h, float u0, float v0, float u1, float v1);
    static native boolean imageButton(int texId, float w, float h, float u0, float v0, float u1, float v1);

    /* Menus */
    public static native boolean beginMainMenuBar();
    public static native void endMainMenuBar();
    public static native boolean beginMenu(String label);
    public static native void endMenu();
    public static native boolean menuItem(String label, String shortcut, boolean selected, boolean enabled);
    public static boolean menuItem(String label) {
        return menuItem(label, null, false, true);
    }
    public static boolean menuText(String label) {
        return menuItem(label, null, false, false);
    }
    public static native boolean menuItemToggle(String label, String shortcut, boolean selected, boolean enabled);

    /* Context menus */
    public static native boolean beginContextMenu(String id);
    public static native boolean contextMenuItem(String label);
    public static native void endContextMenu();

    /* Trees */
    public static boolean treeNode(String label) {
        return treeNode(label, 0, label);
    }
    public static boolean treeNode(String label, int flags) {
        return treeNode(label, flags, label);
    }
    public static native boolean treeNode(String id, int flags, String label);
    public static native void treePop();

    public static void setNextTreeNodeOpen(boolean isOpen) {
        setNextTreeNodeOpen(isOpen, COND_ALWAYS);
    }
    public static native void setNextTreeNodeOpen(boolean isOpen, int when);

    /* Drag and drop. Package-private; use DragDrop class for memory-safe versions of these functions */
    static boolean beginDragDropSource() {
        return beginDragDropSource(0);
    }
    static native boolean beginDragDropSource(int flags);
    static native void endDragDropSource();
    static native boolean setDragDropPayload(String type, Object data);
    static native boolean beginDragDropTarget();
    static native void endDragDropTarget();
    static native Object acceptDragDropPayload(String type, int flags);
    public static <T> T acceptDragDropPayload(String type, Class<T> cls) {
        Object res = acceptDragDropPayload(type, 0);
        if (res == null || !cls.isAssignableFrom(res.getClass())) {
            return null;
        }
        return (T) res;
    }

    /* Interaction */
    public static boolean isItemClicked() {
        return isItemClicked(0, false);
    }
    public static boolean isItemClicked(int mouseButton) {
        return isItemClicked(mouseButton, false);
    }
    public static boolean isItemClicked(boolean allowMouseHold) {
        return isItemClicked(0, allowMouseHold);
    }
    public static native boolean isItemClicked(int mouseButton, boolean allowMouseHold);

    public static boolean isItemDoubleClicked() {
        return isItemDoubleClicked(0);
    }
    public static native boolean isItemDoubleClicked(int mouseButton);
    public static native boolean isItemActive();
    public static native boolean isAltDown();
    public static native boolean isCtrlDown();
    public static native boolean isShiftDown();

    /* IO */
    static native float getFrameRate();
    static native boolean wantCaptureKeyboard();
    static native boolean wantCaptureMouse();
    static native void keyDown(int keycode);
    static native void keyUp(int keycode);
    static native void addInputCharacter(char c);
    static native void scrolled(float amount);

    /* Testing */
    static native boolean showDemoWindow();
    static native boolean showMetricsWindow();
    static native boolean showStyleEditor();
    static native boolean showAboutWindow();

    static {
        System.loadLibrary("slimgui");
    }
}