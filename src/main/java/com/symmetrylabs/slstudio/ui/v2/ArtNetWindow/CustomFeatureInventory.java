package com.symmetrylabs.slstudio.ui.v2.ArtNetWindow;

import java.util.ArrayList;
import java.util.List;

public class CustomFeatureInventory {
    // List to store the features
    protected List<Feature> allFeatures;

    public CustomFeatureInventory() {
        this.allFeatures = new ArrayList<Feature>();

    }

    // Method to add a feature
    public void addFeature(Feature feature) {
        this.allFeatures.add(feature);
    }

    // Method to remove a feature
    public boolean removeFeature(Feature feature) {
        return this.allFeatures.remove(feature);
    }

    // Method to get all features
    public List<Feature> getAllFeatures() {
        return this.allFeatures;
    }

    // Additional methods as needed for managing features
}

