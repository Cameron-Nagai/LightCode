package com.symmetrylabs.slstudio;

import java.awt.*;
import java.io.File;

import com.google.gson.JsonObject;

import com.symmetrylabs.layouts.LayoutRegistry;
import com.symmetrylabs.slstudio.performance.PerformanceManager;
import com.symmetrylabs.slstudio.ui.*;
import heronarts.lx.*;
import heronarts.lx.warp.LXWarp;
import processing.core.PApplet;
import processing.event.MouseEvent;
import processing.event.KeyEvent;

import heronarts.lx.effect.BlurEffect;
import heronarts.lx.effect.DesaturationEffect;
import heronarts.lx.effect.FlashEffect;
import heronarts.lx.model.LXModel;
import heronarts.lx.pattern.IteratorTestPattern;
import heronarts.p3lx.P3LX;
import heronarts.p3lx.ui.UIEventHandler;
import heronarts.p3lx.ui.UIObject;
import heronarts.p3lx.ui.UIWindow;
import heronarts.p3lx.ui.UI2dComponent;
import heronarts.p3lx.ui.component.UIImage;
import heronarts.p3lx.ui.studio.UIBottomTray;
import heronarts.p3lx.ui.studio.UIContextualHelpBar;
import heronarts.p3lx.ui.studio.UILeftPane;
import heronarts.p3lx.ui.studio.clip.UIClipLauncher;
import heronarts.p3lx.ui.studio.clip.UIClipView;
import heronarts.p3lx.ui.studio.device.UIDeviceBin;
import heronarts.p3lx.ui.studio.mixer.UIMixer;
import heronarts.p3lx.ui.studio.mixer.UIMixerStrip;
import heronarts.p3lx.ui.studio.mixer.UIMixerStripControls;
import heronarts.p3lx.ui.studio.modulation.UIModulator;

import com.symmetrylabs.LXClassLoader;
import com.symmetrylabs.slstudio.pattern.base.SLPattern;
import com.symmetrylabs.util.MarkerSource;

import javax.swing.*;

import static java.awt.event.KeyEvent.*;
import static heronarts.lx.PolyBuffer.Space.RGB16;
import static heronarts.lx.PolyBuffer.Space.RGB8;

public class SLStudioLX extends P3LX {
    public static final String COPYRIGHT = "Symmetry Labs";

    private static final String DEFAULT_PROJECT_FILE = "default.lxp";
    private static final String PROJECT_FILE_NAME = ".lxproject";
    private static final String KEY_UI = "ui";
    private static final int RESTART_EXIT_CODE = 999;

    public class UI extends heronarts.p3lx.ui.UI implements LXSerializable {
        public final UIPreviewWindow preview;
        public final UILeftPane leftPane;
        public final UIOverriddenRightPane rightPane;
        public final UIBottomTray bottomTray;
        public final UIContextualHelpBar helpBar;
        public final UIFramerate framerate;
        public final UIHelpText helpText;
        public final UIAxes axes;
        public final UIMarkerPainter markerPainter;
        public final UICubeMapDebug cubeMapDebug;
        public PerformanceManager performanceManager = null;

        private boolean toggleHelpBar = false;
        private boolean toggleClipView = false;
        private boolean toggleSidebars = false;
        private boolean clipViewVisible = true;
        private boolean sidebarsVisible = true;


        private final LX lx;

        /**
         * Help text to display when "?" is pressed.  "@" will be replaced with
         * "Cmd" or "Ctrl", as appropriate for the operating system.
         */
        private static final String HELP_TEXT =
              "@-C           Toggle P3CubeMap debugging\n" +
                "@-D           Delete selected channel, warp, effect, or pattern\n" +
                "@-F           Toggle frame rate status line\n" +
                "@-G           Toggle UI geometry\n" +
                "@-L           Layout selection\n" +
                "@-M           Modulation source\n" +
                "@-N           New channel\n" +
                "@-R           Rename channel or pattern\n" +
                "@-S           Save current project\n" +
                "@-V           Toggle preview display\n" +
                "@-X           Toggle axis display\n" +
                "@-/           Toggle help caption line\n" +
                "@-\\           Toggle 16-bit color (all)\n" +
                "@-|           Toggle 16-bit color (selected channel)\n" +
                "@-Left/Right  Reorder selected channel, warp, or effect\n" +
                "@-Up/Down     Reorder selected pattern"
            ;

