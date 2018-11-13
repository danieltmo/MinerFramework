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
public class MemoryPersistence implements PersistenceMechanism {

    private Bundle globalBundle;
    
    public MemoryPersistence(Bundle globalBundle) {
        this.globalBundle = globalBundle;
    }

    @Override
    public void write(Bundle subBundle) {
        for (String s : subBundle.getAllProperties().keySet()) {
            Object object = subBundle.get(s);
            globalBundle.put(s, object);
        }
    }

    @Override
    public void close() {

    }

}
