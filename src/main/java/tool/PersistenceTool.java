/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tool;

import persistence.PersistenceMechanism;

/**
 *
 * @author Daniel
 */
public class PersistenceTool{
    
    private Tool tool;
    private PersistenceMechanism persistenceMechanism;

    public PersistenceTool(Tool tool, PersistenceMechanism persistenceMechanism) {
        this.tool = tool;
        this.persistenceMechanism = persistenceMechanism;
    }

    public Tool getTool() {
        return tool;
    }

    public PersistenceMechanism getPersistenceMechanism() {
        return persistenceMechanism;
    }
    
    
    
}