        UI(final SLStudioLX lx) {
            super(lx);

            this.lx = lx;

            initialize(lx, this);

            setBackgroundColor(this.theme.getDarkBackgroundColor());

            this.preview = new UIPreviewWindow(this, lx, UILeftPane.WIDTH, 0,
            this.applet.width - UILeftPane.WIDTH - UIOverriddenRightPane.WIDTH,
            this.applet.height - UIBottomTray.HEIGHT - UIContextualHelpBar.VISIBLE_HEIGHT);
            this.leftPane = new UILeftPane(this, lx);
            this.rightPane = new UIOverriddenRightPane(this, lx);
            this.bottomTray = new UIBottomTray(this, lx);
            this.helpBar = new UIContextualHelpBar(this);
            float previewLeft = leftPane.getX() + leftPane.getWidth();
            this.framerate = new UIFramerate(this, lx, previewLeft + 6, 6);
            int mask = Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();
            this.helpText = new UIHelpText(this, previewLeft + 6, 40,
                  HELP_TEXT.replaceAll("@", mask == KeyEvent.CTRL ? "Ctrl" : "Cmd"));
            this.axes = new UIAxes();
            this.markerPainter = new UIMarkerPainter();
            this.cubeMapDebug = new UICubeMapDebug(lx);
            this.preview.addComponent(this.cubeMapDebug);
            this.preview.addComponent(axes);
            this.preview.addComponent(markerPainter);

            new UI2dComponent(0, 0, leftPane.getWidth(), 30) {}.setBackgroundColor(0).addToContainer(leftPane);

            new UIImage(applet.loadImage("symmetry-labs-logo.png"), 4, 4)
            .setDescription("Symmetry Labs")
            .addToContainer(leftPane);

            addLayer(this.preview);
            addLayer(this.leftPane);
            addLayer(this.rightPane);
            addLayer(this.bottomTray);
            addLayer(this.helpBar);
            addLayer(this.framerate);
            addLayer(this.helpText);

            _toggleClipView();

            setTopLevelKeyEventHandler(new UIEventHandler() {
                @Override
                protected void onKeyPressed(KeyEvent keyEvent, char keyChar, int keyCode) {
                    if (keyChar == '?') {
                        helpText.toggleVisible();
                    }
                    if (keyChar == 27) {
                        lx.engine.mapping.setMode(LXMappingEngine.Mode.OFF);
                    }

                    // Remember to update HELP_TEXT above when adding/changing any hotkeys!
                    if (keyEvent.isMetaDown() || keyEvent.isControlDown()) {
                        switch (keyCode) {
                            case VK_C:
                                cubeMapDebug.toggleVisible();
                                break;
                            case VK_F:
                                framerate.toggleVisible();
                                break;
                            case VK_G:
                                markerPainter.toggleVisible();
                                break;
                            case VK_L:
                                String layoutName = (String) JOptionPane.showInputDialog(
                                    null, "Select a layout and click OK to restart.", "Select layout",
                                    JOptionPane.QUESTION_MESSAGE, null, LayoutRegistry.getNames().toArray(), null);
                                if (layoutName != null) {
                                    applet.saveStrings(SLStudio.LAYOUT_FILE_NAME, new String[] {layoutName});
                                    applet.saveStrings(SLStudio.RESTART_FILE_NAME, new String[0]);
                                    applet.exit();
                                }
                                break;
                            case VK_M:
                                LXMappingEngine.Mode mode = keyEvent.isShiftDown() ?
                                        LXMappingEngine.Mode.MIDI : LXMappingEngine.Mode.MODULATION_SOURCE;
                                lx.engine.mapping.setMode(
                                        lx.engine.mapping.getMode() == mode ?
                                        LXMappingEngine.Mode.OFF : mode);
                                break;
                            case VK_V:
                                lx.ui.preview.toggleVisible();
                                break;
                            case VK_X:
                                axes.toggleVisible();
                                break;
                            case VK_I:
                                toggleSidebars();
                                performanceManager.gui.moveWindows(sidebarsVisible);
                                break;
                            case VK_SLASH:
                                toggleHelpBar = true;
                                break;

                            case VK_BACK_SLASH:
                                switch (keyChar) {
                                    case '\\':
                                        PolyBuffer.Space space = lx.engine.colorSpace.getEnum() == RGB16 ? RGB8 : RGB16;
                                        lx.engine.colorSpace.setValue(space);
                                        for (LXChannel channel : lx.engine.channels) {
                                            channel.colorSpace.setValue(space);
                                        }
                                        break;
                                    case '|':
                                        if (engine.getFocusedChannel() instanceof LXChannel) {
                                            LXChannel channel = (LXChannel) engine.getFocusedChannel();
                                            space = channel.colorSpace.getEnum() == RGB16 ? RGB8 : RGB16;
                                            channel.colorSpace.setValue(space);
                                        }
                                        break;
                                }
                                break;
                        }
                    }
                    if (engine.getFocusedChannel() instanceof LXChannel) {
                        LXChannel focusedChannel = (LXChannel) engine.getFocusedChannel();
                        LXPattern focusedPattern = focusedChannel.getFocusedPattern();
                        if (focusedPattern instanceof SLPattern) {
                            SLPattern pattern = (SLPattern) focusedPattern;
                            pattern.unconsumeKeyEvent();
                            pattern.onKeyPressed(keyEvent, keyChar, keyCode);
                            root.keyEventConsumed = pattern.keyEventConsumed();
                        }
                    }
                }

                @Override
                public void onMousePressed(MouseEvent mouseEvent, float mx, float my) {
                    if (engine.getFocusedChannel() instanceof LXChannel) {
                        LXChannel focusedChannel = (LXChannel) engine.getFocusedChannel();
                        LXPattern focusedPattern = focusedChannel.getFocusedPattern();
                        if (focusedPattern instanceof SLPattern) {
                            ((SLPattern) focusedPattern).onMousePressed(mouseEvent, mx, my);
                        }
                    }
                }

                @Override
                public void onMouseReleased(MouseEvent mouseEvent, float mx, float my) {
                    if (engine.getFocusedChannel() instanceof LXChannel) {
                        LXChannel focusedChannel = (LXChannel) engine.getFocusedChannel();
                        LXPattern focusedPattern = focusedChannel.getFocusedPattern();
                        if (focusedPattern instanceof SLPattern) {
                            ((SLPattern) focusedPattern).onMouseReleased(mouseEvent, mx, my);
                        }
                    }
                }

                @Override
                public void onMouseClicked(MouseEvent mouseEvent, float mx, float my) {
                    if (engine.getFocusedChannel() instanceof LXChannel) {
                        LXChannel focusedChannel = (LXChannel) engine.getFocusedChannel();
                        LXPattern focusedPattern = focusedChannel.getFocusedPattern();
                        if (focusedPattern instanceof SLPattern) {
                            ((SLPattern) focusedPattern).onMouseClicked(mouseEvent, mx, my);
                        }
                    }
                }

                @Override
                public void onMouseDragged(MouseEvent mouseEvent, float mx, float my, float dx, float dy) {
                    if (engine.getFocusedChannel() instanceof LXChannel) {
                        LXChannel focusedChannel = (LXChannel) engine.getFocusedChannel();
                        LXPattern focusedPattern = focusedChannel.getFocusedPattern();
                        if (focusedPattern instanceof SLPattern) {
                            ((SLPattern) focusedPattern).onMouseDragged(mouseEvent, mx, my, dx, dy);
                        }
                    }
                }

                @Override
                public void onMouseMoved(MouseEvent mouseEvent, float mx, float my) {
                    if (engine.getFocusedChannel() instanceof LXChannel) {
                        LXChannel focusedChannel = (LXChannel) engine.getFocusedChannel();
                        LXPattern focusedPattern = focusedChannel.getFocusedPattern();
                        if (focusedPattern instanceof SLPattern) {
                            ((SLPattern) focusedPattern).onMouseMoved(mouseEvent, mx, my);
                        }
                    }
                }

                @Override
                public void onMouseOver(MouseEvent mouseEvent) {
                    if (engine.getFocusedChannel() instanceof LXChannel) {
                        LXChannel focusedChannel = (LXChannel) engine.getFocusedChannel();
                        LXPattern focusedPattern = focusedChannel.getFocusedPattern();
                        if (focusedPattern instanceof SLPattern) {
                            ((SLPattern) focusedPattern).onMouseOver(mouseEvent);
                        }
                    }
                }

                @Override
                public void onMouseOut(MouseEvent mouseEvent) {
                    if (engine.getFocusedChannel() instanceof LXChannel) {
                        LXChannel focusedChannel = (LXChannel) engine.getFocusedChannel();
                        LXPattern focusedPattern = focusedChannel.getFocusedPattern();
                        if (focusedPattern instanceof SLPattern) {
                            ((SLPattern) focusedPattern).onMouseOut(mouseEvent);
                        }
                    }
                }

                @Override
                public void onMouseWheel(MouseEvent mouseEvent, float mx, float my, float delta) {
                    if (engine.getFocusedChannel() instanceof LXChannel) {
                        LXChannel focusedChannel = (LXChannel) engine.getFocusedChannel();
                        LXPattern focusedPattern = focusedChannel.getFocusedPattern();
                        if (focusedPattern instanceof SLPattern) {
                            ((SLPattern) focusedPattern).onMouseWheel(mouseEvent, mx, my, delta);
                        }
                    }
                }

                @Override
                public void onKeyReleased(KeyEvent keyEvent, char keyChar, int keyCode) {
                    if (engine.getFocusedChannel() instanceof LXChannel) {
                        LXChannel focusedChannel = (LXChannel) engine.getFocusedChannel();
                        LXPattern focusedPattern = focusedChannel.getFocusedPattern();
                        if (focusedPattern instanceof SLPattern) {
                            SLPattern pattern = (SLPattern) focusedPattern;
                            pattern.unconsumeKeyEvent();
                            pattern.onKeyReleased(keyEvent, keyChar, keyCode);
                            root.keyEventConsumed = pattern.keyEventConsumed();
                        }
                    }
                }

                @Override
                public void onKeyTyped(KeyEvent keyEvent, char keyChar, int keyCode) {
                    if (engine.getFocusedChannel() instanceof LXChannel) {
                        LXChannel focusedChannel = (LXChannel) engine.getFocusedChannel();
                        LXPattern focusedPattern = focusedChannel.getFocusedPattern();
                        if (focusedPattern instanceof SLPattern) {
                            SLPattern pattern = (SLPattern) focusedPattern;
                            pattern.unconsumeKeyEvent();
                            pattern.onKeyTyped(keyEvent, keyChar, keyCode);
                            root.keyEventConsumed = pattern.keyEventConsumed();
                        }
                    }
                }
            });

            setResizable(true);
        }

