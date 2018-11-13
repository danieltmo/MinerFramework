/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tool;

/**
 *
 * @author Daniel
 */
public interface ToolCommand {

    boolean ENABLE = true;

    public void run(ToolState state);

    public String getName();

    public String getDescription();
}
