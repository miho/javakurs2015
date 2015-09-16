/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.mihosoft.tutorial.interpreter01;

import java.util.HashMap;
import java.util.Map;

/**
 * Simple command interpreter.
 * 
 * @author Michael Hoffer &lt;info@michaelhoffer.de&gt;
 */
class InterpreterImpl implements Interpreter {
    private final Map<String,Command> commands = new HashMap<>();
    
    /**
     * Adds a command to this interpreter.
     * @param cmdName command name
     * @param cmd command
     */
    @Override
    public void addCommand(String cmdName, Command cmd) {
        commands.put(cmdName, cmd);
    } 
    
    /**
     * Executes the specified command on this interpreter. 
     * @param cmdName name of the command
     * @param args arguments of the command
     * @return result
     */
    @Override
    public String execute(String cmdName, String args) {
        Command cmd = commands.get(cmdName);
       
        if (cmd == null) {
            throw new UnsupportedOperationException(
                    "Cmd " + cmdName + " not supported.");
        }
        
        return cmd.execute(args);
    } 
}
