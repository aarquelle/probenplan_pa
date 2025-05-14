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

package org.aarquelle.probenplan_pa.business;

import org.aarquelle.probenplan_pa.entity.Scene;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.aarquelle.probenplan_pa.TestUtils.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AnalyzerTest {


    @BeforeAll
    static void setUp() throws BusinessException {
        createTestData();
    }


    @Test
    public void testMissingActors() {
        RehearsalSceneTable<Integer> results = Analyzer.missingActors();
        assertEquals(1, results.get(rehearsal1, scene1));
        assertEquals(0, results.get(rehearsal1, scene2));
        assertEquals(1, results.get(rehearsal1, scene3));
        assertEquals(1, results.get(rehearsal1, scene4));
        assertEquals(1, results.get(rehearsal1, scene5));

        assertEquals(1, results.get(rehearsal2, scene1));
        assertEquals(2, results.get(rehearsal2, scene2));
        assertEquals(1, results.get(rehearsal2, scene3));
        assertEquals(0, results.get(rehearsal2, scene4));
        assertEquals(2, results.get(rehearsal2, scene5));

        assertEquals(1, results.get(rehearsal3, scene1));
        assertEquals(1, results.get(rehearsal3, scene2));
        assertEquals(0, results.get(rehearsal3, scene3));
        assertEquals(0, results.get(rehearsal3, scene4));
        assertEquals(1, results.get(rehearsal3, scene5));

        assertEquals(0, results.get(rehearsal4, scene1));
        assertEquals(1, results.get(rehearsal4, scene2));
        assertEquals(1, results.get(rehearsal4, scene3));
        assertEquals(0, results.get(rehearsal4, scene4));
        assertEquals(1, results.get(rehearsal4, scene5));

        assertEquals(0, results.get(rehearsal5, scene1));
        assertEquals(0, results.get(rehearsal5, scene2));
        assertEquals(0, results.get(rehearsal5, scene3));
        assertEquals(0, results.get(rehearsal5, scene4));
        assertEquals(0, results.get(rehearsal5, scene5));
    }

    @Test
    public void testUncertainActors() {
        RehearsalSceneTable<Integer> results = Analyzer.uncertainActors();
        assertEquals(0, results.get(rehearsal1, scene1));
        assertEquals(0, results.get(rehearsal1, scene2));
        assertEquals(0, results.get(rehearsal1, scene3));
        assertEquals(0, results.get(rehearsal1, scene4));
        assertEquals(0, results.get(rehearsal1, scene5));

        assertEquals(0, results.get(rehearsal2, scene1));
        assertEquals(0, results.get(rehearsal2, scene2));
        assertEquals(0, results.get(rehearsal2, scene3));
        assertEquals(0, results.get(rehearsal2, scene4));
        assertEquals(0, results.get(rehearsal2, scene5));

        assertEquals(0, results.get(rehearsal3, scene1));
        assertEquals(0, results.get(rehearsal3, scene2));
        assertEquals(0, results.get(rehearsal3, scene3));
        assertEquals(0, results.get(rehearsal3, scene4));
        assertEquals(0, results.get(rehearsal3, scene5));

        assertEquals(0, results.get(rehearsal4, scene1));
        assertEquals(0, results.get(rehearsal4, scene2));
        assertEquals(0, results.get(rehearsal4, scene3));
        assertEquals(0, results.get(rehearsal4, scene4));
        assertEquals(0, results.get(rehearsal4, scene5));

        assertEquals(1, results.get(rehearsal5, scene1));
        assertEquals(0, results.get(rehearsal5, scene2));
        assertEquals(1, results.get(rehearsal5, scene3));
        assertEquals(1, results.get(rehearsal5, scene4));
        assertEquals(1, results.get(rehearsal5, scene5));
    }

    @Test
    public void testMissingMajorActors() {
        RehearsalSceneTable<Integer> results = Analyzer.majorMissingActors();
        assertEquals(1, results.get(rehearsal1, scene1));
        assertEquals(0, results.get(rehearsal1, scene2));
        assertEquals(1, results.get(rehearsal1, scene3));
        assertEquals(1, results.get(rehearsal1, scene4));
        assertEquals(1, results.get(rehearsal1, scene5));

        assertEquals(0, results.get(rehearsal2, scene1));
        assertEquals(2, results.get(rehearsal2, scene2));
        assertEquals(1, results.get(rehearsal2, scene3));
        assertEquals(0, results.get(rehearsal2, scene4));
        assertEquals(1, results.get(rehearsal2, scene5));

        assertEquals(0, results.get(rehearsal3, scene1));
        assertEquals(1, results.get(rehearsal3, scene2));
        assertEquals(0, results.get(rehearsal3, scene3));
        assertEquals(0, results.get(rehearsal3, scene4));
        assertEquals(0, results.get(rehearsal3, scene5));

        assertEquals(0, results.get(rehearsal4, scene1));
        assertEquals(1, results.get(rehearsal4, scene2));
        assertEquals(1, results.get(rehearsal4, scene3));
        assertEquals(0, results.get(rehearsal4, scene4));
        assertEquals(1, results.get(rehearsal4, scene5));

        assertEquals(0, results.get(rehearsal5, scene1));
        assertEquals(0, results.get(rehearsal5, scene2));
        assertEquals(0, results.get(rehearsal5, scene3));
        assertEquals(0, results.get(rehearsal5, scene4));
        assertEquals(0, results.get(rehearsal5, scene5));
    }

    @Test
    public void testUncertainMajorActors() {
        RehearsalSceneTable<Integer> results = Analyzer.majorUncertainActors();
        assertEquals(0, results.get(rehearsal1, scene1));
        assertEquals(0, results.get(rehearsal1, scene2));
        assertEquals(0, results.get(rehearsal1, scene3));
        assertEquals(0, results.get(rehearsal1, scene4));
        assertEquals(0, results.get(rehearsal1, scene5));

        assertEquals(0, results.get(rehearsal2, scene1));
        assertEquals(0, results.get(rehearsal2, scene2));
        assertEquals(0, results.get(rehearsal2, scene3));
        assertEquals(0, results.get(rehearsal2, scene4));
        assertEquals(0, results.get(rehearsal2, scene5));

        assertEquals(0, results.get(rehearsal3, scene1));
        assertEquals(0, results.get(rehearsal3, scene2));
        assertEquals(0, results.get(rehearsal3, scene3));
        assertEquals(0, results.get(rehearsal3, scene4));
        assertEquals(0, results.get(rehearsal3, scene5));

        assertEquals(0, results.get(rehearsal4, scene1));
        assertEquals(0, results.get(rehearsal4, scene2));
        assertEquals(0, results.get(rehearsal4, scene3));
        assertEquals(0, results.get(rehearsal4, scene4));
        assertEquals(0, results.get(rehearsal4, scene5));

        assertEquals(1, results.get(rehearsal5, scene1));
        assertEquals(0, results.get(rehearsal5, scene2));
        assertEquals(1, results.get(rehearsal5, scene3));
        assertEquals(1, results.get(rehearsal5, scene4));
        assertEquals(1, results.get(rehearsal5, scene5));
    }

    @Test
    void testCalculateCompletenessScore() {
        Analyzer.runAnalysis();
        RehearsalSceneTable<Double> results = Analyzer.scoreTable;
        
        assertDoubleEquals(1.0/3, results.get(rehearsal1, scene1));
        assertDoubleEquals(1, results.get(rehearsal1, scene2));
        assertDoubleEquals(0.5, results.get(rehearsal1, scene3));
        assertDoubleEquals(0, results.get(rehearsal1, scene4));
        assertDoubleEquals(3.0/5, results.get(rehearsal1, scene5));
    }

    @Test
    void testGetAllScenes() {
        List<Scene> scenes = Analyzer.getAllScenes();
        assertEquals(5, scenes.size());
        assertEquals(scene1, scenes.get(0));
        assertEquals(scene2, scenes.get(1));
        assertEquals(scene3, scenes.get(2));
        assertEquals(scene4, scenes.get(3));
        assertEquals(scene5, scenes.get(4));
    }

    @Test
    void testIsNextScene() {
        Analyzer.runAnalysis();
        assertTrue(Analyzer.isNextScene(scene1, scene2));
        assertTrue(Analyzer.isNextScene(scene2, scene3));
        assertTrue(Analyzer.isNextScene(scene3, scene4));
        assertTrue(Analyzer.isNextScene(scene4, scene5));
        assertFalse(Analyzer.isNextScene(scene5, scene1));
        assertFalse(Analyzer.isNextScene(scene1, scene3));
    }

    @Test
    void testNumberOfLumps() {
        Analyzer.runAnalysis();
        assertEquals(1, Analyzer.getNumberOfLumps(scene1));
        assertEquals(1, Analyzer.getNumberOfLumps(scene5));
        assertEquals(1, Analyzer.getNumberOfLumps(scene1, scene2, scene3));
        assertEquals(1, Analyzer.getNumberOfLumps(scene2, scene1, scene3));
        assertEquals(2, Analyzer.getNumberOfLumps(scene1, scene3));
        assertEquals(2, Analyzer.getNumberOfLumps(scene1, scene3, scene4, scene5));
        assertEquals(3, Analyzer.getNumberOfLumps(scene1, scene3, scene5));
    }
}