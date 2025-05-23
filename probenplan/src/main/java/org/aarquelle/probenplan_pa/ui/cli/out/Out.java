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

package org.aarquelle.probenplan_pa.ui.cli.out;

import org.aarquelle.probenplan_pa.business.BasicService;
import org.aarquelle.probenplan_pa.business.Analyzer;
import org.aarquelle.probenplan_pa.entity.Plan;
import org.aarquelle.probenplan_pa.entity.Actor;
import org.aarquelle.probenplan_pa.entity.Rehearsal;
import org.aarquelle.probenplan_pa.entity.Role;
import org.aarquelle.probenplan_pa.entity.Scene;
import org.aarquelle.probenplan_pa.util.DateUtils;

public class Out {
    //Color codes
    protected static final String ANSI_RESET = "\u001B[0m";
    protected static final String ANSI_RED = "\u001B[31m";
    protected static final String ANSI_GREEN = "\u001B[32m";
    protected static final String ANSI_YELLOW = "\u001B[33m";
    protected static final String ANSI_BLUE = "\u001B[34m";
    protected static final String ANSI_MAGENTA = "\u001B[35m";
    protected static final String ANSI_CYAN = "\u001B[36m";
    protected static final String ANSI_WHITE = "\u001B[37m";
    protected static final String ANSI_DEFAULT = "\u001B[39m";
    protected static final String ANSI_ITALIC = "\u001B[3m";

    public static int getTerminalWidth() {
        return 80;
    }


    private static void println(String message) {
        System.out.println(message);
    }

    private static void print(String message) {
        System.out.print(message);
    }


    public static void line(String message) {
        println(message);
    }

    public static void pr(String message) {
        print(message);
    }

    public static void info(String message) {
        println(ANSI_CYAN + ANSI_ITALIC + message + ANSI_RESET);
    }

    public static void infoPr(String message) {
        print(ANSI_CYAN + ANSI_ITALIC + message + ANSI_RESET);
    }

    public static void error(String message) {
        println(ANSI_RED + message + ANSI_RESET);
    }

    public static String prompt(String prompt) {
        return ANSI_MAGENTA + prompt + ANSI_RESET + "> ";
    }

    public static void prActor(Actor actor) {
        print(ANSI_YELLOW + actor.getName() + ANSI_RESET);
    }


    public static void prRole(Role role) {
        print(ANSI_GREEN + role.getName() + ANSI_RESET);
    }

    public static void prScene(Scene scene) {
        print(ANSI_BLUE + scene.getName() + ANSI_RESET);
    }

    public static void prRehearsal(Rehearsal rehearsal) {
        print(ANSI_WHITE + DateUtils.getString(rehearsal.getDate()) + ANSI_RESET);
    }

    public static void plan(Plan plan) {
        for (Rehearsal r : BasicService.getRehearsals().stream().sorted().toList()) {
            Out.prRehearsal(r);
            Out.infoPr(": ");
            plan.get(r).forEach(s -> {
                Out.prScene(s);
                Out.infoPr("(");
                System.out.printf("%.2f", Analyzer.completenessScore(r, s));
                infoPr(")");
                Out.infoPr(", ");
            });
            Out.line("");
        }
        Out.info(plan.getTestResults().toString());
    }

    public static void percentage(double value) {
        if (value >= 1) {
            pr(ANSI_GREEN);
        } else if (value >= 0.5) {
            pr(ANSI_YELLOW);
        } else {
            pr(ANSI_RED);
        }
        int per = (int) (value * 100);
        pr(per + "%" + ANSI_RESET);
    }

    public static void hideCursor() {
        System.out.print("\033[?25l");
    }
    public static void showCursor() {
        System.out.print("\033[?25h");
    }

    public static void success(String message) {
        println(ANSI_GREEN + message + ANSI_RESET);
    }

    public static void successPr(String message) {
        print(ANSI_GREEN + message + ANSI_RESET);
    }


}
