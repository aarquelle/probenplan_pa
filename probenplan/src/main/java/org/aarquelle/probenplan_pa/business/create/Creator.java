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
                t.getCreateDAO().createPlaysIn(scene, r.getFirst(), r.getSecond());
            }
            t.commit();
        } catch (DuplicateException e) {
            throw new BusinessException(e.getMessage());
        }
    }

    @SafeVarargs
    public static void hasTime(RehearsalDTO rehearsal, Pair<ActorDTO, Boolean>... actor) throws BusinessException {
        try (Transaction t = new Transaction()) {
            for (Pair<ActorDTO, Boolean> a : actor) {
                t.getCreateDAO().createHasTime(a.getFirst(), rehearsal, a.getSecond());
            }
            t.commit();
        } catch (DuplicateException e) {
            throw new BusinessException(e.getMessage());
        }
    }
}
