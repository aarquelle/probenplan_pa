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

package org.aarquelle.probenplan_pa;

import org.aarquelle.probenplan_pa.business.BusinessException;
import org.aarquelle.probenplan_pa.entity.Actor;
import org.aarquelle.probenplan_pa.entity.DataState;
import org.aarquelle.probenplan_pa.entity.Rehearsal;
import org.aarquelle.probenplan_pa.entity.Role;
import org.aarquelle.probenplan_pa.entity.Scene;
import org.aarquelle.probenplan_pa.util.DateUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestUtils {

    public static Actor alice, charlie, bob;
    public static Role hero, villain, messenger, shepherd;
    public static Scene scene1, scene2, scene3, scene4, scene5;
    public static Rehearsal rehearsal1, rehearsal2, rehearsal3, rehearsal4, rehearsal5;


    /**
     * Testszenario:
     * <p>
     * Alice spielt den Helden, Bob den Bösewicht und Charlie den Boten und den Hirten.
     * <p>
     * Szene 1: Hirtenszene: Held, Hirte (klein)
     * Szene 2: Botenszene: Bösewicht, Bote
     * Szene 3: Herausforderung: Held trifft Bösewicht.
     * Szene 4: Monolog: Held hält Monolog.
     * Szene 5: Kampf: Held trifft Bösewicht und Boten (klein).
     * <p>
     * Proben:
     * 1.1, 1.2, 1.3, 1.4, 1.5 2025
     * <p>
     * Alice kann am 1.1 nicht. Also: Nur Szene 2 am 1.1
     * Nur Alice kann am 1.2. Also: Szene 1 semi, Szene 4 am 1.2
     * Charlie kann am 1.3 nicht. Also: Szenen 3 und 4 ganz, Szenen 1 und 5 semi, Szene 2 gar nicht
     * Bob kann am 1.4 nicht. Also: Szene 1 und 4 ganz, Szenen 2, 3 und 5 gar nicht.
     * Am 1.5 können alle, aber Alice nur vielleicht. Also: Szene 2 ganz, Szenen 1, 3, 4, 5 semi.
     */
    public static void createTestData() throws BusinessException {

        Main.TEST_MODE = true;
        DataState ds = DataState.getInstance();
        

        alice = ds.createActor();
        alice.setName("Alice");

        charlie = ds.createActor();
        charlie.setName("Charlie");

        bob = ds.createActor();
        bob.setName("Bob");

        hero = ds.createRole();
        hero.setName("Held");
        hero.setActor(alice);

        villain = ds.createRole();
        villain.setName("Bösewicht");
        villain.setActor(bob);

        messenger = ds.createRole();
        messenger.setName("Bote");
        messenger.setActor(charlie);

        shepherd = ds.createRole();
        shepherd.setName("Hirte");
        shepherd.setActor(charlie);

        scene1 = ds.createScene();
        scene1.setName("Hirtenszene");
        scene1.setPosition(1);
        scene1.setLength(1);

        scene2 = ds.createScene();
        scene2.setName("Botenszene");
        scene2.setPosition(2);
        scene2.setLength(1);

        scene3 = ds.createScene();
        scene3.setName("Herausforderung");
        scene3.setPosition(3);
        scene3.setLength(1);

        scene4 = ds.createScene();
        scene4.setName("Monolog");
        scene4.setPosition(4);
        scene4.setLength(1);

        scene5 = ds.createScene();
        scene5.setName("Kampf");
        scene5.setPosition(5);
        scene5.setLength(1);

        rehearsal1 = ds.createRehearsal();
        rehearsal1.setDate(DateUtils.getLocalDate("01.01.2025"));

        rehearsal2 = ds.createRehearsal();
        rehearsal2.setDate(DateUtils.getLocalDate("01.02.2025"));

        rehearsal3 = ds.createRehearsal();
        rehearsal3.setDate(DateUtils.getLocalDate("01.03.2025"));

        rehearsal4 = ds.createRehearsal();
        rehearsal4.setDate(DateUtils.getLocalDate("01.04.2025"));

        rehearsal5 = ds.createRehearsal();
        rehearsal5.setDate(DateUtils.getLocalDate("01.05.2025"));

        scene1.addSmallRole(shepherd);
        scene1.addBigRole(hero);

        scene2.addBigRole(villain);
        scene2.addBigRole(messenger);

        scene3.addBigRole(hero);
        scene3.addBigRole(villain);

        scene4.addBigRole(hero);

        scene5.addBigRole(hero);
        scene5.addBigRole(villain);
        scene5.addSmallRole(messenger);

        rehearsal1.addMissingActor(alice);
        rehearsal2.addMissingActor(bob);
        rehearsal2.addMissingActor(charlie);
        rehearsal3.addMissingActor(charlie);
        rehearsal4.addMissingActor(bob);
        rehearsal5.addMaybeActor(alice);
    }




    public static void assertDoubleEquals(double expected, double actual) {
        assertEquals(expected, actual, 0.0001);
    }
}
