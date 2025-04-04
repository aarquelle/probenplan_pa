package org.aarquelle.probenplan_pa.business.create;

import org.aarquelle.probenplan_pa.business.BusinessException;
import org.aarquelle.probenplan_pa.data.dao.Transaction;
import org.aarquelle.probenplan_pa.data.exception.DuplicateException;
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
            throw new BusinessException(e.getMessage());
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
                t.getCreateDAO().createHasNoTime(a.first(), rehearsal, a.second());
            }
            t.commit();
        } catch (DuplicateException e) {
            throw new BusinessException(e.getMessage());
        }
    }
}
