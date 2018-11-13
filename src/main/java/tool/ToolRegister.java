/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tool;

import java.util.ArrayList;
import java.util.List;
import org.repodriller.persistence.NoPersistence;
import org.repodriller.persistence.PersistenceMechanism;

/**
 *
 * @author Daniel
 */
public class ToolRegister {

    private List<PersistenceTool> ptools = new ArrayList<>();

    public void registerTool(Tool newTool) {
        PersistenceTool t = new PersistenceTool(newTool, new NoPersistence());
        ptools.add(t);
    }

    public void registerTool(Tool newTool, PersistenceMechanism persistence) {
        PersistenceTool t = new PersistenceTool(newTool, persistence);
        ptools.add(t);
    }

    public void unregisterTool(Tool tool) {
        PersistenceTool toRemove = null;
        for (PersistenceTool pt : ptools) {
            if (pt.getTool().name().equals(tool.name())) {
                toRemove = pt;
            }
        }
        if (toRemove != null) {
            ptools.remove(toRemove);
        }
    }

    public List<PersistenceTool> getPtools() {
        return ptools;
    }

    
}
