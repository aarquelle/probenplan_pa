/*
 * Copyright (c) 2025, Aaron Prott
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *
 */

package org.aarquelle.probenplan_pa.ui.swt;

import org.aarquelle.probenplan_pa.Main;
import org.aarquelle.probenplan_pa.business.BasicService;
import org.aarquelle.probenplan_pa.entity.DataState;
import org.aarquelle.probenplan_pa.persistence.Load;
import org.aarquelle.probenplan_pa.ui.swt.pages.PlanTab;
import org.aarquelle.probenplan_pa.ui.swt.pages.ScenesTab;
import org.aarquelle.probenplan_pa.ui.swt.pages.TimesTab;
import org.aarquelle.probenplan_pa.ui.swt.widgets.CustomGroups;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;

import java.util.List;

public class SwtGui {
    private final Display display;
    Shell shell;
    ScenesTab scenes;
    TimesTab times;
    PlanTab plan;
    public static SwtGui INSTANCE;
    Image appIcon;
    ResourceHandler resourceHandler;



    public SwtGui() {
        //new Load(Main.URL).load(DataState.getInstance());
        display = new Display();
        shell = new Shell(display);

        appIcon = new Image(display, "assets/tadu_icon.svg");
        resourceHandler = new ResourceHandler(display);

        shell.setImage(appIcon);
        shell.setText("Probenplan");
        //shell.setSize(400, 300);
        shell.setLayout(new GridLayout());

        TabFolder tabFolder = new TabFolder(shell, SWT.TOP);
        GridData tabFolderData = new GridData(SWT.FILL, SWT.FILL, true, true);
        tabFolder.setLayoutData(tabFolderData);
        TabItem timesTabItem = new TabItem(tabFolder, SWT.NONE);
        timesTabItem.setText("Termine");
        TabItem scenesTabItem = new TabItem(tabFolder, SWT.NONE);
        scenesTabItem.setText("Szenen");
        TabItem planTabItem = new TabItem(tabFolder, SWT.NONE);
        planTabItem.setText("Plan");

        times = new TimesTab(tabFolder);
        timesTabItem.setControl(times);
        scenes = new ScenesTab(tabFolder);
        scenesTabItem.setControl(scenes);
        plan = new PlanTab(tabFolder);
        planTabItem.setControl(plan);

        Composite persistenceRow = CustomGroups.createImportRow(shell, null,
                List.of("Load", "Save"), List.of(false, false), List.of(
                        () -> {
                            BasicService.loadFromFile();
                            updateData();
                        },

                        BasicService::saveToFile
                ));

        shell.pack();
        INSTANCE = this;
    }

    public void start() {
        shell.open();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
        appIcon.dispose();
        resourceHandler.dispose();
        display.dispose();
    }

    private void updateData() {
        scenes.updateData();
        times.updateData();
        plan.updateData();

        shell.redraw();
        shell.update();
        shell.pack();

    }

    public Shell getMainShell() {
        return shell;
    }
}
