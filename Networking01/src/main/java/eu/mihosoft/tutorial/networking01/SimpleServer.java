/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.mihosoft.tutorial.networking01;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Simple socket demo server.
 *
 * @author Michael Hoffer &lt;info@michaelhoffer.de&gt;
 */
public class SimpleServer {

    private final ServerSocket serverSocket;

    /**
     * Creates a new server with the specified port and timeout.
     *
     * @param port port
     * @param timeout timeout in ms
     * @throws IOException
     */
    public SimpleServer(int port, int timeout) throws IOException {
        serverSocket = new ServerSocket(port);
        serverSocket.setSoTimeout(timeout);
    }

    public void run() {

        Socket server = null;

        try {
            System.out.println(">> waiting for client on port "
                    + serverSocket.getLocalPort());

            // connecting to server
            server = serverSocket.accept();
            System.out.println(">> we connected to client from "
                    + server.getRemoteSocketAddress());
            // creating data input stream based on socket input stream
            DataInputStream in
                    = new DataInputStream(server.getInputStream());

            // reading from the socket
            System.out.println(in.readUTF());

            // creating date output stream based on server output stream
            DataOutputStream out
                    = new DataOutputStream(server.getOutputStream());

            // writing to socket via server data stream
            out.writeUTF("we are terminating connection to client "
                    + server.getLocalSocketAddress());
        } catch (SocketTimeoutException s) {
            System.out.println(">> socket timed out!");
        } catch (IOException ex) {
            Logger.getLogger(SimpleServer.class.getName()).
                    log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (server != null) {
                    server.close();
                }
            } catch (IOException ex) {
                Logger.getLogger(SimpleServer.class.getName()).
                        log(Level.SEVERE, null, ex);
            }
        }

    }

    public static void main(String[] args) {
        try {
            SimpleServer server
                    = new SimpleServer(/*port*/50000, /*timeout in ms*/ 10000);
            server.run();
        } catch (IOException ex) {
            Logger.getLogger(SimpleServer.class.getName()).
                    log(Level.SEVERE, null, ex);
        }
    }
}
