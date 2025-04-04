package org.aarquelle.probenplan_pa;

import org.aarquelle.probenplan_pa.business.BusinessException;
import org.aarquelle.probenplan_pa.business.create.Creator;
import org.aarquelle.probenplan_pa.data.dao.Transaction;
import org.aarquelle.probenplan_pa.dto.ActorDTO;
import org.aarquelle.probenplan_pa.dto.RehearsalDTO;
import org.aarquelle.probenplan_pa.dto.RoleDTO;
import org.aarquelle.probenplan_pa.dto.SceneDTO;
import org.aarquelle.probenplan_pa.util.Pair;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestUtils {

    public static ActorDTO alice, charlie, bob;
    public static RoleDTO hero, villain, messenger, shepherd;
    public static SceneDTO scene1, scene2, scene3, scene4, scene5;
    public static RehearsalDTO rehearsal1, rehearsal2, rehearsal3, rehearsal4, rehearsal5;


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

        Transaction.onStartup();
        try (Transaction t = new Transaction()) {
            t.getDBManager().clearDB();
            t.getDBManager().initDB();
            t.commit();
        }

        alice = new ActorDTO();
        alice.setName("Alice");

        charlie = new ActorDTO();
        charlie.setName("Charlie");

        bob = new ActorDTO();
        bob.setName("Bob");

        Creator.createActor(alice, charlie, bob);

        hero = new RoleDTO();
        hero.setName("Held");
        hero.setActor(alice);

        villain = new RoleDTO();
        villain.setName("Bösewicht");
        villain.setActor(bob);

        messenger = new RoleDTO();
        messenger.setName("Bote");
        messenger.setActor(charlie);

        shepherd = new RoleDTO();
        shepherd.setName("Hirte");
        shepherd.setActor(charlie);

        Creator.createRole(hero, villain, messenger, shepherd);

        scene1 = new SceneDTO();
        scene1.setName("Hirtenszene");
        scene1.setPosition(1);
        scene1.setLength(1);

        scene2 = new SceneDTO();
        scene2.setName("Botenszene");
        scene2.setPosition(2);
        scene2.setLength(1);

        scene3 = new SceneDTO();
        scene3.setName("Herausforderung");
        scene3.setPosition(3);
        scene3.setLength(1);

        scene4 = new SceneDTO();
        scene4.setName("Monolog");
        scene4.setPosition(4);
        scene4.setLength(1);

        scene5 = new SceneDTO();
        scene5.setName("Kampf");
        scene5.setPosition(5);
        scene5.setLength(1);

        Creator.createScene(scene5, scene2, scene3, scene4, scene1);

        rehearsal1 = new RehearsalDTO();
        rehearsal1.setDate(java.sql.Date.valueOf("2025-01-01"));

        rehearsal2 = new RehearsalDTO();
        rehearsal2.setDate(java.sql.Date.valueOf("2025-01-02"));

        rehearsal3 = new RehearsalDTO();
        rehearsal3.setDate(java.sql.Date.valueOf("2025-01-03"));

        rehearsal4 = new RehearsalDTO();
        rehearsal4.setDate(java.sql.Date.valueOf("2025-01-04"));

        rehearsal5 = new RehearsalDTO();
        rehearsal5.setDate(java.sql.Date.valueOf("2025-01-05"));

        Creator.createRehearsal(rehearsal1, rehearsal2, rehearsal3, rehearsal4, rehearsal5);

        Creator.takesPart(scene1, new Pair<>(hero, false), new Pair<>(shepherd, true));
        Creator.takesPart(scene2, new Pair<>(villain, false), new Pair<>(messenger, false));
        Creator.takesPart(scene3, new Pair<>(hero, false), new Pair<>(villain, false));
        Creator.takesPart(scene4, new Pair<>(hero, false));
        Creator.takesPart(scene5, new Pair<>(hero, false), new Pair<>(villain, false), new Pair<>(messenger, true));

        Creator.hasNoTime(rehearsal1, new Pair<>(alice, false));
        Creator.hasNoTime(rehearsal2, new Pair<>(bob, false), new Pair<>(charlie, false));
        Creator.hasNoTime(rehearsal3, new Pair<>(charlie, false));
        Creator.hasNoTime(rehearsal4, new Pair<>(bob, false));
        Creator.hasNoTime(rehearsal5, new Pair<>(alice, true));
    }




    public static void assertDoubleEquals(double expected, double actual) {
        assertEquals(expected, actual, 0.0001);
    }
}
