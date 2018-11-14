/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package framework;

import jade.core.ProfileImpl;
import jade.wrapper.*;
import java.util.ArrayList;
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
import util.Parse;

/**
 *
 * @author Daniel
 */
public class CollectorFramework {
    
    private static Logger log = LogManager.getLogger(CollectorFramework.class);
    public static Parse<PersistenceTool> parse = new Parse<>();
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

    //Methods only for agents usage
    public ArrayList<PersistenceTool> getTools() {
        return colector.getTools();
    }
    
    public void updateTools(ArrayList<PersistenceTool> tools) {
        colector.getTools().clear();
        colector.getTools().addAll(tools);
    }
    
    public void runWithAgents() throws StaleProxyException {
        
        for (PersistenceTool pt : colector.getTools()) {
            parse.addSubType(pt);
        }
        
        ContainerController cc = jade.core.Runtime.instance().createMainContainer(new ProfileImpl(true));
        AgentController sortAgentControl = cc.createNewAgent("sortAgent", "agent.SortAgent", new Object[]{this});
        sortAgentControl.start();        
        AgentController mainAgentControl = cc.createNewAgent("mainAgent", "agent.MainAgent", new Object[]{this});
        mainAgentControl.start();

        //System.exit(0);
    }
    
}
