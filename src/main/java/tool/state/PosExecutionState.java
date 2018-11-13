/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tool.state;

import org.repodriller.domain.Commit;
import org.repodriller.scm.SCMRepository;
import persistence.Bundle;
import persistence.PersistenceMechanism;
import tool.ToolState;

/**
 *
 * @author Daniel
 */
public class PosExecutionState implements ToolState{

    
    private SCMRepository repo;
    private PersistenceMechanism writer;

    public PosExecutionState(SCMRepository repo, PersistenceMechanism writer, Bundle memoryBundle) {
        this.repo = repo;
        this.writer = writer;
    }

    @Override
    public Object[] getStateData() {
       return new Object[]{this};
    }

    public SCMRepository getRepo() {
        return repo;
    }


    public PersistenceMechanism getWriter() {
        return writer;
    }
   
}
