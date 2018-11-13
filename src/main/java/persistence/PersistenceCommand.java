/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package persistence;

import org.repodriller.domain.Commit;
import tool.ToolCommand;
import tool.ToolState;
import tool.state.ExecutionState;

/**
 *
 * @author Daniel
 */
public class PersistenceCommand implements ToolCommand {
    
    @Override
    public void run(ToolState state) {
        if (state instanceof ExecutionState) {
            Commit currentCommit = (Commit) state.getStateData()[1];
            LocalPersistence persistence = (LocalPersistence) state.getStateData()[2];
            persistence.write(currentCommit);
        }
        
    }
    
    @Override
    public String getName() {
        return "Salvar repositorio";
    }
    
    @Override
    public String getDescription() {
        return "Salva localmente o repositorio e as informações solicitadas";
    }
    
}
