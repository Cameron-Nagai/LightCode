/**
 * Copyright 2013- Mark C. Slee, Heron Arts LLC
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 *
 * ##library.name##
 * ##library.sentence##
 * ##library.url##
 *
 * @author      ##author##
 * @modified    ##date##
 * @version     ##library.prettyVersion## (##library.version##)
 */

package heronarts.p3lx.ui.studio;

import java.io.File;

import heronarts.lx.LX;
import heronarts.p3lx.ui.UI;
import heronarts.p3lx.ui.component.UIButton;
import heronarts.p3lx.ui.component.UILabel;
import processing.core.PConstants;

public class UIProjectManager extends UICollapsibleSection {

    private final LX lx;
    private final UILabel fileLabel;
    private File file;

    public UIProjectManager(final UI ui, final LX lx, float x, float y, float w, float h) {
        super(ui, x, y, w, h);
        this.lx = lx;
        this.file = lx.getProject();
        if (this.file == null) {
            this.file = ui.applet.saveFile("default.lxp");
        }

        setTitle("FILE");

        this.fileLabel = new UILabel(0, 0, getContentWidth(), 16);
        this.fileLabel
        .setLabel(this.file.getName())
        .setBackgroundColor(UI.BLACK)
        .setBorderRounding(4)
        .setTextAlignment(PConstants.CENTER, PConstants.CENTER)
        .addToContainer(this);

        lx.addProjectListener(new LX.ProjectListener() {
            public void projectChanged(File file) {
                UIProjectManager.this.file = file;
                fileLabel.setLabel(file.getName());
            }
        });

        new UIButton(0, 20, getContentWidth(), 16) {
            @Override
            public void onToggle(boolean on) {
                if (on) {
                    lx.saveProject(file);
                }
            }
        }
        .setLabel("Save")
        .setMomentary(true)
        .addToContainer(this);

        new UIButton(0, 40, getContentWidth(), 16) {
            @Override
            public void onToggle(boolean on) {
                if (on) {
                    ui.applet.selectOutput("Select a file to save to:", "onSave", ui.applet.saveFile("default.lxp"), UIProjectManager.this);
                }
            }
        }
        .setLabel("Save As...")
        .setMomentary(true)
        .addToContainer(this);

        new UIButton(0, 60, getContentWidth(), 16) {
            @Override
            public void onToggle(boolean on) {
                if (on) {
                    ui.applet.selectInput("Select a file to load:", "onLoad", ui.applet.saveFile("default.lxp"), UIProjectManager.this);
                }
            }
        }
        .setLabel("Load...")
        .setMomentary(true)
        .addToContainer(this);
    }

    public void onSave(final File saveFile) {
        if (saveFile != null) {
            lx.engine.addTask(new Runnable() {
                public void run() {
                    lx.saveProject(saveFile);
                }
            });
        }
    }

    public void onLoad(final File loadFile) {
        if (loadFile != null) {
            lx.engine.addTask(new Runnable() {
                public void run() {
                    lx.loadProject(loadFile);
                }
            });
        }
    }
}
