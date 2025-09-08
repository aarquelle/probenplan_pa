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

import org.aarquelle.probenplan_pa.business.BasicService;
import org.aarquelle.probenplan_pa.business.BusinessException;
import org.aarquelle.probenplan_pa.ui.swt.pages.ParamTab;
import org.aarquelle.probenplan_pa.ui.swt.pages.PlanTab;
import org.aarquelle.probenplan_pa.ui.swt.pages.ScenesTab;
import org.aarquelle.probenplan_pa.ui.swt.pages.TimesTab;
import org.aarquelle.probenplan_pa.ui.swt.widgets.CustomElements;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
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
    ParamTab params;
    public static SwtGui INSTANCE;
    Image appIcon;
    ResourceHandler resourceHandler;
    TabFolder tabFolder;


    public SwtGui() {
        //new Load(Main.URL).load(DataState.getInstance());
        display = new Display();
        shell = new Shell(display);

        appIcon = new Image(display, "assets/tadu_icon.png");
        resourceHandler = new ResourceHandler(display);

        shell.setImage(appIcon);
        shell.setText("Probenplan");
        //shell.setSize(400, 300);
        shell.setLayout(new GridLayout());

        tabFolder = new TabFolder(shell, SWT.TOP);
        GridData tabFolderData = new GridData(SWT.FILL, SWT.FILL, true, true);
        tabFolder.setLayoutData(tabFolderData);
        TabItem timesTabItem = new TabItem(tabFolder, SWT.NONE);
        timesTabItem.setText("Termine");
        TabItem scenesTabItem = new TabItem(tabFolder, SWT.NONE);
        scenesTabItem.setText("Szenen");
        TabItem planTabItem = new TabItem(tabFolder, SWT.NONE);
        planTabItem.setText("Plan");
        TabItem paramTabItem = new TabItem(tabFolder, 0);
        paramTabItem.setText("Parameters");

        times = new TimesTab(tabFolder);
        timesTabItem.setControl(times);
        scenes = new ScenesTab(tabFolder);
        scenesTabItem.setControl(scenes);
        plan = new PlanTab(tabFolder);
        planTabItem.setControl(plan);
        params = new ParamTab(tabFolder);
        paramTabItem.setControl(params);

        Composite persistenceRow = CustomElements.createImportRow(shell, null,
                List.of("Load", "Save"), List.of(false, false), List.of(
                        () -> {
                            BasicService.loadFromFile();
                            params.resetInputs();
                            repaintSelectedPage();
                        },

                        BasicService::saveToFile
                ));

        shell.pack();
        INSTANCE = this;
    }

    public void start() {
        shell.setMaximized(true);
        shell.open();
        while (!shell.isDisposed()) {
            try {
                if (!display.readAndDispatch()) {
                    display.sleep();
                }
            } catch (BusinessException e) {
                MessageBox errorBox = new MessageBox(shell, SWT.ICON_ERROR);
                errorBox.setMessage(e.getMessage());
                errorBox.open();
            }
        }
        appIcon.dispose();
        resourceHandler.dispose();
        display.dispose();
    }

    public void repaintSelectedPage() {
        int selectionIndex = tabFolder.getSelectionIndex();
        if (selectionIndex >= 0) {
            TabItem selectedItem = tabFolder.getItem(selectionIndex);
            Control selectedControl = selectedItem.getControl();
            selectedControl.redraw();
            selectedControl.update();
        }
    }

    public Shell getMainShell() {
        return shell;
    }
}
