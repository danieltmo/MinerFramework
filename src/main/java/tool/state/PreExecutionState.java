/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tool.state;

import org.repodriller.scm.SCMRepository;
import persistence.PersistenceMechanism;
import tool.ToolState;

/**
 *
 * @author Daniel
 */
public class PreExecutionState implements ToolState{
    
       
    private SCMRepository repo;
    private PersistenceMechanism writer;

    public PreExecutionState(SCMRepository repo, PersistenceMechanism writer) {
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
