/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.mihosoft.tutorial.networking01;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Creates a new client with the specified server host and port.
 *
 * @author Michael Hoffer &lt;info@michaelhoffer.de&gt;
 */
public class SimpleClient {

    private final String hostName;
    private final int port;

    /**
     * Creates a new client with the specified server and hostname.
     *
     * @param hostName server hostname
     * @param port server port
     */
    public SimpleClient(String hostName, int port) {
        this.hostName = hostName;
        this.port = port;
    }

    public void run() {

        System.out.println(">> connecting to server " + hostName
                + " on port " + port);

        // using try with resources
        try (Socket client = new Socket(hostName, port)) {

            System.out.println(">> successfully connected to "
                    + client.getRemoteSocketAddress());

            // creating data output stream based on client output stream
            DataOutputStream out = new DataOutputStream(client.getOutputStream());

            // writing message to client output stream
            out.writeUTF("message from client "
                    + client.getLocalSocketAddress() + ": my message");

            // creating date input stream based on client input stream
            DataInputStream in
                    = new DataInputStream(client.getInputStream());

            // reading message from server
            String messageFromServer = in.readUTF();

            System.out.println(">> message from server: \""
                    + messageFromServer + "\"");
        } catch (IOException ex) {
            Logger.getLogger(SimpleClient.class.getName()).
                    log(Level.SEVERE, null, ex);
        }
    }

    public static void main(String[] args) {
        SimpleClient client
                = new SimpleClient(
                        /*server address*/"localhost",
                        /*port*/ 50000);
        client.run();
    }
}
