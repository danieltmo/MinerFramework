/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package persistence;

import tool.ToolCommand;
import tool.ToolState;
import tool.state.ExecutionState;

/**
 *
 * @author Daniel
 */
public class GitPersistenceCommand implements ToolCommand {

    @Override
    public void run(ToolState state) {
        if (state instanceof ExecutionState) {

            ExecutionState ee = (ExecutionState) state;
            PersistenceMechanism persistence = (PersistenceMechanism) ee.getWriter();

            Bundle b = new Bundle();
            b.put("commit", ee.getCommit());
            persistence.write(b);
        }

    }

    @Override
    public String getName() {
        return "Git Persistence";
    }

    @Override
    public String getDescription() {
        return "Coleta dados dos repositorios";
    }
    
    

}
