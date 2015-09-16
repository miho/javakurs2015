/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.mihosoft.tutorial.interpreter01;

import java.util.Date;
import java.util.Scanner;
import java.util.stream.IntStream;

/**
 * Server-/Client protocol demo.
 *
 * @author Michael Hoffer &lt;info@michaelhoffer.de&gt;
 */
public class InterpreterDemo {

    private boolean exit;

    /**
     * Runs the demo.
     */
    public void run() {

        // obtain an interpreter instance
        Interpreter interpreter = Interpreter.newInstance();

        // addValues command, takes two double values
        interpreter.addCommand("addValues", (argStr) -> {

            // split the argument string with ',' as delimiter
            String[] cmdArgs = argStr.split(",");

            // throw exception if the number of arguments does not match
            if (cmdArgs.length != 2) {
                throw new IllegalArgumentException(
                        "Wrong number of args: expected 2, got "
                        + cmdArgs.length);
            }

            // parse the arguments
            double v1 = Double.parseDouble(cmdArgs[0]);
            double v2 = Double.parseDouble(cmdArgs[1]);

            // perform the task
            return "" + (v1 + v2);
        });

        // prime number task, takes 1 argument (upper bound)
        interpreter.addCommand("primesInRange", (argStr) -> {
            String[] args = argStr.split(",");

            // throw exception if the number of arguments does not match
            if (args.length != 1) {
                throw new IllegalArgumentException(
                        "Wrong number of args: expected 1, got "
                        + args.length);
            }

            // parse the arguments
            int v1 = Integer.parseInt(args[0]);

            // perform the task
            int[] primeNumbers
                    = IntStream.range(2, v1).filter(PrimeUtil::isPrime).
                    toArray();

            // encode the result (numbers with ',' as delimiter)
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < primeNumbers.length; i++) {

                if (i > 0) {
                    sb.append(",");
                }

                sb.append(primeNumbers[i]);
            }

            return sb.toString();
        });

        // time command, takes zero arguments
        interpreter.addCommand("time", (cmdArgs) -> {
            return new Date().toString();
        });

        // exit command
        interpreter.addCommand("exit", (argStr) -> {
            exit = true;
            return "";
        });

        // use keyboard input for this demo
        Scanner sc = new Scanner(System.in);

        while (!exit) {
            // obtains the cmd name
            System.out.println("#> cmd: ");
            String cmd = sc.nextLine();
            // obtains the arguments
            System.out.println("#> args: ");
            String cmdArgs = sc.nextLine();

            // execute the specified command with the specified arguments
            try {
                String result = interpreter.execute(cmd, cmdArgs);
                System.out.println("-> " + result);
            } catch (UnsupportedOperationException ex) {
                System.err.println("#> ERROR: command '" + cmd + "' not found.");
            } catch (IllegalArgumentException ex) {
                System.err.println("#> ERROR: " + ex.getMessage());
            }
        }
    }
}