        protected SLPattern getFocusedSLPattern() {
            if (engine.getFocusedChannel() instanceof LXChannel) {
                LXChannel focusedChannel = (LXChannel) engine.getFocusedChannel();
                LXPattern focusedPattern = focusedChannel.getFocusedPattern();
                if (focusedPattern instanceof SLPattern) {
                    return (SLPattern) focusedPattern;
                }
            }
            return null;
        }

        @Override
        protected void beginDraw() {
            if (this.toggleHelpBar) {
                this.toggleHelpBar = false;
                toggleHelpBar();
            }
            if (this.toggleClipView) {
                this.toggleClipView = false;
                _toggleClipView();
            }
            if (this.toggleSidebars) {
                this.toggleSidebars = false;
                _toggleSidebars();
            }
        }

        public boolean isClipViewVisible() {
            return this.clipViewVisible;
        }

        private void setClipViewVisible(boolean visible) {
            if (this.clipViewVisible != visible) {
                toggleClipView();
            }
        }

        public boolean toggleClipView() {
            this.toggleClipView = true;
            return (this.clipViewVisible = !this.clipViewVisible);
        }

        public boolean areSidebarsVisible() {
            return this.sidebarsVisible;
        }

        private void setSidebarsVisible(boolean visible) {
            if (this.sidebarsVisible != visible) {
                toggleSidebars();
            }
        }

