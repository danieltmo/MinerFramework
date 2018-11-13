/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tool;

import java.util.ArrayList;
import java.util.List;
import org.repodriller.domain.Commit;
import org.repodriller.persistence.PersistenceMechanism;
import org.repodriller.scm.CommitVisitor;
import org.repodriller.scm.SCMRepository;
import tool.state.ExecutionState;
import tool.state.PosExecutionState;
import tool.state.PreExecutionState;

/**
 *
 * @author Daniel
 */
public abstract class Tool implements CommitVisitor {

    private List<ToolCommand> commands = new ArrayList<>();

    public Tool addCommand(ToolCommand command) {
        commands.add(command);
        return this;
    }

    @Override
    public abstract String name();

    @Override
    public void initialize(SCMRepository repo, PersistenceMechanism writer) {
        runCommands(new PreExecutionState() {
            @Override
            public Object[] getStateData() {
                return new Object[]{repo, writer};
            }
        });
    }

    @Override
    public void process(SCMRepository repo, Commit commit, PersistenceMechanism writer) {
        runCommands(new ExecutionState() {
            @Override
            public Object[] getStateData() {
                return new Object[]{repo, commit, writer};
            }
        });
    }

    @Override
    public void finalize(SCMRepository repo, PersistenceMechanism writer) {
        runCommands(new PosExecutionState() {
            @Override
            public Object[] getStateData() {
                return new Object[]{repo, writer};
            }
        });
    }
    
    public ArrayList<String> requiredProperties(){
        return new ArrayList<>();
    }

    public abstract String getDescription();

    public abstract ArrayList<String> offeredProperties();

    private void runCommands(ToolState state) {
        commands.forEach((tc) -> {
            tc.run(state);
        });
    }

}
