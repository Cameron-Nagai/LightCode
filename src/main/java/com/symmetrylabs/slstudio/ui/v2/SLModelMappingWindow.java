package com.symmetrylabs.slstudio.ui.v2;

import com.symmetrylabs.shows.base.SLShow;
import com.symmetrylabs.slstudio.mappings.SLSculptureControllerMapping;
import com.symmetrylabs.slstudio.model.SLModel;
import com.symmetrylabs.slstudio.output.DiscoverableController;
import com.symmetrylabs.util.hardware.ControllerMetadata;
import com.symmetrylabs.util.hardware.SLControllerInventory;
import heronarts.lx.LX;
import heronarts.lx.model.LXModel;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class SLModelMappingWindow extends CloseableWindow {
    protected final LX lx;
    protected final SLModel model;
    protected final SLControllerInventory inventory;
    private String filter = "";
    private SLShow show;

    public SLModelMappingWindow(LX lx, SLShow show) {
        super("Mapping");
        this.lx = lx;
        this.show = show;
        this.inventory = show.controllerInventory;
        this.model = (SLModel) lx.model; // top level model
    }

    @Override
    protected void windowSetup() {
        UI.setNextWindowDefaults(300, 50, UIConstants.DEFAULT_WINDOW_WIDTH, 500);
    }

    @Override
    protected void drawContents() {
        List<SLModel> topoModel = new ArrayList<>();
        Iterator<? extends LXModel> it = model.getMappableFixtures();
        for (; it.hasNext(); ) {
            LXModel model = it.next();
            topoModel.add((SLModel) model);
        }
        UI.text("%d fixtures", topoModel.size());

        UI.textWrapped(
            "Output should update as soon as you hit enter, but changes will not be saved to disk unless you press Save.");

        boolean expand = UI.button("Expand all");
        UI.sameLine();
        boolean collapse = UI.button("Collapse all");

        UI.sameLine();
        if (UI.button("Save")) {
            if (!SLShow.mapping.save()) {
                UI.openPopup("saveFailed");
            }
        }
        if (UI.isItemHovered()) {
            UI.beginTooltip();
            UI.text("Saves the updated mapping to disk.");
            UI.endTooltip();
        }
        if (UI.beginPopup("saveFailed", true)) {
            UI.text("Failed to save mapping, check logs for details");
            if (UI.button("Close")) {
                UI.closePopup();
            }
            UI.endPopup();
        }

        UI.textWrapped("filters on model ID, associated phys ID, and associated controller IDs and MAC addresses");
        filter = UI.inputText("filter", filter);

        boolean anyUpdated = false;
        for (int i = 0; i < topoModel.size(); i++) {

            SLModel c = topoModel.get(i);

            SLSculptureControllerMapping.PhysIdAssignment pia = SLShow.mapping.lookUpByModelID(c.modelId);
            ControllerMetadata pc = pia != null ? show.controllerInventory.controllerByCtrlId.get(pia.humanID) : null;

            if (!filter.equals("")) {
                boolean modelIdMatch = c.modelId.contains(filter);
                boolean physIdMatch = pia != null && pia.humanID != null && pia.humanID.contains(filter);
                boolean ctrlIdMatch = pc != null && ((pc.getHumanID() != null && pc.getHumanID().contains(filter)));
                boolean ctrlAddrMatch = pc != null && ((pc.getMacAddr() != null && pc.getMacAddr().contains(filter)));
                if (!(modelIdMatch || physIdMatch || ctrlIdMatch || ctrlAddrMatch)) {
                    continue;
                }
            }

            if (expand) {
                UI.setNextTreeNodeOpen(true);
            } else if (collapse) {
                UI.setNextTreeNodeOpen(false);
            }

            String addControllerID = pia ==  null ? "" : " - " + pia.humanID;
            UI.CollapseResult cr = UI.collapsibleSection(c.modelId + addControllerID, false);
            if (UI.beginDragDropTarget()) {
                String physId = UI.acceptDragDropPayload("SL.ControllerHumanID", String.class);
                if (physId == null){
                    physId = UI.acceptDragDropPayload("SL.ControllerMacAddr", String.class);
                }

                if (physId != null) {
                    SLShow.mapping.setControllerAssignment(c.modelId, physId);
                    anyUpdated = true;
                }
            }

            if (pc != null){
                DiscoverableController cc = show.controllerByName.get(pc.getHumanID());
                if (cc != null){
                    cc.momentaryAltTestOutput.setValue(UI.isItemClicked(true) && UI.isAltDown());
                }
            }

            if (!cr.isOpen) {
                continue;
            }
//            UI.labelText("type", c.type.toString());

            UI.inputFloat3("position##" + i, new float[] {c.ax, c.ay, c.az, c.cx, c.cy, c.cz}, UI.INPUT_TEXT_FLAG_READ_ONLY);

            UI.labelText("controllerID", pia == null ? "(null)" : pia.humanID);

//            String oldPhysId = pia == null ? "" : pia.physicalId;
//            String newPhysId = UI.inputText(String.format("physid##%d", i), oldPhysId);
//            if (!oldPhysId.equals(newPhysId)) {
//                model.mapping.setControllerAssignment(c.modelId, newPhysId);
//                anyUpdated = true;
//            }
//            if (pia != null && pc != null) {
//                UI.text("Associated cube data");
//                UI.labelText("idA", pc.idA == null ? "(null)" : pc.idA);
//                UI.labelText("addrA", pc.addrA == null ? "(null)" : pc.addrA);
//                if (pc.idB != null || pc.addrB != null) {
//                    UI.labelText("idB", pc.idB == null ? "(null)" : pc.idB);
//                    UI.labelText("addrB", pc.addrB == null ? "(null)" : pc.addrB);
//                }
//            }
        }

        if (anyUpdated) {
            model.update(true, true);
        }
    }
}
