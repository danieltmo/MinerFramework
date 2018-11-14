
import framework.CollectorFramework;
import framework.CollectorFrameworkException;
import jade.wrapper.StaleProxyException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.repodriller.domain.Commit;
import org.repodriller.scm.SCMRepository;
import persistence.Bundle;
import persistence.PersistenceMechanism;
import repository.AnalysisRepository;
import repository.Source;
import tool.Tool;
import tool.ToolCommand;
import tool.ToolRegister;
import tool.ToolState;
import tool.state.PosExecutionState;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Daniel
 */
public class Main {

    public static void main(String[] args) throws GitAPIException, IOException, CollectorFrameworkException, StaleProxyException {

        AnalysisRepository repository = AnalysisRepository.buildRepository("https://github.com/mauricioaniche/repodriller-tutorial.git", Source.GIT, "tutorial");

        ToolRegister register = new ToolRegister();
        register.registerTool(new MyTool().addCommand(new CountDeveloperCommand())
                , new PersistenceMechanism() {
            @Override
            public void write(Bundle bundle) {
                File f = new File("./developer.txt");
                try {
                    FileWriter writer = new FileWriter(f);
                    writer.write(bundle.get("Developer#").toString());
                    writer.flush();
                    writer.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }

            @Override
            public void close() {
            }
        });

        CollectorFramework.GetInstance()
                .repository(repository)
                .tools(register)
                .run();
    }

}

class MyTool extends Tool {

    @Override
    public void process(SCMRepository repo, Commit commit, PersistenceMechanism writer) {
        runCommands(new CountDeveloperState(commit.getCommitter().getName()));
        super.process(repo, commit, writer);
    }

    @Override
    public String name() {
        return "myTool";
    }

    @Override
    public String getDescription() {
        return "Count different developers";
    }

    @Override
    public ArrayList<String> offeredProperties() {
        return new ArrayList<>();
    }

}

class CountDeveloperState implements ToolState {

    public String name;

    public CountDeveloperState(String name) {
        this.name = name;
    }

    @Override
    public Object[] getStateData() {
        return new Object[]{name};
    }

}

class CountDeveloperCommand implements ToolCommand {

    private TreeSet<String> names = new TreeSet<>();

    @Override
    public void run(ToolState state) {
        if (state instanceof CountDeveloperState) {
            CountDeveloperState cds = (CountDeveloperState) state;
            names.add(cds.getStateData()[0].toString());
            System.out.println(names);
        }

        if (state instanceof PosExecutionState) {
            PosExecutionState pstate = (PosExecutionState) state;
            Bundle b = new Bundle();
            b.put("Developer#", names.size());
            pstate.getWriter().write(b);
        }
    }

    @Override
    public String getName() {
        return "Count command";
    }

    @Override
    public String getDescription() {
        return "Count different developers that worked on project";
    }

}
