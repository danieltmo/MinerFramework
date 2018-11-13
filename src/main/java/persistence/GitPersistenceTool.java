/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package persistence;

/**
 *
 * @author Daniel
 */
import java.util.ArrayList;
import tool.Tool;

public class GitPersistenceTool extends Tool {

    @Override
    public String name() {
        return "persistenceGit";
    }
    
    @Override
    public String getDescription() {
        return "Uma ferramenta para armazenar os dados minerados de um projeto do git";
    }

    @Override
    public ArrayList<String> offeredProperties() {
        ArrayList<String> properties = new ArrayList<>();
        properties.add("Codigo fonte");
        properties.add("Diff");
        properties.add("Autor do commit");
        return properties;
    }

}
