/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.mihosoft.tutorial.interpreter01;

/**
 * Command interface.
 *
 * @author Michael Hoffer &lt;info@michaelhoffer.de&gt;
 */
@FunctionalInterface
public interface Command {

    /**
     * Executes this command.
     *
     * @param args arguments that shall be passed to the command
     * @return result of the command execution
     */
    public String execute(String args);
}
