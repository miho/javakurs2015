package eu.mihosoft.tutorial.threads01;


public class Main {
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Thread t1 = new Thread(new Runnable() {

            @Override
            public void run() {
                System.out.println(">> Message from another Thread.");
            }
        });

        t1.start();
        
        System.out.println(">> Message from Main-Thread");
    }
}
