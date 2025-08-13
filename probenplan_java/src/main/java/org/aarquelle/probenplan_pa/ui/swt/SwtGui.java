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
import org.aarquelle.probenplan_pa.entity.DataState;
import org.aarquelle.probenplan_pa.persistence.Load;
import org.aarquelle.probenplan_pa.ui.swt.pages.ScenesTab;
import org.aarquelle.probenplan_pa.ui.swt.pages.TimesTab;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;

import java.util.List;

public class SwtGui {
    public static void main(String[] args) {
        new Load(Main.URL).load(DataState.getInstance());
        Display display = new Display();
        Shell shell = new Shell(display);
        shell.setText("Probenplan");
        //shell.setSize(400, 300);
        shell.setLayout(new FillLayout());

        TabFolder tabFolder = new TabFolder(shell, SWT.TOP);
        tabFolder.setLayout(new FillLayout());
        TabItem timesTabItem = new TabItem(tabFolder, SWT.NONE);
        timesTabItem.setText("Termine");
        TabItem scenesTabItem = new TabItem(tabFolder, SWT.NONE);
        scenesTabItem.setText("Szenen");
        TabItem planTabItem = new TabItem(tabFolder, SWT.NONE);
        planTabItem.setText("Plan");


        //TODO demo values
        List<String> actorNames = List.of("Antonius der Dritte Freiherr von Thurn und Taxis", "Berta", "Cedric", "Denise", "Emil", "Franzi");
        List<String> rehearsalNames = List.of("1.1", "2.1", "3.1");
        List<String> roleNames = List.of("König", "Hirte", "Held", "Bösewicht", "Bote", "Drache");
        List<String> sceneNames = List.of("1.1", "1.2", "1.3", "1.4", "2.1", "2.2", "2.3", "3.1", "Epilog");
        Composite timesComp = new TimesTab(tabFolder, actorNames, rehearsalNames);
        timesTabItem.setControl(timesComp);
        Composite scenesComp = new ScenesTab(tabFolder);
        scenesTabItem.setControl(scenesComp);

        shell.pack();
        shell.open();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
        display.dispose();
    }
}
