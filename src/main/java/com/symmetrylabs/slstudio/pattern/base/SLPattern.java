package com.symmetrylabs.slstudio.pattern.base;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import com.google.common.reflect.TypeToken;
import com.symmetrylabs.slstudio.model.SLModel;
import heronarts.lx.LXModelComponent;
import processing.event.KeyEvent;
import processing.event.MouseEvent;

import heronarts.lx.LX;
import heronarts.lx.LXPattern;
import heronarts.lx.LXBuffer;
import heronarts.lx.model.LXModel;
import heronarts.lx.model.LXPoint;
import heronarts.lx.parameter.BooleanParameter;
import heronarts.lx.parameter.CompoundParameter;
import heronarts.lx.parameter.DiscreteParameter;
import heronarts.lx.parameter.LXParameter;

import com.symmetrylabs.slstudio.SLStudioLX;
import com.symmetrylabs.slstudio.render.Renderer;
import com.symmetrylabs.slstudio.render.InterpolatingRenderer;
import com.symmetrylabs.slstudio.render.Renderable;

public abstract class SLPattern<M extends SLModel> extends LXPattern implements Renderable {

    protected final SLStudioLX lx;
    protected M model;  // overrides LXPattern's model field with a more specific type

    private volatile Renderer renderer;
    private ReusableBuffer reusableBuffer = new ReusableBuffer();
    private boolean isManaged = false;

    public SLPattern(LX lx) {
        super(lx);
        this.lx = (SLStudioLX) lx;
        setModel(lx.model);
        createParameters();
    }

    @Override public M getModel() {  // overrides LXPattern's getModel() to return a more specific type
        return model;
    }

    @Override public LXModelComponent setModel(LXModel model) {
        this.model = asSpecializedModel(model);
        return super.setModel(model);
    }

    /** Gets the model class, M. */
    public Class getModelClass() {
        return getEmptyModel().getClass();
    }

    /** Gets an empty instance of the model class, M. */
    private M getEmptyModel() {
        String modelClassName = new TypeToken<M>(getClass()) {}.getType().getTypeName();
        M emptyModel;
        try {
            emptyModel = (M) Class.forName(modelClassName).getConstructor().newInstance();
        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException |
            InstantiationException | InvocationTargetException | ClassCastException e) {
            throw new RuntimeException(
                "Could not find a public default constructor for " + modelClassName + ": " + e);
        }
        return emptyModel;
    }

    /** Casts the given model to the M type if possible, otherwise instantiates an empty M instance. */
    private M asSpecializedModel(LXModel model) {
        try {
            if (getEmptyModel().getClass().isAssignableFrom(model.getClass())) {
                return (M) model;
            }
        } catch (ClassCastException e) { }
        return getEmptyModel();
    }

    protected void createParameters() { }

    protected Renderer createRenderer(LXModel model, int[] colors, Renderable renderable) {
        return new InterpolatingRenderer(model, colors, renderable);
        //return new SequentialRenderer(model, colors, renderable);
    }

    public synchronized void setManagedMode(boolean isManaged) {
        boolean wasManaged = this.isManaged;
        this.isManaged = isManaged;

        if (wasManaged && !isManaged) {
            onActive();
        }
        else if (!wasManaged && isManaged) {
            onInactive();
        }
    }

    @Override
    public void onActive() {
        super.onActive();

        synchronized (this) {
            if (!isManaged && renderer != null) {
                renderer = createRenderer(model, colors, this);
                renderer.start();
            }
        }
    }

    @Override
    public void onInactive() {
        super.onInactive();

        synchronized (this) {
            if (renderer != null) {
                renderer.stop();
                renderer = null;
            }
        }
    }

    @Override
    public void loop(double deltaMs) {
        try {
            super.loop(deltaMs);
        } catch (Exception e) {
            System.err.print("\nException in " + getClass().getSimpleName() + " pattern: ");
            e.printStackTrace(System.err);
        }
    }

    @Override
    public void dispose() {
        onInactive();

        super.dispose();
    }

    @Override
    protected void run(double deltaMs) {
        Renderer renderer = this.renderer;

        if (renderer != null) {
            renderer.run(deltaMs);
        }
        else {
            render(deltaMs, model.getPoints(), colors);
        }
    }

    @Override
    public void render(double deltaMs, List<LXPoint> points, int[] layer) { }

    protected <T extends LXParameter> T addParam(T param) {
        addParameter(param);
        return param;
    }

    protected BooleanParameter booleanParam(String name) {
        return addParam(new BooleanParameter(name));
    }

    protected BooleanParameter booleanParam(String name, boolean value) {
        return addParam(new BooleanParameter(name, value));
    }

    protected CompoundParameter compoundParam(String name, double value, double min, double max) {
        return addParam(new CompoundParameter(name, value, min, max));
    }

    protected DiscreteParameter discreteParameter(String name, int value, int min, int max) {
        return addParam(new DiscreteParameter(name, value, min, max));
    }

    public void unconsumeKeyEvent() {
        this.keyEventConsumed = false;
    }

    public void consumeKeyEvent() {
        this.keyEventConsumed = true;
    }

    public boolean keyEventConsumed() {
        return this.keyEventConsumed;
    }

    private boolean keyEventConsumed = false;

    public void onMousePressed(MouseEvent mouseEvent, float mx, float my) {}
    public void onMouseReleased(MouseEvent mouseEvent, float mx, float my) {}
    public void onMouseClicked(MouseEvent mouseEvent, float mx, float my) {}
    public void onMouseDragged(MouseEvent mouseEvent, float mx, float my, float dx, float dy) {}
    public void onMouseMoved(MouseEvent mouseEvent, float mx, float my) {}
    public void onMouseOver(MouseEvent mouseEvent) {}
    public void onMouseOut(MouseEvent mouseEvent) {}
    public void onMouseWheel(MouseEvent mouseEvent, float mx, float my, float delta) {}
    public void onKeyPressed(KeyEvent keyEvent, char keyChar, int keyCode) {}
    public void onKeyReleased(KeyEvent keyEvent, char keyChar, int keyCode) {}
    public void onKeyTyped(KeyEvent keyEvent, char keyChar, int keyCode) {}

    public SLPattern setBuffer(int[] buffer) {
        reusableBuffer.setArray(buffer);
        return (SLPattern)setBuffer(reusableBuffer);
    }

    private class ReusableBuffer implements LXBuffer {
        private int[] layer;

        @Override
        public int[] getArray() {
            return layer;
        }

        public void setArray(int[] layer) {
            this.layer = layer;
        }
    };
}
