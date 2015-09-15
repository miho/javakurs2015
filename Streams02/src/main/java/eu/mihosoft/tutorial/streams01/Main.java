package eu.mihosoft.tutorial.streams01;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class Main {
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        ByteArrayOutputStream bOut = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(bOut);
        
        out.writeUTF("Test");
        
        ByteArrayInputStream bIn = new ByteArrayInputStream(bOut.toByteArray());
        DataInputStream in = new DataInputStream(bIn);
        
        System.out.println(in.readUTF());
    }
}
