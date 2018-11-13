/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package framework;


import java.util.ArrayList;
import java.util.List;
import miner.GitCollector;
import repository.AnalysisRepository;
import tool.PersistenceTool;
import tool.ToolRegister;

/**
 *
 * @author Daniel
 */
public class CollectorFramework {

    private GitCollector colector;
    private AnalysisRepository repository;
    private CommitRange range = CommitRange.ALL;
    private String initialCommit;
    private String endCommit;
    private String singleCommit;
    private List<PersistenceTool> ptools = new ArrayList<>();

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

    public CollectorFramework single(String commit){
        range= CommitRange.SINGLE;
        this.singleCommit = commit;
        return this;
    }
    
    public CollectorFramework between(String initialCommit, String endCommit) {       
        this.initialCommit = initialCommit;
        this.endCommit = endCommit;
        return this;
    }

    public void run() {
        switch(range){
            case SINGLE:
                colector.collect(singleCommit);
                break;
            case ALL:
                colector.all().collect();
        }

    }

    public CollectorFramework tools(ToolRegister register) {
        this.ptools = register.getPtools();
        colector.setTools(ptools);
        return this;
    }

    enum CommitRange{
        SINGLE,
        BETWEEN,
        ALL,
        SINCE_DATE,
        BEFORE_DATE,
        LIST        
    }
    
}
