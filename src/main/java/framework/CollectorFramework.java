/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package framework;

import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import persistence.Bundle;
import persistence.LocalPersistence;
import persistence.MemoryPersistence;
import persistence.GitPersistenceCommand;
import persistence.GitPersistenceTool;
import repository.AnalysisRepository;
import tool.PersistenceTool;
import tool.ToolRegister;

/**
 *
 * @author Daniel
 */
public class CollectorFramework {

    private static Logger log = LogManager.getLogger(CollectorFramework.class);

    public static final Bundle globalBundle = new Bundle();
    private GitCollector colector;
    private AnalysisRepository repository;
    private CommitRange range = CommitRange.ALL;
    private String initialCommit;
    private String endCommit;
    private String singleCommit;

    public static CollectorFramework GetInstance() {
        return new CollectorFramework();
    }

    private CollectorFramework() {
    }

    public CollectorFramework repository(AnalysisRepository repository) {
        this.repository = repository;
        this.colector = new GitCollector(repository);
        return this;
    }

    public CollectorFramework single(String commit) {
        range = CommitRange.SINGLE;
        this.singleCommit = commit;
        return this;
    }

    public CollectorFramework between(String initialCommit, String endCommit) {
        this.initialCommit = initialCommit;
        this.endCommit = endCommit;
        return this;
    }

    public void run() throws CollectorFrameworkException {

        if (repository == null) {
            throw new CollectorFrameworkException("Nenhum repositorio registrado");
        }
        if (colector.getTools().isEmpty()) {
            throw new CollectorFrameworkException("Nenhuma ferramenta registrada");
        }

        switch (range) {
            case SINGLE:
                colector.collect(singleCommit);
                break;
            case ALL:
                colector.all().collect();
        }

    }

    public CollectorFramework tools(ToolRegister register) {        
        colector.addTools(register.getPtools());
        return this;
    }

    public CollectorFramework saveLocalGit() {
        colector.addTool(new PersistenceTool(getGitTool(), new LocalPersistence(repository)));
        return this;
    }

    public CollectorFramework saveMemoryGit() {
        colector.addTool(new PersistenceTool(getGitTool(), new MemoryPersistence(globalBundle)));
        return this;
    }

    private GitPersistenceTool getGitTool() {
        GitPersistenceTool tool = new GitPersistenceTool();
        tool.addCommand(new GitPersistenceCommand());        
        return tool;
    }

    enum CommitRange {
        SINGLE,
        BETWEEN,
        ALL,
        SINCE_DATE,
        BEFORE_DATE,
        LIST
    }

}
