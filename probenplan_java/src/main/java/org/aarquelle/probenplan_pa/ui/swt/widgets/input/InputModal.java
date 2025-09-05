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

package org.aarquelle.probenplan_pa.ui.swt.widgets.input;

import org.aarquelle.probenplan_pa.ui.swt.SwtGui;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Opens a modal with several input elements, a cancel button and an ok button that performs some action
 * based on the inputs.
 */
public class InputModal {

    private final Shell modal;
    private final List<InputWidget<?>> inputWidgets;
    private final Consumer<List<InputWidget<?>>> okConsumer;
    private final Composite bottomRow;

    public InputModal(String title, List<Input<?>> inputs,
                      Consumer<List<InputWidget<?>>> okConsumer) {
        this.inputWidgets = new ArrayList<>();
        this.okConsumer = okConsumer;

        modal = new Shell(SwtGui.INSTANCE.getMainShell(), SWT.APPLICATION_MODAL | SWT.DIALOG_TRIM | SWT.RESIZE);
        modal.setText(title);
        modal.setLayout(new GridLayout(1, true));

        for (Input<?> input : inputs) {
            InputWidget<?> widget = new InputWidget<>(modal, input);
            widget.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));
            inputWidgets.add(widget);
        }

        bottomRow = bottomRow();
    }

    private Composite bottomRow() {
        Composite bottomRow = new Composite(modal, 0);
        bottomRow.setLayout(new RowLayout());
        Button cancel = new Button(bottomRow, SWT.PUSH);
        cancel.setText("Cancel");
        cancel.addListener(SWT.Selection, f -> {
            modal.setVisible(false);
            modal.dispose();
        });

        Button ok = new Button(bottomRow, SWT.PUSH);
        ok.addListener(SWT.Selection, f -> {
            okConsumer.accept(getInputs());
            modal.setVisible(false);
            modal.dispose();
            SwtGui.INSTANCE.repaintSelectedPage();
        });
        ok.setText("Confirm");
        return bottomRow;
    }

    private List<InputWidget<?>> getInputs() {
        return inputWidgets;
    }

    public InputModal addButton(String name, boolean confirm, String confirmText,
                                Consumer<List<InputWidget<?>>> deleteConsumer) {
        Button deleteButton = new Button(bottomRow, SWT.PUSH);
        deleteButton.setText(name);
        deleteButton.addListener(SWT.Selection, e -> {
            boolean confirmed = confirm;
            if (!confirmed) {
                MessageBox confirmBox = new MessageBox(modal, SWT.YES | SWT.NO);
                confirmBox.setMessage(confirmText);
                confirmed = confirmBox.open() == SWT.YES;
            }
            if (confirmed) {
                deleteConsumer.accept(getInputs());
                modal.setVisible(false);
                modal.dispose();
                SwtGui.INSTANCE.repaintSelectedPage();
            }
        });
        return this;
    }

    public void open() {
        modal.pack();
        modal.open();
    }
}
