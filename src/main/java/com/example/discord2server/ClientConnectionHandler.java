package com.example.discord2server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ClientConnectionHandler extends Thread {

    private final Socket socket;
    private final IncomingConnectionsHandler parent;

    public Scanner in;
    public PrintWriter out;

    public ClientConnectionHandler(Socket socket, IncomingConnectionsHandler parent) throws IOException {
        this.socket = socket;
        this.parent = parent;

        in = new Scanner(socket.getInputStream());
        out = new PrintWriter(socket.getOutputStream(), true);

        //this is a synchronised method so it should prevent concurrency errors.
        parent.AddOutWriter(this);
    }

    @Override
    public void run() {
        System.out.println("Connection opened with " + socket);
        while (!socket.isClosed() && !currentThread().isInterrupted()) {
            String incoming = in.nextLine();
            System.out.println("Incoming: " + incoming);
            switch (incoming) {
                case Discord2Protocol.CRED_REQUEST -> {
                    //the client wants us to send the database credentials over
                    System.out.println("got credential request");
                    //get the array of credentials properly formatted and everything
                    String[] creds = Discord2Protocol.GetCredsToSend();
                    for (String cred : creds) {
                        //print each one followed by a newline
                        out.println(cred);
                    }
                } //if a client is telling us we sent a message, tell all the other clients that they need
                //to check their inbox.
                case Discord2Protocol.CLIENT_MESSAGE_SENT -> parent.Broadcast(Discord2Protocol.CLIENT_CHECK_MESSAGES);
                default -> System.err.println("unexpected message received");
            }
        }
        System.out.println("Connection closed");
        //close this thread
        interrupt();
    }

    //make sure we close the socket before we kill this thread.
    @Override
    public void interrupt() {
        System.out.println("Closing thread " + this);
        try {
            System.out.println("Closing socket " + socket);
            socket.close();
        } catch (IOException ignored) {
        } finally {
            super.interrupt();
        }

    }
}
