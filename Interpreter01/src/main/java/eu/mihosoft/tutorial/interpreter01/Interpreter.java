/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.mihosoft.tutorial.interpreter01;

/**
 *
 * @author Michael Hoffer &lt;info@michaelhoffer.de&gt;
 */
public interface Interpreter {

    /**
     * Adds a command to this interpreter.
     * @param cmdName command name
     * @param cmd command
     */
    void addCommand(String cmdName, Command cmd);

    /**
     * Executes the specified command on this interpreter.
     * @param cmdName name of the command
     * @param args arguments of the command
     * @return result
     */
    String execute(String cmdName, String args);
    
    /**
     * Creates a new interpreter instance.
     * @return interpreter instance
     */
    static Interpreter newInstance() {
        return new InterpreterImpl();
    }
    
}
