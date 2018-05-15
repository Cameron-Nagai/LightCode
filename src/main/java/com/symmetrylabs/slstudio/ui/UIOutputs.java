package com.symmetrylabs.slstudio.ui;

import java.util.List;
import java.util.ArrayList;

import com.symmetrylabs.layouts.cubes.CubesController;
import com.symmetrylabs.layouts.cubes.CubesLayout;
import com.symmetrylabs.slstudio.network.NetworkDevice;
import heronarts.lx.LX;
import heronarts.p3lx.ui.UI;
import heronarts.p3lx.ui.component.UIButton;
import heronarts.p3lx.ui.component.UIItemList;
import heronarts.p3lx.ui.studio.UICollapsibleSection;

import com.symmetrylabs.slstudio.SLStudio;
import com.symmetrylabs.util.dispatch.Dispatcher;
import com.symmetrylabs.util.listenable.IntListener;
import com.symmetrylabs.util.listenable.ListListener;

public class UIOutputs extends UICollapsibleSection {
        private final UIItemList.ScrollList outputList;

        private Dispatcher dispatcher;

        public UIOutputs(LX lx, UI ui, CubesLayout layout, float x, float y, float w) {
                super(ui, x, y, w, 124);

                dispatcher = Dispatcher.getInstance(lx);

                outputList = new UIItemList.ScrollList(ui, 0, 22, w-8, 78);

                updateItems(layout);
                outputList.setSingleClickActivate(true);
                outputList.addToContainer(this);

                layout.addControllerListListener(new ListListener<CubesController>() {
                    public void itemAdded(final int index, final CubesController c) {
                        dispatcher.dispatchUi(() -> {
                            if (c.networkDevice != null) {
                                c.networkDevice.versionNumber.addListener(deviceVersionListener);
                            }

                            updateItems(layout);
                        });
                    }

                    public void itemRemoved(final int index, final CubesController c) {
                        dispatcher.dispatchUi(() -> {
                            if (c.networkDevice != null) {
                                c.networkDevice.versionNumber.removeListener(deviceVersionListener);
                            }

                            updateItems(layout);
                        });
                    }
                });

                UIButton testOutput = new UIButton(0, 0, w/2 - 8, 19) {
                    @Override
                    public void onToggle(boolean isOn) { }
                }.setLabel("Test Broadcast").setParameter(SLStudio.applet.outputControl.testBroadcast);
                testOutput.addToContainer(this);

                UIButton resetCubes = new UIButton(w/2-6, 0, w/2 - 1, 19) {
                    @Override
                    public void onToggle(boolean isOn) {
                        SLStudio.applet.outputControl.controllerResetModule.enabled.setValue(isOn);
                    }
                }.setMomentary(true).setLabel("Reset Controllers");
                resetCubes.addToContainer(this);

                addTopLevelComponent(new UIButton(4, 4, 12, 12) {}
                    .setParameter(SLStudio.applet.outputControl.enabled).setBorderRounding(4));

                SLStudio.applet.outputControl.enabled.addListener(param -> redraw());
        }

      private void updateItems(CubesLayout layout) {
            final List<UIItemList.Item> items = new ArrayList<UIItemList.Item>();
            for (CubesController c : layout.getSortedControllers()) { items.add(new ControllerItem(c)); }
            outputList.setItems(items);
            setTitle(items.size());
            redraw();
      }

        private final IntListener deviceVersionListener = version -> dispatcher.dispatchUi(this::redraw);

        private void setTitle(int count) {
                setTitle("OUTPUT (" + count + ")");
                setTitleX(20);
        }

        class ControllerItem extends UIItemList.AbstractItem {
                final CubesController controller;

                ControllerItem(CubesController _controller) {
                    this.controller = _controller;
                    controller.enabled.addListener(param -> redraw());
                }

                public String getLabel() {
                        NetworkDevice device = controller.networkDevice;
                        if (device != null && !device.versionId.isEmpty()) {
                                return controller.id + " (" + device.versionId + ")";
                        } else if (device != null && device.versionNumber.get() >= 0) {
                                return controller.id + " (v" + device.versionNumber + ")";
                        } else {
                                return controller.id;
                        }
                }

                public boolean isSelected() {
                        return controller.enabled.isOn();
                }

                @Override
                public boolean isActive() {
                        return controller.enabled.isOn();
                }

                @Override
                public int getActiveColor(UI ui) {
                        return isSelected() ? ui.theme.getPrimaryColor() : ui.theme.getSecondaryColor();
                }

                @Override
                public void onActivate() {
                        if (!SLStudio.applet.outputControl.enabled.getValueb())
                                return;
                        controller.enabled.toggle();
                }

                // @Override
                // public void onDeactivate() {
                //     println("onDeactivate");
                //     controller.enabled.setValue(false);
                // }
        }
}
