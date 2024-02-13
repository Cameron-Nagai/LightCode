package com.symmetrylabs.slstudio.ui.v2.ArtNetWindow;

import java.util.UUID;


public class Feature {
    private final String id; // Ensure ID is immutable
    private String name;

    public Feature(String name) {
        this.id = UUID.randomUUID().toString(); // Assign a unique ID upon creation
        this.name = name;
    }

    // Getters
    public String getId() { return id; }
    public String getName() { return name; }

    // Setter for name only
    public void setName(String name) { this.name = name; }
}
