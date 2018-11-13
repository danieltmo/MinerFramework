/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package persistence;

import java.util.HashMap;
import repository.AnalysisRepository;

/**
 *
 * @author Daniel
 */
public class Bundle {

    private HashMap<String, Object> properties = new HashMap<>();

    public Bundle() {

    }

    public Bundle(AnalysisRepository repository) {
        properties.put("projectName", repository.getProjectName());
    }

    public HashMap<String, Object> getAllProperties(){
        return properties;
    }
    public void put(String key, Object value) {
        properties.put(key, value);
    }

    public Object get(String key) {
        return properties.get(key);
    }

}
