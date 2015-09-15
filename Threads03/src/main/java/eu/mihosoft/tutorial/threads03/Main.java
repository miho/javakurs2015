package eu.mihosoft.tutorial.threads03;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Shows how race conditions can mess up everything! Unfortunately, they usually
 * do this only sometimes :(
 *
 * Potential scenario:
 *
 * <pre>
 * {@code
 *  data.reset() = 0;
 *  Thread 1:  reads data.getValue() and stores it in a tmp var
 *  Thread 2:  reads data.getValue() and stores it in a tmp var
 *  Thread 2:  adds value 4 to the tmp var
 *  Thread 2:  writes tmp var value back via data.setValue() (tmp var=4 -> data.getValue() = 4)
 *  Thread 1:  adds value 5 to the tmp var
 *  Thread 1:  writes tmp var value back via data.setValue() (tmp var=5 -> data.getValue() = 5)
 * }
 * </pre>
 *
 * What happened? 4 + 5 is NOT 5, is it?
 *
 * @author Michael Hoffer &lt;info@michaelhoffer.de&gt;
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        // sample output:
        //
        // sum of numbers 1 to 100=5050
        // attempt #5: race condition! 5044!=5050

        final Data data = new Data();

        int n = 100;

        // sum from 1 to n:
        int s = n * (n + 1) / 2;
        System.out.println("sum of numbers 1 to " + n + "=" + s);

        // 100 attempts to produce a race condition
        int attempts = 100;
        for (int attempt = 0; attempt < attempts; attempt++) {

            // new attempt, reset data
            data.reset();

            // array that contains n+1 number of threads
            Thread[] threads = new Thread[n + 1];

            // do the math (compute s)
            for (int i = 0; i <= n; i++) {
                // strange way of telling lambda expression in
                // the thread to use i, even if it is not final
                final int finalI = i;
                threads[i] = new Thread(() -> {
//                    synchronized (data) {
                    data.setValue(data.getValue() + finalI);
//                    }
                });
                threads[i].start();
            }

            // join all threads
            for (Thread t : threads) {
                try {
                    t.join();
                } catch (InterruptedException ex) {
                    Logger.getLogger(Main.class.getName()).
                            log(Level.SEVERE, null, ex);
                }
            }

            // exit loop if we detected a race condition
            if (data.getValue() != s) {
                System.out.println("attempt #" + attempt + ": race condition! " + data.getValue() + "!=" + s);
                break;
            }
        }
    }
}
