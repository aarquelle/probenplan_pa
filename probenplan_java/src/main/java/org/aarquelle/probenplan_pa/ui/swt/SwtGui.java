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

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;

import java.util.List;

public class SwtGui {
    public static void main(String[] args) {
        Display display = new Display();
        Shell shell = new Shell(display);
        shell.setText("Probenplan");
        //shell.setSize(400, 300);
        shell.setLayout(new FillLayout());

        TabFolder tabFolder = new TabFolder(shell, SWT.TOP);
        tabFolder.setLayout(new FillLayout());
        TabItem timesTabItem = new TabItem(tabFolder, SWT.NONE);
        timesTabItem.setText("Termine");
        TabItem scenesTab = new TabItem(tabFolder, SWT.NONE);
        scenesTab.setText("Szenen");
        TabItem planTab = new TabItem(tabFolder, SWT.NONE);
        planTab.setText("Plan");


        //TODO demo values
        List<String> actorNames = List.of("Anton", "Berta", "Cedric", "Denise", "Emil", "Franzi");
        List<String> rehearsalNames = List.of("1.1", "2.1", "3.1");
        Composite timesComp = new TimesTab(tabFolder, actorNames, rehearsalNames);
        timesTabItem.setControl(timesComp);
//        timesComp.setLayout(new GridLayout());
//        Group timesImportGroup = new Group(timesComp, SWT.BORDER);
//        timesImportGroup.setLayoutData(new GridData(SWT.FILL, SWT.END, true, false));
//        timesImportGroup.setLayout(new GridLayout(4, true));
//        timesImportGroup.setText("Import times");
//        Button clipboardButton = new Button(timesImportGroup, SWT.PUSH);
//        clipboardButton.setText("From Clipboard");
//
//        Composite urlTimesComp = new Composite(timesImportGroup, SWT.BORDER);
//        urlTimesComp.setLayoutData(new GridData(SWT.FILL, SWT.END, true, false, 2, 1));
//        GridLayout urlLayout = new GridLayout(2, false);
//        urlTimesComp.setLayout(urlLayout);
//        Text timesUrlText = new Text(urlTimesComp, SWT.NONE);
//        timesUrlText.setLayoutData(new GridData(SWT.FILL, SWT.END, true, false));
//        Button timesUrlButton = new Button(urlTimesComp, SWT.PUSH);
//        timesUrlButton.setText("From URL");
//
//        Button timesImportFile = new Button(timesImportGroup, SWT.PUSH);
//        timesImportFile.setLayoutData(new GridData(SWT.END, SWT.END, false, false));
//        timesImportFile.setText("From file");


        /*Group buttonGroup = new Group(tabFolder, SWT.SHADOW_ETCHED_OUT);
        buttonGroup.setLayout(new RowLayout());

        Button b1 = new Button(buttonGroup, SWT.PUSH);
        Color red = new Color(display, 200, 0, 0);
        b1.setToolTipText("Tooltop");
        b1.setBackground(red);


        Button b2 = new Button(buttonGroup, SWT.CHECK);
        b1.setText("Hi");
        b2.setBackground(red);
        b2.setText("Ciao");
        Text help = new Text(buttonGroup, SWT.BORDER);
        help.setText("Click button for help");
        timesTab.setControl(buttonGroup);

        b1.addListener(SWT.Selection, e -> {
            help.setText("This is helpful");
        });*/

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
