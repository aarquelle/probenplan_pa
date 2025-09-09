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
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;

import java.io.IOException;
import java.io.InputStream;

public class SwtUtils {
    public static boolean confirm(String text) {
        MessageBox box = new MessageBox(SwtGui.INSTANCE.getMainShell(), SWT.YES | SWT.NO);
        box.setMessage(text);
        return box.open() == SWT.YES;
    }

    public static Image getImage(String path) {
        try (InputStream in = SwtUtils.class.getResourceAsStream(path)) {
            if (in == null) {
                System.out.println("in is null");
                throw new IOException();
            }
            return new Image(Display.getCurrent(), in);

        } catch (IOException e) {
            System.out.println("Couldn't find image " + path);
            return new Image(Display.getCurrent(), 1,1);
        }
    }
}
