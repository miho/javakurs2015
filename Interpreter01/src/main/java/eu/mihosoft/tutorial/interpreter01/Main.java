package eu.mihosoft.tutorial.interpreter01;

import java.util.Scanner;

public class Main {
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        // simulates the server-/client protocol
        InterpreterDemo demo = new InterpreterDemo();
        demo.run();
        
    }
}
