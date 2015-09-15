package eu.mihosoft.tutorial.streams01;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Simple input stream sample.
 * 
 * @author Michael Hoffer &lt;info@michaelhoffer.de&gt;
 */
public class Main {

    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[]{1,2,3});

        while (true) {
            int data = input.read();
            if (data==-1) break;
            System.out.println("data: " + data);
        }
    }
}
