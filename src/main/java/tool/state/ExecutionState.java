/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tool.state;

import org.repodriller.domain.Commit;
import org.repodriller.scm.SCMRepository;
import persistence.PersistenceMechanism;
import tool.ToolState;

/**
 *
 * @author Daniel
 */
public class ExecutionState implements ToolState{
    
    private SCMRepository repo;
    private Commit commit;
    private PersistenceMechanism writer;

    public ExecutionState(SCMRepository repo, Commit commit, PersistenceMechanism writer) {
        this.repo = repo;
        this.commit = commit;
        this.writer = writer;
    }

    @Override
    public Object[] getStateData() {
       return new Object[]{this};
    }

    public SCMRepository getRepo() {
        return repo;
    }

    public Commit getCommit() {
        return commit;
    }

    public PersistenceMechanism getWriter() {
        return writer;
    }
    
    
   
}
