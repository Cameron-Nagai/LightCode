package com.symmetrylabs.slstudio.ui.v2.ArtNetWindow;

import com.symmetrylabs.slstudio.ui.v2.CloseableWindow;
import com.symmetrylabs.slstudio.ui.v2.FontLoader;
import com.symmetrylabs.slstudio.ui.v2.UI;
import heronarts.lx.LX;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;


public class ArtNetWindow extends CloseableWindow {
    protected final LX lx;
    // Assume CustomFeatureInventory is a class that manages your custom features
    protected final CustomFeatureInventory inventory;
    private List<Feature> allFeatures;
    private Map<String, Boolean> sectionStates = new HashMap<>();

    public List<Feature> getAllFeatures() {
        return this.allFeatures;
    }

    public ArtNetWindow(LX lx, CustomFeatureInventory inventory) {
        super("ArtNet");
        this.lx = lx;
        this.inventory = inventory;
    }

    @Override
    protected void windowSetup() {
        UI.setNextWindowDefaults(300, 50, 400, 600); // Adjust size as needed
    }

    @Override
    protected void drawContents() {
        allFeatures = new ArrayList<>();
        // Example: Display custom features in a list
        UI.pushFont(FontLoader.DEFAULT_FONT_L);
        UI.text(String.format("Features: %d items", inventory.allFeatures.size()));
        UI.popFont();

        // Example: Add a new feature
        if (UI.button("Add Controller")) {
            // Logic to add a new feature
            Feature newFeature = new Feature("IP");

            inventory.allFeatures.add(newFeature);
        }

        for (Feature feature : inventory.allFeatures) {
            String featureId = feature.getId();
            String sectionId = "ArtNet Controller##" + featureId;
            String inputId = "IPAddress##" + featureId;

            if (UI.collapsibleSection(sectionId)) {
                // Temporarily store the new name to avoid immediate state change
                String newName = UI.inputText(inputId, feature.getName());

                // Apply the new name after the UI element to minimize state disruption
                if (!newName.equals(feature.getName())) {
                    feature.setName(newName);
                }

                // Handle other interactions, like deleting the feature
                if (UI.button("Delete##" + feature.getId())) {
                    inventory.allFeatures.remove(feature);
                    break; // Exit the loop to avoid concurrent modification issues
                }
            }
        }
        // Additional UI elements as needed
    }

    // Additional methods as needed for your custom window
}