        public void toggleSidebars() {
            this.toggleSidebars = true;
            sidebarsVisible = !sidebarsVisible;
        }

        @Override
        protected void onResize() {
            reflow();
        }

        private void toggleHelpBar() {
            this.helpBar.toggleVisible();
            reflow();
        }

        private void _toggleClipView() {
            // Mixer section
            float controlsY = this.clipViewVisible ? UIMixer.PADDING + UIClipLauncher.HEIGHT : 0;
            float stripHeight = this.clipViewVisible ? UIMixerStrip.HEIGHT : UIMixerStripControls.HEIGHT;

            UIMixer mixer = this.bottomTray.mixer;
            for (UIMixerStrip strip : mixer.channelStrips.values()) {
                strip.clipLauncher.setVisible(false);
                strip.controls.setY(controlsY);
                strip.setHeight(stripHeight);
            }
            mixer.addChannelButton.setY(controlsY + UIMixer.PADDING);
            mixer.masterStrip.clipLauncher.setVisible(false);
            mixer.masterStrip.controls.setY(controlsY);
            mixer.masterStrip.setHeight(stripHeight);
            mixer.sceneStrip.sceneLauncher.setVisible(false);
            mixer.sceneStrip.clipViewToggle.setY(controlsY);
            mixer.sceneStrip.setHeight(stripHeight);
            mixer.setContentHeight(stripHeight + 2 * UIMixer.PADDING);

            // Clip/device section
            this.bottomTray.clipView.setVisible(this.clipViewVisible);
            float binY = this.clipViewVisible
                ? UIClipView.HEIGHT + UIBottomTray.PADDING + UIMixerStrip.SPACING - 1
                : UIMixerStrip.SPACING;
            for (UIDeviceBin bin : this.bottomTray.deviceBins.values()) {
                bin.setY(binY);
            }
            this.bottomTray.rightSection.setHeight(stripHeight + 2 * UIMixer.PADDING);

            // Overall height
            this.bottomTray.setHeight(this.clipViewVisible ? UIBottomTray.HEIGHT : UIBottomTray.CLOSED_HEIGHT);

            // Reflow the UI
            reflow();
        }

