
import framework.CollectorFramework;
import framework.CollectorFrameworkException;
import java.io.IOException;
import java.util.ArrayList;
import org.eclipse.jgit.api.errors.GitAPIException;
import repository.AnalysisRepository;
import repository.Source;
import tool.Tool;

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

    public static void main(String[] args) throws GitAPIException, IOException, CollectorFrameworkException {

        AnalysisRepository repository = AnalysisRepository.buildRepository("https://github.com/mauricioaniche/repodriller-tutorial.git", Source.GIT, "tutorial");

        CollectorFramework.GetInstance()
                .repository(repository)
                .saveMemoryGit()
                .run();
    }

}

class MyTool extends Tool{

    @Override
    public String name() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getDescription() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ArrayList<String> offeredProperties() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
