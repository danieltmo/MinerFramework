/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tool;

import java.util.ArrayList;
import java.util.List;
import org.repodriller.domain.Commit;
import persistence.PersistenceMechanism;
import org.repodriller.scm.CommitVisitor;
import org.repodriller.scm.SCMRepository;
import persistence.Bundle;
import tool.state.ExecutionState;
import tool.state.PosExecutionState;
import tool.state.PreExecutionState;

/**
 *
 * @author Daniel
 */
public abstract class Tool implements CommitVisitor{

    private List<ToolCommand> commands = new ArrayList<>();

    public Tool addCommand(ToolCommand command) {
        commands.add(command);
        return this;
    }

    @Override
    public abstract String name();

    @Override
    public void initialize(SCMRepository repo, PersistenceMechanism writer) {
        runCommands(new PreExecutionState(repo,writer));
    }

    @Override
    public void process(SCMRepository repo, Commit commit, PersistenceMechanism writer) {
        runCommands(new ExecutionState(repo,commit,writer));
    }

    @Override
    public void finalize(SCMRepository repo, PersistenceMechanism writer, Bundle memoryBundle) {
        runCommands(new PosExecutionState(repo,writer,memoryBundle));
    }
    
    public ArrayList<String> requiredProperties(){
        return new ArrayList<>();
    }

    public abstract String getDescription();

    public abstract ArrayList<String> offeredProperties();

    protected void runCommands(ToolState state) {
        commands.forEach((tc) -> {
            tc.run(state);
        });
    }

    public List<ToolCommand> getCommands() {
        return commands;
    }

    
}