        private void _toggleSidebars() {
            leftPane.setVisible(sidebarsVisible);
            rightPane.setVisible(sidebarsVisible);
        }

        @Override
        public void reflow() {
            float uiWidth = getWidth();
            float uiHeight = getHeight();
            float helpBarHeight = this.helpBar.isVisible() ? UIContextualHelpBar.VISIBLE_HEIGHT : 0;
            float bottomTrayHeight = this.bottomTray.getHeight();

            float bottomTrayY  = Math.max(100, uiHeight - bottomTrayHeight - helpBarHeight);
            if (!this.bottomTray.visible.getValueb()) {
                bottomTrayY += bottomTrayHeight;
            }

            this.bottomTray.setY(bottomTrayY);
            this.bottomTray.setWidth(uiWidth);
            this.bottomTray.reflow();

            this.helpBar.setY(uiHeight - helpBarHeight);
            this.helpBar.setWidth(uiWidth);
            this.leftPane.setHeight(bottomTrayY);
            this.rightPane.setHeight(bottomTrayY);
            this.rightPane.setX(uiWidth - this.rightPane.getWidth());

            /*
            UI2dScrollContext outputsOuterScrollContext = this.rightPane.utility;
            float listHeight = outputsOuterScrollContext.getHeight() - UIOutputs.TOP_MARGIN;
            UI2dScrollContext outputsInnerScrollContext = this.rightPane.uiOutputs.outputList;
            if (outputsInnerScrollContext.getHeight() != listHeight) {
                outputsInnerScrollContext.setHeight(listHeight);
            }
            UIOutputs outputs = this.rightPane.uiOutputs;
            if (outputs.getContentTarget().getHeight() != listHeight) {
                outputs.getContentTarget().setHeight(listHeight);
            }
            */

            this.preview.setSize(
                Math.max(100, uiWidth - this.leftPane.getWidth() - this.rightPane.getWidth()),
                Math.max(100, bottomTrayY - 10)
            );
        }

