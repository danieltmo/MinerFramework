package framework;

import java.io.IOException;
import persistence.GitPersistenceTool;
import persistence.LocalPersistence;
import persistence.PersistenceCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.repodriller.RepoDriller;
import repository.AnalysisRepository;
import repository.Source;
import tool.ToolRegister;

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

    public static void main(String[] args) throws GitAPIException, IOException {

        AnalysisRepository repository = AnalysisRepository.buildRepository("./okhttp", Source.LOCAL, "okhttp");

        ToolRegister register = new ToolRegister();
        register.registerTool(
                new GitPersistenceTool()
                        .addCommand(new PersistenceCommand()),
                new LocalPersistence(repository));

        CollectorFramework.GetInstance()
                .repository(repository)
                .tools(register)
                .run();
    }

}
