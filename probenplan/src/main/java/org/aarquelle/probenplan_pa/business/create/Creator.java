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

package org.aarquelle.probenplan_pa.business.create;

import org.aarquelle.probenplan_pa.business.BusinessException;
import org.aarquelle.probenplan_pa.data.dao.Transaction;
import org.aarquelle.probenplan_pa.data.exception.DuplicateException;
import org.aarquelle.probenplan_pa.data.exception.NoSuchDataException;
import org.aarquelle.probenplan_pa.data.exception.RequiredValueMissingException;
import org.aarquelle.probenplan_pa.dto.ActorDTO;
import org.aarquelle.probenplan_pa.dto.RehearsalDTO;
import org.aarquelle.probenplan_pa.dto.RoleDTO;
import org.aarquelle.probenplan_pa.dto.SceneDTO;
import org.aarquelle.probenplan_pa.util.Pair;

public class Creator {

    public static void createActor(ActorDTO... actor) throws BusinessException {
        try (Transaction t = new Transaction()) {
            for (ActorDTO a : actor) {
                t.getCreateDAO().createActor(a);
            }
            t.commit();
        } catch (DuplicateException e) {
            throw new BusinessException(e.getMessage());
        }
    }

    public static void createRole(RoleDTO... role) throws BusinessException {
        try (Transaction t = new Transaction()) {
            for (RoleDTO r : role) {
                t.getCreateDAO().createRole(r);
            }
            t.commit();
        } catch (DuplicateException e) {
            throw new BusinessException(e.getMessage());
        }
    }

    public static void createScene(SceneDTO... scene) throws BusinessException {
        try (Transaction t = new Transaction()) {
            for (SceneDTO s : scene) {
                t.getCreateDAO().createScene(s);
            }
            t.commit();
        } catch (DuplicateException | RequiredValueMissingException e) {
            throw new BusinessException(e.getMessage() + " caused by " + e.getCause());
        }
    }

    public static void createRehearsal(RehearsalDTO... rehearsal) throws BusinessException {
        try (Transaction t = new Transaction()) {
            for (RehearsalDTO r : rehearsal) {
                t.getCreateDAO().createRehearsal(r);
            }
            t.commit();
        } catch (DuplicateException | RequiredValueMissingException e) {
            throw new BusinessException(e.getMessage());
        }
    }


    @SafeVarargs
    public static void takesPart(SceneDTO scene, Pair<RoleDTO, Boolean>... role) throws BusinessException {
        try (Transaction t = new Transaction()) {
            for (Pair<RoleDTO, Boolean> r : role) {
                t.getCreateDAO().createPlaysIn(scene, r.first(), r.second());
            }
            t.commit();
        } catch (DuplicateException e) {
            throw new BusinessException(e.getMessage());
        }
    }

    /**
     * Creates a new HasNoTime entry for the given actor and rehearsal.
     * @param rehearsal The rehearsal, for which the actor has no time.
     * @param actor A pair of actor and maybe. The maybe flag is {@code true}, the actor might have time, if it is
     *              {@code false}, the actor has definitely no time.
     * @throws BusinessException
     */
    @SafeVarargs
    public static void hasNoTime(RehearsalDTO rehearsal, Pair<ActorDTO, Boolean>... actor) throws BusinessException {
        try (Transaction t = new Transaction()) {
            for (Pair<ActorDTO, Boolean> a : actor) {
                t.getReadDAO().fillActorDTO(a.first());
                t.getCreateDAO().createHasNoTime(a.first(), rehearsal, a.second());
            }
            t.commit();
        } catch (DuplicateException | NoSuchDataException e) { //TODO Ist Runtime für NoSuchData nicht besser?
            throw new BusinessException(e.getMessage());
        }
    }

    public static void lockScene(SceneDTO scene, RehearsalDTO rehearsal) throws BusinessException {
        try (Transaction t = new Transaction()) {
            t.getReadDAO().fillRehearsalDTO(rehearsal);
            t.getReadDAO().fillSceneDTO(scene);
            t.getCreateDAO().lockScene(scene, rehearsal);
            t.commit();
        } catch (DuplicateException | NoSuchDataException e) {
            throw new BusinessException(e.getMessage());
        }
    }

    public static void removeLock(SceneDTO scene, RehearsalDTO rehearsal) {
        try (Transaction t = new Transaction()) {
            t.getCreateDAO().removeLock(scene, rehearsal);
            t.commit();
        }
    }

    public static void clearLocks() {
        try (Transaction t = new Transaction()) {
            t.getCreateDAO().clearLocks();
            t.commit();
        }
    }

    public static void lockRehearsal(RehearsalDTO rehearsal, boolean locked) throws BusinessException {
        try (Transaction t = new Transaction()) {
            t.getReadDAO().fillRehearsalDTO(rehearsal);
            t.getCreateDAO().lockRehearsal(rehearsal, locked);
            t.commit();
        } catch (NoSuchDataException e) {
            throw new BusinessException(e.getMessage(), e);
        }
    }

    public static void updateRole(RoleDTO role) throws BusinessException {
        try (Transaction t = new Transaction()) {
            t.getReadDAO().fillActorDTO(role.getActor());
            t.getReadDAO().fillRoleDTO(role);
            t.getCreateDAO().updateRole(role);
            t.commit();
        } catch (RequiredValueMissingException | NoSuchDataException e) {
            throw new BusinessException(e.getMessage());
        }
    }

    public static void clearDB() {
        try (Transaction t = new Transaction()) {
            t.getDBManager().clearDB();
            t.getDBManager().initDB();
            t.commit();
        } catch (Exception e) {
            throw new RuntimeException("Error clearing database: " + e.getMessage(), e);
        }
    }
}