        private static final String KEY_AUDIO_EXPANDED = "audioExpanded";
        private static final String KEY_PALETTE_EXPANDED = "paletteExpanded";
        private static final String KEY_MODULATORS_EXPANDED = "modulatorExpanded";
        private static final String KEY_ENGINE_EXPANDED = "engineExpanded";
        private static final String KEY_CAMERA_EXPANDED = "cameraExpanded";
        private static final String KEY_CLIP_VIEW_VISIBLE = "clipViewVisible";
        private static final String KEY_PREVIEW = "preview";

        @Override
        public void save(LX lx, JsonObject object) {
            object.addProperty(KEY_AUDIO_EXPANDED, this.leftPane.audio.isExpanded());
            object.addProperty(KEY_PALETTE_EXPANDED, this.leftPane.palette.isExpanded());
            object.addProperty(KEY_ENGINE_EXPANDED, this.leftPane.engine.isExpanded());
            //object.addProperty(KEY_CAMERA_EXPANDED, this.leftPane.camera.isExpanded());
            object.addProperty(KEY_CLIP_VIEW_VISIBLE, this.clipViewVisible);
            JsonObject modulatorObj = new JsonObject();

            for (UIObject child : this.rightPane.modulation) {
                // TODO Java: Is this the right UIModfulator?
                if (child instanceof UIModulator) {
                    UIModulator uiModulator = (UIModulator) child;
                    modulatorObj.addProperty(uiModulator.getIdentifier(), uiModulator.isExpanded());
                }
            }
            object.add(KEY_MODULATORS_EXPANDED, modulatorObj);
            object.add(KEY_PREVIEW, Utils.toObject(lx, ui.preview));
        }

