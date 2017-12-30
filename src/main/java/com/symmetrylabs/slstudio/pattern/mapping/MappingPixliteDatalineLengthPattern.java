package com.symmetrylabs.slstudio.pattern.mapping;

import com.symmetrylabs.slstudio.SLStudio;
import com.symmetrylabs.slstudio.mappings.FultonStreetLayout;
import com.symmetrylabs.slstudio.mappings.Mappings;
import com.symmetrylabs.slstudio.mappings.PixliteMapping;
import com.symmetrylabs.slstudio.mappings.PixliteMapping.DatalineMapping;
import com.symmetrylabs.slstudio.pattern.SLPattern;
import com.symmetrylabs.slstudio.pixlites.Pixlite;
import heronarts.lx.LX;
import heronarts.lx.color.LXColor;
import heronarts.lx.parameter.BooleanParameter;
import heronarts.lx.parameter.DiscreteParameter;
import processing.event.KeyEvent;

import java.util.Arrays;
import java.util.Map;

public class MappingPixliteDatalineLengthPattern extends SLPattern {

        private final Mappings mappings;
        private final Map<String, int[]> mappingColorsPerPixlite;

        private final BooleanParameter enabled = new BooleanParameter("Enabled");

        private final DiscreteParameter pixliteId;
        private final DiscreteParameter datalineIndex;
        private final DiscreteParameter numPoints;

        public MappingPixliteDatalineLengthPattern(LX lx) {
                super(lx);
                mappings = FultonStreetLayout.mappings;
                mappingColorsPerPixlite = SLStudio.applet.mappingColorsPerPixlite;

                addParameter(enabled);
                enabled.setShouldSerialize(false);
                enabled.addListener(param -> SLStudio.applet.mappingModeEnabled.setValue(enabled.isOn()));
                enabled.addListener(param -> resetNumPoints());

                addParameter(pixliteId = new DiscreteParameter("Pixlite", mappings.getOutputIds().toArray(new String[0])));
                addParameter(datalineIndex = new DiscreteParameter("Dataline", PixliteMapping.NUM_DATALINES));
                addParameter(numPoints = new DiscreteParameter("# Points", 0, Pixlite.MAPPING_COLORS_POINTS_PER_DATALINE));
                numPoints.setShouldSerialize(false);

                pixliteId.addListener(param -> resetNumPoints());
                datalineIndex.addListener(param -> resetNumPoints());
                resetNumPoints();

                numPoints.addListener(param -> saveNumPoints());
        }

        @Override
        public void onKeyPressed(KeyEvent keyEvent, char keyChar, int keyCode) {
                if (keyCode == java.awt.event.KeyEvent.VK_LEFT) {
                        numPoints.decrement();
                        consumeKeyEvent();
                } else if (keyCode == java.awt.event.KeyEvent.VK_RIGHT) {
                        numPoints.increment();
                        consumeKeyEvent();
                }
        }

        private void clearColorsBuffer() {
                for (int[] mappingColors : mappingColorsPerPixlite.values()) {
                        Arrays.fill(mappingColors, LXColor.BLACK);
                }
        }

        private void resetNumPoints() {
                clearColorsBuffer();
                PixliteMapping pixliteMapping = mappings.getOutputById(pixliteId.getOption(), PixliteMapping.class);
                DatalineMapping datalineMapping = pixliteMapping.getDatalineMappings()[datalineIndex.getValuei()];
                numPoints.setValue(datalineMapping.numPoints);
        }

        private void saveNumPoints() {
                PixliteMapping pixliteMapping = mappings.getOutputById(pixliteId.getOption(), PixliteMapping.class);
                DatalineMapping datalineMapping = pixliteMapping.getDatalineMappings()[datalineIndex.getValuei()];
                datalineMapping.numPoints = numPoints.getValuei();
                FultonStreetLayout.saveMappings();
        }

        @Override
        protected void run(double deltaMs) {
                if (!enabled.isOn()) return;

                int[] mappingColors = mappingColorsPerPixlite.get(this.pixliteId.getOption());

                int currentDatalineIndex = this.datalineIndex.getValuei();
                int datalineStart = currentDatalineIndex * Pixlite.MAPPING_COLORS_POINTS_PER_DATALINE;

                int datalineEnd = datalineStart + numPoints.getValuei();
                int lastPixel = datalineEnd - 1;
                if (datalineStart <= lastPixel - 1) {
                        Arrays.fill(mappingColors, datalineStart, datalineEnd - 1, LXColor.rgb(0, 0, 127));
                }
                mappingColors[lastPixel] = LXColor.RED;
                Arrays.fill(mappingColors, lastPixel + 1,
                        datalineStart + Pixlite.MAPPING_COLORS_POINTS_PER_DATALINE, LXColor.GREEN);
        }
}
