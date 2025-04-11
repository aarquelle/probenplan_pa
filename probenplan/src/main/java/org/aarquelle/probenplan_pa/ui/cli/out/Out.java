package org.aarquelle.probenplan_pa.ui.cli.out;

import org.aarquelle.probenplan_pa.dto.ActorDTO;
import org.aarquelle.probenplan_pa.dto.RehearsalDTO;
import org.aarquelle.probenplan_pa.dto.RoleDTO;
import org.aarquelle.probenplan_pa.dto.SceneDTO;
import org.aarquelle.probenplan_pa.util.DateUtils;

public class Out {
    //Color codes
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_YELLOW = "\u001B[33m";
    private static final String ANSI_BLUE = "\u001B[34m";
    private static final String ANSI_MAGENTA = "\u001B[35m";
    private static final String ANSI_CYAN = "\u001B[36m";
    private static final String ANSI_WHITE = "\u001B[37m";
    private static final String ANSI_DEFAULT = "\u001B[39m";

    private static final String ANSI_ITALIC = "\u001B[3m";


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

    public static void prActor(ActorDTO actor) {
        print(ANSI_YELLOW + actor.getName() + ANSI_RESET);
    }


    public static void prRole(RoleDTO role) {
        print(ANSI_GREEN + role.getName() + ANSI_RESET);
    }

    public static void prScene(SceneDTO scene) {
        print(ANSI_BLUE + scene.getName() + ANSI_RESET);
    }

    public static void prRehearsal(RehearsalDTO rehearsal) {
        print(ANSI_WHITE + DateUtils.getString(rehearsal.getDate()) + ANSI_RESET);
    }
}