        @Override
        public void load(LX lx, JsonObject object) {
            if (object.has(KEY_AUDIO_EXPANDED)) {
                this.leftPane.audio.setExpanded(object.get(KEY_AUDIO_EXPANDED).getAsBoolean());
            }
            if (object.has(KEY_PALETTE_EXPANDED)) {
                this.leftPane.palette.setExpanded(object.get(KEY_PALETTE_EXPANDED).getAsBoolean());
            }
            if (object.has(KEY_ENGINE_EXPANDED)) {
                this.leftPane.engine.setExpanded(object.get(KEY_ENGINE_EXPANDED).getAsBoolean());
            }
            if (object.has(KEY_CAMERA_EXPANDED)) {
                this.leftPane.camera.setExpanded(object.get(KEY_CAMERA_EXPANDED).getAsBoolean());
            }
            if (object.has(KEY_CLIP_VIEW_VISIBLE)) {
                setClipViewVisible(object.get(KEY_CLIP_VIEW_VISIBLE).getAsBoolean());
            }
            if (object.has(KEY_MODULATORS_EXPANDED)) {
                JsonObject modulatorObj = object.getAsJsonObject(KEY_MODULATORS_EXPANDED);
                for (UIObject child : this.rightPane.modulation) {
                    if (child instanceof UIModulator) {
                        UIModulator uiModulator = (UIModulator) child;
                        String identifier = uiModulator.getIdentifier();
                        if (modulatorObj.has(identifier)) {
                            uiModulator.setExpanded(modulatorObj.get(identifier).getAsBoolean());
                        }
                    }
                }
            }
            if (object.has(KEY_PREVIEW)) {
                ui.preview.load(lx, object.getAsJsonObject(KEY_PREVIEW));
            }
        }

        public void addMarkerSource(MarkerSource source) {
            markerPainter.addSource(source);
        }

        public void removeMarkerSource(MarkerSource source) {
            markerPainter.removeSource(source);
        }
    }

    public final UI ui;

    public SLStudioLX(PApplet applet, LXModel model) {
        this(applet, model, true);
    }

    public SLStudioLX(PApplet applet, LXModel model, boolean multiThreaded) {
        super(applet, model);

        this.ui = (UI)super.ui;

        onUIReady(this, ui);
        registerExternal(KEY_UI, ui);

        try {
            File projectFile = this.applet.saveFile(PROJECT_FILE_NAME);
            if (projectFile.exists()) {
                String[] lines = this.applet.loadStrings(PROJECT_FILE_NAME);
                if (lines != null && lines.length > 0) {
                    File file = this.applet.saveFile(lines[0]);
                    if (file.exists()) {
                        openProject(file);
                    }
                }
            } else {
                File defaultProject = this.applet.saveFile(DEFAULT_PROJECT_FILE);
                if (defaultProject.exists()) {
                    openProject(defaultProject);
                }
            }
        } catch (Exception x) {
            // ignored
        }

        engine.setThreaded(multiThreaded);
    }

    @Override
    protected void setProject(File file, ProjectListener.Change change) {
        super.setProject(file, change);
        if (file != null) {
            this.applet.saveStrings(
                PROJECT_FILE_NAME,
                new String[]{
                    // Relative path of the project file WRT the default save file location for the sketch
                    this.applet.saveFile(PROJECT_FILE_NAME).toPath().getParent().relativize(file.toPath()).toString()
                }
            );
        }
    }

    @Override
    protected UI buildUI() {
        return new UI(this);
    }

    protected void initialize(SLStudioLX lx, SLStudioLX.UI ui) {
        // Add all warps
        LXClassLoader.findWarps().stream().forEach(c -> lx.registerWarp(c));

        // Add all effects
        LXClassLoader.findEffects().stream().forEach(c -> lx.registerEffect(c));

        // Add all patterns
        LXClassLoader.findPatterns().stream().forEach(c -> lx.registerPattern(c));

        lx.registerPattern(heronarts.p3lx.pattern.SolidColorPattern.class);
        lx.registerPattern(IteratorTestPattern.class);

        lx.registerEffect(FlashEffect.class);
        lx.registerEffect(BlurEffect.class);
        lx.registerEffect(DesaturationEffect.class);
    }

    protected void onUIReady(SLStudioLX lx, SLStudioLX.UI ui) { }

    @Override
    public LXEffect instantiateEffect(final String className) {
        return super.instantiateEffect(LXClassLoader.guessExistingEffectClassName(className));
    }

    @Override
    public LXPattern instantiatePattern(final String className) {
        return super.instantiatePattern(LXClassLoader.guessExistingPatternClassName(className));
    }

    public LXPattern instantiatePattern(final Class<? extends LXPattern> c) {
        return super.instantiatePattern(c.getName());
    }
}
